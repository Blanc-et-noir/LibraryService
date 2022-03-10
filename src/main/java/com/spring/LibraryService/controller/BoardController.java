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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.service.BoardService;
import com.spring.LibraryService.vo.CustomerVO;

//�Խ��ǰ� ���õ� ��û�� ó���� ��Ʈ�ѷ�
@Controller("board")
public class BoardController {
	/*
	 
	 
	 
	 
	 
	 
	 
	 
	 
	@Autowired
	private BoardService boardService;
	//����, ����, ���ǰԽ��ǵ��� ���̽� ���丮
	private final static String REPOSITORY_PATH = "D:\\LibraryService\\board\\";
	
	
	//============================================================================================
	//Ŭ���̾�Ʈ�� ��û�� �Խ��� ������ ���� ���� �ٸ� �Ķ���͸� mav ��ü�� �Ҵ���. 
	//============================================================================================
	@RequestMapping(value="/board/boardForm.do")
	public ModelAndView boardForm(HttpServletRequest request, HttpServletResponse response) {
		String board = request.getParameter("board");
		ModelAndView mav = new ModelAndView("board");
		if(board==null) {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}else if(board.equals("free_board")) {
			mav.addObject("board", "free_board");
			return mav;
		}else if(board.equals("info_board")) {
			mav.addObject("board", "info_board");
			return mav;
		}else if(board.equals("qna_board")) {
			mav.addObject("board", "qna_board");
			return mav;
		}else {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	//============================================================================================
	//�Խñ� �ۼ� ȭ���û�� ó���ϴ� �޼ҵ�, �Խ��� ������ �߸��Ǿ������� ����ȭ������ �̵���.
	//============================================================================================
	@RequestMapping(value="/board/addArticleForm.do")
	public ModelAndView LOGONMAV_addArticleForm(HttpServletRequest request, HttpServletResponse response) {
		String board = request.getParameter("board");
		ModelAndView mav = new ModelAndView("addArticle");
		mav.addObject("parent_article_id", request.getParameter("parent_article_id"));
		if(board==null) {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}else if(board.equals("free_board")) {
			mav.addObject("board", "free_board");
			return mav;
		}else if(board.equals("info_board")) {
			mav.addObject("board", "info_board");
			return mav;
		}else if(board.equals("qna_board")) {
			mav.addObject("board", "qna_board");
			return mav;
		}else {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	
	//============================================================================================
	//�Խñ� �ۼ� ��û�� ó���ϴ� �޼ҵ�
	//============================================================================================
	@RequestMapping(value="/board/addArticle.do")
	public ResponseEntity<HashMap> LOGONMAP_addArticle(MultipartHttpServletRequest request, HttpServletResponse response){
		HashMap result = new HashMap();
		try {
			String board = request.getParameter("board");
			if(board==null||(!board.equals("free_board")&&!board.equals("qna_board")&&!board.equals("info_board"))) {
				result.put("flag", "FALSE");
				result.put("FALSE", "�Խñ� �ۼ��� �����߽��ϴ�.");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
			
			CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("CUSTOMER");
			HashMap map = new HashMap();
			map.put("customer_id", customerVO.getCUSTOMER_ID());
			map.put("article_title", request.getParameter("article_title"));
			map.put("ARTICLE_content", request.getParameter("ARTICLE_content"));
			map.put("PARENT_article_id", Integer.parseInt(request.getParameter("PARENT_article_id")));
			map.put("ARTICLE_VIEWS", 0);
			map.put("board", board);
			
			result = boardService.addArticle(map);
			int article_id = (Integer) result.get("article_id");
			
			Iterator<String> iterator = request.getFileNames();
			List<HashMap> fileNames = new ArrayList<HashMap>();

			while(iterator.hasNext()) {
				String fileName = iterator.next();
				MultipartFile mFile = request.getFile(fileName);
				String originalFileName = mFile.getOriginalFilename();
				
				//������ SALT���� SHA512 �˰����� �̿��� ������ �ӽ� �̸��� ����
				//���� ��ġ�� �޶�, �̸��� ���� �̹��������� ������ �ֱ� ������ �̸� �ߺ����� ���� �⺻Ű ���� ������ ȸ����
				String tempFileName = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 32);
				
				//������ Ȯ���ڸ� ���, ���� �߱޹��� �ӽ� �����̸��� Ȯ���ڸ� �����̰�, �Ʒ��� ��ο� �̹����� ������
				String fileExtension = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
				File dest = new File(REPOSITORY_PATH+"\\"+board+"\\"+article_id+"\\"+tempFileName+fileExtension);
				
				//���� ÷�ε� ������ �����ϸ�, �ش� ���Ͽ� ���� ������ DB�� �߰��ϱ����� map ��ü�� ������.
				HashMap info = new HashMap();
				info.put("tempFileName",tempFileName+fileExtension);
				info.put("originalFileName",originalFileName);
				fileNames.add(info);
				dest.mkdirs();
				mFile.transferTo(dest);
			}
			
			//÷���� ������ ���� ��쿡��, �Խñ��� ÷�����ϵ��� ������ DB�� ������.
			if(fileNames.size()>0) {
				HashMap param = new HashMap();
				param.put("article_id", article_id);
				param.put("fileNames", fileNames);
				param.put("board_FILE", board+"_file");
				boardService.addFiles(param);
			}			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "FALSE");
			result.put("content", "�Խñ� �ۼ��� �����߽��ϴ�.");
			return result;
		}
	}
	
	
	
	
	//============================================================================================
	//�Խñ��� �����ϴ� �޼ҵ�, �ڽ��� �Խñ��̾�߸� ������ �����ϴ�.
	//============================================================================================
	@RequestMapping(value="/board/modifyArticle.do")
	public ResponseEntity<HashMap> LOGONMAP_modifyArticle(MultipartHttpServletRequest request, HttpServletResponse response) {
		HashMap result = new HashMap();
		try {
			String board = request.getParameter("board");
			if(board==null||(!board.equals("free_board")&&!board.equals("qna_board")&&!board.equals("info_board"))) {
				result.put("flag", "false");
				result.put("content", "�Խñ� ������ �����߽��ϴ�.");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
			
			CustomerVO customerVO = (CustomerVO) request.getSession().getAttribute("customer");
			
			HashMap param = new HashMap();
			
			int article_id = Integer.parseInt(request.getParameter("article_id"));
			param.put("customer_id", customerVO.getCUSTOMER_ID());
			param.put("article_id", article_id);
			param.put("article_title", request.getParameter("article_title"));
			param.put("article_content", request.getParameter("article_content"));
			param.put("board", board);
			param.put("board_file", board+"_file");

			return boardService.modifyArticle(request, param);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "error");
			result.put("content", "�Խñ� ������ �����߽��ϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}		
	}
	
	
	
	
	//============================================================================================
	//�Խñ� ����� ��� �޼ҵ�, �񵿱������ �����ϹǷ� ȭ�� ���ΰ�ħ�� �߻����� ����. ���Ǽ��� �����ϱ� ����.
	//�Խñ��� ��¥������ �������� ���������� (SECTION-1)*100+(PAGE-1)*10+1 ��° �Խñۺ��� 10���� �Խñ��� ����.
	//============================================================================================
	@RequestMapping(value="/board/listArticles.do")
	public ResponseEntity<HashMap> listArticles(@RequestParam HashMap param) throws Exception{
		HashMap result = new HashMap();
		String board = (String)param.get("board");
		
		if(board==null||(!board.equals("free_board")&&!board.equals("qna_board")&&!board.equals("info_board"))) {
			result.put("flag", "false");
			result.put("content", "�Խñ� ��� �ҷ����⿡ �����߽��ϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}

		return boardService.listArticles(param);
	}

	
	

	//============================================================================================
	//Ư�� �Խñ��� �а��� �� �� �����ϴ� �޼ҵ�
	//============================================================================================
	@RequestMapping(value="/board/viewArticle.do")
	public ModelAndView viewArticle(@RequestParam HashMap param){
		try {
			String board = (String)param.get("board");
			
			if(board==null||(!board.equals("free_board")&&!board.equals("qna_board")&&!board.equals("info_board"))) {
				return new ModelAndView("redirect:/customer/mainForm.do");
			}

			//�Խñ� �б⿡ �����ϸ� viewArticle �並 �����ϸ�, JSP�� JSTL ����� �̿��Ͽ�
			//�������� �Խñ� ����� ����, ÷�ε� �̹����鿡 ���� ó���� �����Ѵ�.
			return boardService.viewArticle(param);
		}catch(Exception e) {
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	
	//============================================================================================
	//�Խñ��� �����Ҷ� ��û�ϴ� �޼ҵ�, �ش� �Խñ��� �ڽ��� �Խñ��̾�߸� ������ �����ϴ�.
	//1�������� JSTL�� �̿��Ͽ� ������ư�� �������� ���� �����ϸ�, 2�������� ��Ʈ�ѷ����� �α��� ���θ� AOP�� �̿��Ͽ� �ɷ�����
	//3�������� DELETE�� ������ �� WHERE�� CUSTOMER_ID�� ���ƾ� ������ �ǵ��� ������ �߰��ߴ�.
	//============================================================================================
	@RequestMapping(value="/board/deleteArticle.do")
	public ModelAndView LOGONMAV_deleteArticle(@RequestParam HashMap param){
		try {
			String board = (String)param.get("board");
			
			if(board==null||(!board.equals("free_board")&&!board.equals("qna_board")&&!board.equals("info_board"))) {
				return new ModelAndView("redirect:/customer/mainForm.do");
			}
			
			//�Խñ� ������ �����ϸ� �Խñ� ������� �̵��Ѵ�.
			return boardService.deleteArticle(param);
		}catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	
	
	//============================================================================================
	//�ش� �Խñۿ� ÷�ε� �̹������� �������� �ٿ�ε� ��û�� �� �� �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/board/download.do")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response){
		try {
			String board = request.getParameter("board");
			String file_temp_name = request.getParameter("file_temp_name");
			int article_id = Integer.parseInt(request.getParameter("article_id"));
			if(board==null||(!board.equals("free_board")&&!board.equals("qna_board")&&!board.equals("info_board"))||file_temp_name==null) {
				return;
			}
			
			//�ش� �Խñ��� �̹��������� ������ �����ؾ߸� �ٿ�ε尡 ���������� �����.
			File file = new File(REPOSITORY_PATH+"\\"+board+"\\"+article_id+"\\"+file_temp_name);
			if(file.exists()) {				
				response.setHeader("Cache-Control", "no-cache");
				response.addHeader("Content-disposition", "attachment; fileName="+file_temp_name);
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
	
	
	
	*/
}
