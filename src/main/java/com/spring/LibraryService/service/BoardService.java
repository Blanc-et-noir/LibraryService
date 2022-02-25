package com.spring.LibraryService.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.dao.BoardDAO;
import com.spring.LibraryService.encrypt.SHA;

@Service("boardService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class BoardService {
	@Autowired
	private BoardDAO boardDAO;
	private static String REPOSITORY_PATH = "D:\\LibraryService\\board";
	
	
	
	//============================================================================================
	//게시글 작성을 수행하는 서비스 메소드
	//============================================================================================
	public HashMap addArticle(HashMap map) throws Exception{
		//게시글 종류에 따라 새 게시글을 위한 새로운 게시글 ID를 얻음.
		//오라클DBMS 11XE에서는 기본 시퀀스문법만 지원하여 아래와 같이 처리함.
		map.put("ARTICLE_ID",boardDAO.getNewArticleID(map));
		
		//게시글 작성에 성공하면 성공했음을 응답, 실패시에 예외를 발생시킴.
		boardDAO.addArticle(map);
		HashMap result = new HashMap();
		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "게시글 작성에 성공했습니다.");
		result.put("ARTICLE_ID", map.get("ARTICLE_ID"));
		return result;
	}
	
	
	
	//============================================================================================
	//게시글에 첨부된 파일들에 대한 정보를 추가하는 메소드.
	//============================================================================================
	public void addFiles(HashMap map) throws Exception{
		boardDAO.addFiles(map);
	}
	
	
	
	
	//============================================================================================
	//게시글 수정을 수행하는 메소드. 자신의 게시글만 수정할 수 있다.
	//============================================================================================
	public HashMap modifyArticle(MultipartHttpServletRequest request, HashMap map) throws Exception{
		HashMap result = new HashMap();
		String BOARD = (String)map.get("BOARD");
		int ARTICLE_ID = (Integer)map.get("ARTICLE_ID");
		
		//먼저 자신의 게시글과 관련하여 게시글 제목, 내용을 수정함.
		boardDAO.modifyArticle(map);
		
		//게시글 제목과 내용수정에 성공하면 해당 게시글에 첨부된 파일들과 관련하여 처리를 진행해야함.
		//1. 원래 이미지를 수정없이 남겨두는 경우.
		//2. 원래 이미지를 다른 이미지로 변경한 경우.
		//3. 원래 이미지를 삭제한 경우.
		//4. 새로 추가한 이미지인 경우.
		//총 4가지로 이미지를 분류할 수 있는데, 이중 1번의 경우만 FILE_TEMP_NAMES 파라미터에 임시 이름이 담겨있다.
		//그외의 이미지들은 임시 이름이 없는데, 이렇게 임시 이름이 없는 2,3,4 이미지 파일들의 경우는 서버에 저장된 정보를
		//전부 삭제하고 그에 맞게 다시 추가해야한다.
		//1의 경우는 그대로 유지하면 된다.
		String[] FILE_TEMP_NAMES = request.getParameter("FILE_TEMP_NAMES").split("\\*");
		Iterator<String> iterator = request.getFileNames();
		List<HashMap> fileNames = new ArrayList<HashMap>();
		
		//수정되지 않은 그대로의 이미지가 존재하는 경우에는 아래와 같이 잠시 TEMP 디렉토리로 옮긴후에
		//앞서 언급한 2, 3, 4의 이미지들을 서버 저장소에서 제거한 후, 다시 TEMP 디렉토리에서 원래 디렉토리로 옮긴다.
		if(request.getParameter("FILE_TEMP_NAMES")!=null&&!request.getParameter("FILE_TEMP_NAMES").equals("")) {
			//임시로 1번 이미지들을 TEMP 디렉토리로 이동시킴.
			for(int i=0;i<FILE_TEMP_NAMES.length;i++) {
				File file = new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID+"\\"+FILE_TEMP_NAMES[i]);
				if(file.exists()) {
					FileUtils.moveFileToDirectory(file, new File(REPOSITORY_PATH+"\\"+BOARD+"\\temp"),true);
				}
			}
			
			//해당 게시글 디렉토리를 완전히 삭제함.
			FileUtils.deleteDirectory(new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID));
			
			//TEMP에서 해당 게시글 디렉토리로 다시 이미지 파일들을 이동시킴. 
			for(int i=0;i<FILE_TEMP_NAMES.length;i++) {
				File file = new File(REPOSITORY_PATH+"\\"+BOARD+"\\temp\\"+FILE_TEMP_NAMES[i]);
				if(file.exists()) {
					FileUtils.moveFileToDirectory(file, new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID),true);
				}
			}
		}else {
			//수정되지 않은 이미지파일이 없는 경우, 전부 변경된 이미지이므로 디렉토리 자체를 일단 삭제함.
			FileUtils.deleteDirectory(new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID));
		}
		
		iterator = request.getFileNames();

		//새로 변경되거나 추가된 이미지들에 대해서, 게시글 작성때와 마찬가지로 임시 이름을 할당하고
		//서버에 해당 이름과 확장자를 가진 파일의 형태로 저장함.
		while(iterator.hasNext()) {
			String fileName = iterator.next();
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			
			//게시글에 첨부된 이미지파일의 이름 중복을 회피하기위해, SHA512를 이용하여 무작위 이름을 할당함.
			String tempFileName = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 32);
			String fileExtension = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
			File dest = new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID+"\\"+tempFileName+fileExtension);
			HashMap info = new HashMap();
			info.put("tempFileName",tempFileName+fileExtension);
			info.put("originalFileName",originalFileName);
			fileNames.add(info);
			dest.mkdirs();
			mFile.transferTo(dest);
		}

		//수정되지않은 파일이 존재한다면 해당 파일을 제외한 모든 파일의 정보를 DB에서 수정함.
		if(request.getParameter("FILE_TEMP_NAMES")!=null&&!request.getParameter("FILE_TEMP_NAMES").equals("")) {
			map.put("FILE_TEMP_NAMES", FILE_TEMP_NAMES);
			boardDAO.modifyFiles(map);
		}else {
			//모든 파일이 수정되었다면, 모든 파일의 정보를 DB에서 수정함.
			boardDAO.modifyAllFiles(map);
		}
		
		//새로 변경되거나 추가된 파일들에 대한 정보를 DB에 추가함.
		if(fileNames.size()>0) {
			map.put("fileNames", fileNames);
			boardDAO.addFiles(map);
		}
		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "게시글 수정에 성공했습니다.");
		return result;
	}
	
	
	
	
	//============================================================================================
	//게시글 목록을 얻어오는 서비스 메소드, LIST는 게시글들의 정보가 담긴 해시맵들을 저장한 리스트, TOTAL은 페이징을 위해
	//조건에 맞는 게시글이 총 몇 개 존재하는지 그 개수를 나타내는 값임.
	//============================================================================================
	public HashMap listArticles(HashMap map) {
		List list = boardDAO.listArticles(map);
		int TOTAL = boardDAO.getArticlesCount(map);
		HashMap result = new HashMap();
		if(list != null) {
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "게시글 목록 불러오기에 성공했습니다.");
			result.put("LIST", list);
			result.put("TOTAL", TOTAL);
		}else {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "게시글 목록 불러오기에 실패했습니다.");
		}
		return result;
	}
	
	
	
	
	//============================================================================================
	//게시글의 상세정보를 읽고, 해당 게시글을 읽을 수 있도록 뷰를 리턴하는 메소드
	//============================================================================================
	public ModelAndView viewArticle(HashMap map) throws Exception{
		//해당 게시글에 대한 게시글 ID, 게시글 제목, 게시글 내용, 작성 날짜 등등을 얻음.
		HashMap articleVO = boardDAO.viewArticle(map);
		
		//게시글을 읽는데 문제가 없었다면, 해당 게시글에 첨부된 이미지 파일들에 대한 정보를 얻음.
		map.put("BOARD_FILE", map.get("BOARD")+"_FILE");
		List files = boardDAO.getFiles(map);
		
		//게시글을 읽는데 성공했다면, 해당 게시글의 조회수를 1 증가시킴.
		if(articleVO!=null) {
			boardDAO.updateViews(map);
		}
		
		//해당 게시글의 상세정보를 보여주는 뷰를 리턴하는데, 해당 게시글 객체와 게시판 종류
		//게시글에 첨부된 이미지 파일들의 정보가 담긴 리스트를 파라미터로 같이 전달함.
		ModelAndView mav = new ModelAndView("viewArticle");
		mav.addObject("articleVO", articleVO);
		mav.addObject("BOARD", map.get("BOARD"));
		mav.addObject("FILES",files);
		return mav;
	}
	
	
	
	
	//============================================================================================
	//게시글 삭제를 수행하는 메소드, 자신의 게시글만 삭제가 가능하며, 게시글 삭제에 성공하면 게시글에 첨부된 이미지 파일들의 정보와
	//실제 이미지 파일들을 DB 및 서버의 저장소에서 제거함.
	//============================================================================================
	public ModelAndView deleteArticle(HashMap map) throws Exception{
		
		//게시글 삭제에 앞서, 특정 게시글을 삭제하면 해당 게시글을 포함하여 자신의 하위 게시글들은 모조리 삭제 되어야함.
		//따라서 계층형 쿼리를 통해 먼저 연쇄적으로 삭제가 될 수 있도록 게시글 번호를 얻음.
		List list = boardDAO.selectDeletedArticlesID(map);
		map.put("BOARD_FILE", map.get("BOARD")+"_FILE");
		map.put("LIST", list);
		
		//게시글에 첨부된 이미지 파일을 먼저 삭제함, 외래키 제약조건때문에 게시글이 먼저 삭제되면 오류가 발생함.
		boardDAO.deleteFiles(map);
		
		//게시글을 삭제함.
		boardDAO.deleteArticle(map);
		
		//게시글과 이미지 파일 정보삭제에 성공하면 실제로 서버에 저장된 이미지 파일들을 제거함.
		//기존에 삭제되어야할 게시글들의 번호를 계층형 쿼리로 얻은 후, 리스트에 담았음.
		for(int i=0;i<list.size();i++) {
			File file = new File(REPOSITORY_PATH+"\\"+map.get("BOARD")+"\\"+list.get(i));
			try {
				if(file.exists()) {
					FileUtils.deleteDirectory(file);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//게시글 삭제에 성공했으면, 해당 게시판으로 이동하도록 설정함.
		ModelAndView mav = new ModelAndView("redirect:/board/boardForm.do");
		mav.addObject("BOARD", map.get("BOARD"));
		return mav;
	}
}
