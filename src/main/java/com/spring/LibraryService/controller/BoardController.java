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

//�Խ��ǰ� ���õ� ��û�� ó���� ��Ʈ�ѷ�
@Controller("board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	//����, ����, ���ǰԽ��ǵ��� ���̽� ���丮
	private static String REPOSITORY_PATH = "D:\\LibraryService\\board\\";
	
	
	//============================================================================================
	//Ŭ���̾�Ʈ�� ��û�� �Խ��� ������ ���� ���� �ٸ� �Ķ���͸� mav ��ü�� �Ҵ���. 
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
			//�Խ����� ������ �߸��Ǿ�����, ����ȭ������ �̵�
			return new ModelAndView("redirect:/customer/mainForm.do");
		}
	}
	
	
	//============================================================================================
	//�Խñ� �ۼ� ȭ���û�� ó���ϴ� �޼ҵ�, �Խ��� ������ �߸��Ǿ������� ����ȭ������ �̵���.
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
	//�Խñ� �ۼ� ��û�� ó���ϴ� �޼ҵ�
	//============================================================================================
	@RequestMapping(value="/board/addArticle.do")
	@ResponseBody
	public HashMap LOGONMAP_addArticle(MultipartHttpServletRequest request, HttpServletResponse response){
		HashMap result = new HashMap();
		try {
			String BOARD = request.getParameter("BOARD");
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
				result.put("FLAG", "FALSE");
				result.put("FALSE", "�Խñ� �ۼ��� �����߽��ϴ�.");
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
				
				//������ SALT���� SHA512 �˰����� �̿��� ������ �ӽ� �̸��� ����
				//���� ��ġ�� �޶�, �̸��� ���� �̹��������� ������ �ֱ� ������ �̸� �ߺ����� ���� �⺻Ű ���� ������ ȸ����
				String tempFileName = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 32);
				
				//������ Ȯ���ڸ� ���, ���� �߱޹��� �ӽ� �����̸��� Ȯ���ڸ� �����̰�, �Ʒ��� ��ο� �̹����� ������
				String fileExtension = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
				File dest = new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID+"\\"+tempFileName+fileExtension);
				
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
				param.put("ARTICLE_ID", ARTICLE_ID);
				param.put("fileNames", fileNames);
				param.put("BOARD_FILE", BOARD+"_file");
				boardService.addFiles(param);
			}			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "�Խñ� �ۼ��� �����߽��ϴ�.");
			return result;
		}
	}
	
	
	
	
	//============================================================================================
	//�Խñ��� �����ϴ� �޼ҵ�, �ڽ��� �Խñ��̾�߸� ������ �����ϴ�.
	//============================================================================================
	@RequestMapping(value="/board/modifyArticle.do")
	@ResponseBody
	public HashMap LOGONMAP_modifyArticle(MultipartHttpServletRequest request, HttpServletResponse response) {
		HashMap result = new HashMap();
		try {
			String BOARD = request.getParameter("BOARD");
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
				result.put("FLAG", "FALSE");
				result.put("CONTENT", "�Խñ� ������ �����߽��ϴ�.");
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
			result.put("CONTENT", "�Խñ� ������ �����߽��ϴ�.");
			return result;
		}		
	}
	
	
	
	
	//============================================================================================
	//�Խñ� ����� ��� �޼ҵ�, �񵿱������ �����ϹǷ� ȭ�� ���ΰ�ħ�� �߻����� ����. ���Ǽ��� �����ϱ� ����.
	//�Խñ��� ��¥������ �������� ���������� (SECTION-1)*100+(PAGE-1)*10+1 ��° �Խñۺ��� 10���� �Խñ��� ����.
	//============================================================================================
	@RequestMapping(value="/board/listArticles.do")
	@ResponseBody
	public HashMap listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String BOARD = request.getParameter("BOARD");
		HashMap<String,String> result = new HashMap<String,String>();
		if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))) {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "�Խñ� ��� �ҷ����⿡ �����߽��ϴ�.");
			return result;
		}
		HashMap map = new HashMap();
		map.put("SECTION", request.getParameter("SECTION"));
		map.put("PAGE", request.getParameter("PAGE"));
		map.put("BOARD", BOARD);
		
		//FLAG�� ����� ID, �Խñ� ����, �Խñ� ������ � ���� �������� �Խñ��� �˻��� �������� ��Ÿ��.
		map.put("FLAG", request.getParameter("FLAG"));
		map.put("SEARCH", request.getParameter("SEARCH"));
		return boardService.listArticles(map);
	}

	
	

	//============================================================================================
	//Ư�� �Խñ��� �а��� �� �� �����ϴ� �޼ҵ�
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
			
			//�Խñ� �б⿡ �����ϸ� viewArticle �並 �����ϸ�, JSP�� JSTL ����� �̿��Ͽ�
			//�������� �Խñ� ����� ����, ÷�ε� �̹����鿡 ���� ó���� �����Ѵ�.
			return boardService.viewArticle(map);
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
			
			//�Խñ� ������ �����ϸ� �Խñ� ������� �̵��Ѵ�.
			return boardService.deleteArticle(map);
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
			String BOARD = request.getParameter("BOARD");
			String FILE_TEMP_NAME = request.getParameter("FILE_TEMP_NAME");
			int ARTICLE_ID = Integer.parseInt(request.getParameter("ARTICLE_ID"));
			if(BOARD==null||(!BOARD.equals("free_board")&&!BOARD.equals("qna_board")&&!BOARD.equals("info_board"))||FILE_TEMP_NAME==null) {
				return;
			}
			
			//�ش� �Խñ��� �̹��������� ������ �����ؾ߸� �ٿ�ε尡 ���������� �����.
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
