package com.spring.LibraryService.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.service.BoardService;
import com.spring.LibraryService.vo.CustomerVO;

//게시판과 관련된 요청을 처리할 컨트롤러
@Controller("board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	//자유, 정보, 문의게시판들의 베이스 디렉토리
	private static String REPOSITORY_PATH = "D:\\LibraryService\\board\\";
	
	
	//============================================================================================
	//클라이언트가 요청한 게시판 종류에 따라 서로 다른 파라미터를 mav 객체에 할당함. 
	//============================================================================================
	@RequestMapping(value="/board/boardForm.do")
	public ModelAndView boardForm(HttpServletRequest request, HttpServletResponse response) {
		String BOARD = request.getParameter("BOARD");
		ModelAndView mav = new ModelAndView("board");
		if(BOARD==null) {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}else if(BOARD.equals("free_board")) {
			mav.addObject("BOARD", "free_board");
			return mav;
		}else if(BOARD.equals("info_board")) {
			mav.addObject("BOARD", "info_board");
			return mav;
		}else if(BOARD.equals("qna_board")) {
			mav.addObject("BOARD", "qna_board");
			return mav;
		}else {
			//게시판의 종류가 잘못되었을때, 메인화면으로 이동
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	//============================================================================================
	//게시글 작성 화면요청을 처리하는 메소드, 게시판 종류가 잘못되어있으면 메인화면으로 이동함.
	//============================================================================================
	@RequestMapping(value="/board/addArticleForm.do")
	public ModelAndView LOGONMAV_addArticleForm(HttpServletRequest request, HttpServletResponse response) {
		String BOARD = request.getParameter("BOARD");
		ModelAndView mav = new ModelAndView("addArticle");
		mav.addObject("PARENT_ARTICLE_ID", request.getParameter("PARENT_ARTICLE_ID"));
		if(BOARD==null) {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}else if(BOARD.equals("free_board")) {
			mav.addObject("BOARD", "free_board");
			return mav;
		}else if(BOARD.equals("info_board")) {
			mav.addObject("BOARD", "info_board");
			return mav;
		}else if(BOARD.equals("qna_board")) {
			mav.addObject("BOARD", "qna_board");
			return mav;
		}else {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	
	//============================================================================================
	//게시글 작성 요청을 처리하는 메소드
	//============================================================================================
	@RequestMapping(value="/board/addArticle.do")
	@ResponseBody
	public HashMap LOGONMAP_addArticle(MultipartHttpServletRequest request, HttpServletResponse response){
		HashMap result = new HashMap();
		try {
			String BOARD = request.getParameter("BOARD");
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
				result.put("FLAG", "FALSE");
				result.put("FALSE", "게시글 작성에 실패했습니다.");
				return result;
			}
			CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
			HashMap map = new HashMap();
			map.put("CUSTOMER_ID", customerVO.getCUSTOMER_ID());
			map.put("ARTICLE_TITLE", request.getParameter("ARTICLE_TITLE"));
			map.put("ARTICLE_CONTENT", request.getParameter("ARTICLE_CONTENT"));
			map.put("PARENT_ARTICLE_ID", Integer.parseInt(request.getParameter("PARENT_ARTICLE_ID")));
			map.put("ARTICLE_VIEWS", 0);
			map.put("BOARD", BOARD);
			
			result = boardService.addArticle(map);
			int ARTICLE_ID = (Integer) result.get("ARTICLE_ID");
			
			Iterator<String> iterator = request.getFileNames();
			List<HashMap> fileNames = new ArrayList<HashMap>();

			while(iterator.hasNext()) {
				String fileName = iterator.next();
				MultipartFile mFile = request.getFile(fileName);
				String originalFileName = mFile.getOriginalFilename();
				
				//무작위 SALT값과 SHA512 알고리즘을 이용해 무작위 임시 이름을 얻음
				//서로 위치는 달라도, 이름은 같은 이미지파일이 있을수 있기 때문에 이름 중복으로 인한 기본키 제약 위반을 회피함
				String tempFileName = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 32);
				
				//파일의 확장자를 얻고, 새로 발급받은 임시 파일이름에 확장자를 덧붙이고, 아래의 경로에 이미지를 저장함
				String fileExtension = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
				File dest = new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID+"\\"+tempFileName+fileExtension);
				
				//만약 첨부된 파일이 존재하면, 해당 파일에 대한 정보를 DB에 추가하기위해 map 객체에 저장함.
				HashMap info = new HashMap();
				info.put("tempFileName",tempFileName+fileExtension);
				info.put("originalFileName",originalFileName);
				fileNames.add(info);
				dest.mkdirs();
				mFile.transferTo(dest);
			}
			
			//첨부한 파일이 있을 경우에는, 게시글의 첨부파일들의 정보를 DB에 삽입함.
			if(fileNames.size()>0) {
				HashMap param = new HashMap();
				param.put("ARTICLE_ID", ARTICLE_ID);
				param.put("fileNames", fileNames);
				param.put("BOARD_FILE", BOARD+"_file");
				boardService.addFiles(param);
			}			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "게시글 작성에 실패했습니다.");
			return result;
		}
	}
	
	
	
	
	//============================================================================================
	//게시글을 수정하는 메소드, 자신의 게시글이어야만 수정이 가능하다.
	//============================================================================================
	@RequestMapping(value="/board/modifyArticle.do")
	@ResponseBody
	public HashMap LOGONMAP_modifyArticle(MultipartHttpServletRequest request, HttpServletResponse response) {
		HashMap result = new HashMap();
		try {
			String BOARD = request.getParameter("BOARD");
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
				result.put("FLAG", "FALSE");
				result.put("CONTENT", "게시글 수정에 실패했습니다.");
				return result;
			}
			CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
			HashMap map = new HashMap();
			int ARTICLE_ID = Integer.parseInt(request.getParameter("ARTICLE_ID"));
			map.put("CUSTOMER_ID", customerVO.getCUSTOMER_ID());
			map.put("ARTICLE_ID", ARTICLE_ID);
			map.put("ARTICLE_TITLE", request.getParameter("ARTICLE_TITLE"));
			map.put("ARTICLE_CONTENT", request.getParameter("ARTICLE_CONTENT"));
			map.put("BOARD", BOARD);
			map.put("BOARD_FILE", BOARD+"_file");
			return boardService.modifyArticle(request, map);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "게시글 수정에 실패했습니다.");
			return result;
		}		
	}
	
	
	
	
	//============================================================================================
	//게시글 목록을 얻는 메소드, 비동기식으로 응답하므로 화면 새로고침이 발생하지 않음. 편의성을 보장하기 위함.
	//게시글을 날짜순서로 내림차순 정렬했을때 (SECTION-1)*100+(PAGE-1)*10+1 번째 게시글부터 10개의 게시글을 얻음.
	//============================================================================================
	@RequestMapping(value="/board/listArticles.do")
	@ResponseBody
	public HashMap listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String BOARD = request.getParameter("BOARD");
		HashMap<String,String> result = new HashMap<String,String>();
		if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "게시글 목록 불러오기에 실패했습니다.");
			return result;
		}
		HashMap map = new HashMap();
		map.put("SECTION", request.getParameter("SECTION"));
		map.put("PAGE", request.getParameter("PAGE"));
		map.put("BOARD", BOARD);
		
		//FLAG는 사용자 ID, 게시글 제목, 게시글 내용중 어떤 것을 기준으로 게시글을 검색할 것인지를 나타냄.
		map.put("FLAG", request.getParameter("FLAG"));
		map.put("SEARCH", request.getParameter("SEARCH"));
		return boardService.listArticles(map);
	}

	
	

	//============================================================================================
	//특정 게시글을 읽고자 할 때 응답하는 메소드
	//============================================================================================
	@RequestMapping(value="/board/viewArticle.do")
	public ModelAndView viewArticle(HttpServletRequest request, HttpServletResponse response){
		try {
			String BOARD = request.getParameter("BOARD");
			int ARTICLE_ID = Integer.parseInt(request.getParameter("ARTICLE_ID"));
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
				return new ModelAndView("redirect:/customer/mainForm.do");
			}
			HashMap map = new HashMap();
			map.put("BOARD", BOARD);
			map.put("ARTICLE_ID", ARTICLE_ID);
			
			//게시글 읽기에 성공하면 viewArticle 뷰를 리턴하며, JSP의 JSTL 기능을 이용하여
			//동적으로 게시글 제목과 내용, 첨부된 이미지들에 대한 처리를 진행한다.
			return boardService.viewArticle(map);
		}catch(Exception e) {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	
	//============================================================================================
	//게시글을 삭제할때 요청하는 메소드, 해당 게시글이 자신의 게시글이어야만 삭제가 가능하다.
	//1차적으로 JSTL을 이용하여 삭제버튼을 보여줄지 말지 결정하며, 2차적으로 컨트롤러에서 로그인 여부를 AOP를 이용하여 걸러내고
	//3차적으로 DELETE를 수행할 때 WHERE에 CUSTOMER_ID와 같아야 삭제가 되도록 조건을 추가했다.
	//============================================================================================
	@RequestMapping(value="/board/deleteArticle.do")
	public ModelAndView LOGONMAV_deleteArticle(HttpServletRequest request, HttpServletResponse response){
		try {
			String BOARD = request.getParameter("BOARD");
			int ARTICLE_ID = Integer.parseInt(request.getParameter("ARTICLE_ID"));
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
				return new ModelAndView("redirect:/customer/mainForm.do");
			}
			HashMap map = new HashMap();
			map.put("BOARD", BOARD);
			map.put("ARTICLE_ID", ARTICLE_ID);
			
			//게시글 삭제에 성공하면 게시글 목록으로 이동한다.
			return boardService.deleteArticle(map);
		}catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	
	
	//============================================================================================
	//해당 게시글에 첨부된 이미지들을 서버에서 다운로드 요청을 할 때 응답하는 메소드.
	//============================================================================================
	@RequestMapping(value="/board/download.do")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response){
		try {
			String BOARD = request.getParameter("BOARD");
			String FILE_TEMP_NAME = request.getParameter("FILE_TEMP_NAME");
			int ARTICLE_ID = Integer.parseInt(request.getParameter("ARTICLE_ID"));
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))||FILE_TEMP_NAME==null) {
				return;
			}
			
			//해당 게시글의 이미지파일이 실제로 존재해야만 다운로드가 성공적으로 진행됨.
			File file = new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID+"\\"+FILE_TEMP_NAME);
			if(file.exists()) {				
				response.setHeader("Cache-Control", "no-cache");
				response.addHeader("Content-disposition", "attachment; fileName="+FILE_TEMP_NAME);
				OutputStream out = response.getOutputStream();
				byte[] buffer = new byte[1024*1024*10];
				FileInputStream in = new FileInputStream(file);
				while(true) {
					int cnt = in.read(buffer);
					if(cnt== -1) {
						break;
					}else {
						out.write(buffer,0,cnt);
					}
				}
				out.close();
				in.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
