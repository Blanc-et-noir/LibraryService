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
	
	public HashMap addArticle(HashMap map) throws Exception{
		map.put("ARTICLE_ID",boardDAO.getNewArticleID(map));
		boardDAO.addArticle(map);
		HashMap result = new HashMap();
		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "게시글 작성에 성공했습니다.");
		result.put("ARTICLE_ID", map.get("ARTICLE_ID"));
		return result;
	}
	
	public void addFiles(HashMap map) throws Exception{
		boardDAO.addFiles(map);
	}
	
	public HashMap modifyArticle(MultipartHttpServletRequest request, HashMap map) throws Exception{
		HashMap result = new HashMap();
		String BOARD = (String)map.get("BOARD");
		int ARTICLE_ID = (Integer)map.get("ARTICLE_ID");
		boardDAO.modifyArticle(map);
		
		String[] FILE_TEMP_NAMES = request.getParameter("FILE_TEMP_NAMES").split("\\*");
		Iterator<String> iterator = request.getFileNames();
		List<HashMap> fileNames = new ArrayList<HashMap>();
		
		if(request.getParameter("FILE_TEMP_NAMES")!=null&&!request.getParameter("FILE_TEMP_NAMES").equals("")) {
			for(int i=0;i<FILE_TEMP_NAMES.length;i++) {
				File file = new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID+"\\"+FILE_TEMP_NAMES[i]);
				if(file.exists()) {
					FileUtils.moveFileToDirectory(file, new File(REPOSITORY_PATH+"\\"+BOARD+"\\temp"),true);
				}
			}
			
			FileUtils.deleteDirectory(new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID));
			
			for(int i=0;i<FILE_TEMP_NAMES.length;i++) {
				File file = new File(REPOSITORY_PATH+"\\"+BOARD+"\\temp\\"+FILE_TEMP_NAMES[i]);
				if(file.exists()) {
					FileUtils.moveFileToDirectory(file, new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID),true);
				}
			}
		}else {
			FileUtils.deleteDirectory(new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID));
		}
		
		iterator = request.getFileNames();

		while(iterator.hasNext()) {
			String fileName = iterator.next();
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
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

		if(request.getParameter("FILE_TEMP_NAMES")!=null&&!request.getParameter("FILE_TEMP_NAMES").equals("")) {
			map.put("FILE_TEMP_NAMES", FILE_TEMP_NAMES);
			boardDAO.modifyFiles(map);
		}else {
			boardDAO.modifyAllFiles(map);
		}
		
		if(fileNames.size()>0) {
			map.put("fileNames", fileNames);
			boardDAO.addFiles(map);
		}
		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "게시글 수정에 성공했습니다.");
		return result;
	}
	
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

	public ModelAndView viewArticle(HashMap map) throws Exception{
		HashMap articleVO = boardDAO.viewArticle(map);
		map.put("BOARD_FILE", map.get("BOARD")+"_FILE");
		List files = boardDAO.getFiles(map);
		if(articleVO!=null) {
			boardDAO.updateViews(map);
		}
		ModelAndView mav = new ModelAndView("viewArticle");
		mav.addObject("articleVO", articleVO);
		mav.addObject("BOARD", map.get("BOARD"));
		mav.addObject("FILES",files);
		return mav;
	}
	
	public ModelAndView deleteArticle(HashMap map) throws Exception{
		List list = boardDAO.selectDeletedArticlesID(map);
		map.put("BOARD_FILE", map.get("BOARD")+"_FILE");
		map.put("LIST", list);
		
		boardDAO.deleteFiles(map);
		boardDAO.deleteArticle(map);
		
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
		ModelAndView mav = new ModelAndView("redirect:/board/boardForm.do");
		mav.addObject("BOARD", map.get("BOARD"));
		return mav;
	}
}
