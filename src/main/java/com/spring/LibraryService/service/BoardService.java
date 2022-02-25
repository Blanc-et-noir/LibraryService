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
	//�Խñ� �ۼ��� �����ϴ� ���� �޼ҵ�
	//============================================================================================
	public HashMap addArticle(HashMap map) throws Exception{
		//�Խñ� ������ ���� �� �Խñ��� ���� ���ο� �Խñ� ID�� ����.
		//����ŬDBMS 11XE������ �⺻ ������������ �����Ͽ� �Ʒ��� ���� ó����.
		map.put("ARTICLE_ID",boardDAO.getNewArticleID(map));
		
		//�Խñ� �ۼ��� �����ϸ� ���������� ����, ���нÿ� ���ܸ� �߻���Ŵ.
		boardDAO.addArticle(map);
		HashMap result = new HashMap();
		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "�Խñ� �ۼ��� �����߽��ϴ�.");
		result.put("ARTICLE_ID", map.get("ARTICLE_ID"));
		return result;
	}
	
	
	
	//============================================================================================
	//�Խñۿ� ÷�ε� ���ϵ鿡 ���� ������ �߰��ϴ� �޼ҵ�.
	//============================================================================================
	public void addFiles(HashMap map) throws Exception{
		boardDAO.addFiles(map);
	}
	
	
	
	
	//============================================================================================
	//�Խñ� ������ �����ϴ� �޼ҵ�. �ڽ��� �Խñ۸� ������ �� �ִ�.
	//============================================================================================
	public HashMap modifyArticle(MultipartHttpServletRequest request, HashMap map) throws Exception{
		HashMap result = new HashMap();
		String BOARD = (String)map.get("BOARD");
		int ARTICLE_ID = (Integer)map.get("ARTICLE_ID");
		
		//���� �ڽ��� �Խñ۰� �����Ͽ� �Խñ� ����, ������ ������.
		boardDAO.modifyArticle(map);
		
		//�Խñ� ����� ��������� �����ϸ� �ش� �Խñۿ� ÷�ε� ���ϵ�� �����Ͽ� ó���� �����ؾ���.
		//1. ���� �̹����� �������� ���ܵδ� ���.
		//2. ���� �̹����� �ٸ� �̹����� ������ ���.
		//3. ���� �̹����� ������ ���.
		//4. ���� �߰��� �̹����� ���.
		//�� 4������ �̹����� �з��� �� �ִµ�, ���� 1���� ��츸 FILE_TEMP_NAMES �Ķ���Ϳ� �ӽ� �̸��� ����ִ�.
		//�׿��� �̹������� �ӽ� �̸��� ���µ�, �̷��� �ӽ� �̸��� ���� 2,3,4 �̹��� ���ϵ��� ���� ������ ����� ������
		//���� �����ϰ� �׿� �°� �ٽ� �߰��ؾ��Ѵ�.
		//1�� ���� �״�� �����ϸ� �ȴ�.
		String[] FILE_TEMP_NAMES = request.getParameter("FILE_TEMP_NAMES").split("\\*");
		Iterator<String> iterator = request.getFileNames();
		List<HashMap> fileNames = new ArrayList<HashMap>();
		
		//�������� ���� �״���� �̹����� �����ϴ� ��쿡�� �Ʒ��� ���� ��� TEMP ���丮�� �ű��Ŀ�
		//�ռ� ����� 2, 3, 4�� �̹������� ���� ����ҿ��� ������ ��, �ٽ� TEMP ���丮���� ���� ���丮�� �ű��.
		if(request.getParameter("FILE_TEMP_NAMES")!=null&&!request.getParameter("FILE_TEMP_NAMES").equals("")) {
			//�ӽ÷� 1�� �̹������� TEMP ���丮�� �̵���Ŵ.
			for(int i=0;i<FILE_TEMP_NAMES.length;i++) {
				File file = new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID+"\\"+FILE_TEMP_NAMES[i]);
				if(file.exists()) {
					FileUtils.moveFileToDirectory(file, new File(REPOSITORY_PATH+"\\"+BOARD+"\\temp"),true);
				}
			}
			
			//�ش� �Խñ� ���丮�� ������ ������.
			FileUtils.deleteDirectory(new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID));
			
			//TEMP���� �ش� �Խñ� ���丮�� �ٽ� �̹��� ���ϵ��� �̵���Ŵ. 
			for(int i=0;i<FILE_TEMP_NAMES.length;i++) {
				File file = new File(REPOSITORY_PATH+"\\"+BOARD+"\\temp\\"+FILE_TEMP_NAMES[i]);
				if(file.exists()) {
					FileUtils.moveFileToDirectory(file, new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID),true);
				}
			}
		}else {
			//�������� ���� �̹��������� ���� ���, ���� ����� �̹����̹Ƿ� ���丮 ��ü�� �ϴ� ������.
			FileUtils.deleteDirectory(new File(REPOSITORY_PATH+"\\"+BOARD+"\\"+ARTICLE_ID));
		}
		
		iterator = request.getFileNames();

		//���� ����ǰų� �߰��� �̹����鿡 ���ؼ�, �Խñ� �ۼ����� ���������� �ӽ� �̸��� �Ҵ��ϰ�
		//������ �ش� �̸��� Ȯ���ڸ� ���� ������ ���·� ������.
		while(iterator.hasNext()) {
			String fileName = iterator.next();
			MultipartFile mFile = request.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			
			//�Խñۿ� ÷�ε� �̹��������� �̸� �ߺ��� ȸ���ϱ�����, SHA512�� �̿��Ͽ� ������ �̸��� �Ҵ���.
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

		//������������ ������ �����Ѵٸ� �ش� ������ ������ ��� ������ ������ DB���� ������.
		if(request.getParameter("FILE_TEMP_NAMES")!=null&&!request.getParameter("FILE_TEMP_NAMES").equals("")) {
			map.put("FILE_TEMP_NAMES", FILE_TEMP_NAMES);
			boardDAO.modifyFiles(map);
		}else {
			//��� ������ �����Ǿ��ٸ�, ��� ������ ������ DB���� ������.
			boardDAO.modifyAllFiles(map);
		}
		
		//���� ����ǰų� �߰��� ���ϵ鿡 ���� ������ DB�� �߰���.
		if(fileNames.size()>0) {
			map.put("fileNames", fileNames);
			boardDAO.addFiles(map);
		}
		
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "�Խñ� ������ �����߽��ϴ�.");
		return result;
	}
	
	
	
	
	//============================================================================================
	//�Խñ� ����� ������ ���� �޼ҵ�, LIST�� �Խñ۵��� ������ ��� �ؽøʵ��� ������ ����Ʈ, TOTAL�� ����¡�� ����
	//���ǿ� �´� �Խñ��� �� �� �� �����ϴ��� �� ������ ��Ÿ���� ����.
	//============================================================================================
	public HashMap listArticles(HashMap map) {
		List list = boardDAO.listArticles(map);
		int TOTAL = boardDAO.getArticlesCount(map);
		HashMap result = new HashMap();
		if(list != null) {
			result.put("FLAG", "TRUE");
			result.put("CONTENT", "�Խñ� ��� �ҷ����⿡ �����߽��ϴ�.");
			result.put("LIST", list);
			result.put("TOTAL", TOTAL);
		}else {
			result.put("FLAG", "FALSE");
			result.put("CONTENT", "�Խñ� ��� �ҷ����⿡ �����߽��ϴ�.");
		}
		return result;
	}
	
	
	
	
	//============================================================================================
	//�Խñ��� �������� �а�, �ش� �Խñ��� ���� �� �ֵ��� �並 �����ϴ� �޼ҵ�
	//============================================================================================
	public ModelAndView viewArticle(HashMap map) throws Exception{
		//�ش� �Խñۿ� ���� �Խñ� ID, �Խñ� ����, �Խñ� ����, �ۼ� ��¥ ����� ����.
		HashMap articleVO = boardDAO.viewArticle(map);
		
		//�Խñ��� �дµ� ������ �����ٸ�, �ش� �Խñۿ� ÷�ε� �̹��� ���ϵ鿡 ���� ������ ����.
		map.put("BOARD_FILE", map.get("BOARD")+"_FILE");
		List files = boardDAO.getFiles(map);
		
		//�Խñ��� �дµ� �����ߴٸ�, �ش� �Խñ��� ��ȸ���� 1 ������Ŵ.
		if(articleVO!=null) {
			boardDAO.updateViews(map);
		}
		
		//�ش� �Խñ��� �������� �����ִ� �並 �����ϴµ�, �ش� �Խñ� ��ü�� �Խ��� ����
		//�Խñۿ� ÷�ε� �̹��� ���ϵ��� ������ ��� ����Ʈ�� �Ķ���ͷ� ���� ������.
		ModelAndView mav = new ModelAndView("viewArticle");
		mav.addObject("articleVO", articleVO);
		mav.addObject("BOARD", map.get("BOARD"));
		mav.addObject("FILES",files);
		return mav;
	}
	
	
	
	
	//============================================================================================
	//�Խñ� ������ �����ϴ� �޼ҵ�, �ڽ��� �Խñ۸� ������ �����ϸ�, �Խñ� ������ �����ϸ� �Խñۿ� ÷�ε� �̹��� ���ϵ��� ������
	//���� �̹��� ���ϵ��� DB �� ������ ����ҿ��� ������.
	//============================================================================================
	public ModelAndView deleteArticle(HashMap map) throws Exception{
		
		//�Խñ� ������ �ռ�, Ư�� �Խñ��� �����ϸ� �ش� �Խñ��� �����Ͽ� �ڽ��� ���� �Խñ۵��� ������ ���� �Ǿ����.
		//���� ������ ������ ���� ���� ���������� ������ �� �� �ֵ��� �Խñ� ��ȣ�� ����.
		List list = boardDAO.selectDeletedArticlesID(map);
		map.put("BOARD_FILE", map.get("BOARD")+"_FILE");
		map.put("LIST", list);
		
		//�Խñۿ� ÷�ε� �̹��� ������ ���� ������, �ܷ�Ű �������Ƕ����� �Խñ��� ���� �����Ǹ� ������ �߻���.
		boardDAO.deleteFiles(map);
		
		//�Խñ��� ������.
		boardDAO.deleteArticle(map);
		
		//�Խñ۰� �̹��� ���� ���������� �����ϸ� ������ ������ ����� �̹��� ���ϵ��� ������.
		//������ �����Ǿ���� �Խñ۵��� ��ȣ�� ������ ������ ���� ��, ����Ʈ�� �����.
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
		
		//�Խñ� ������ ����������, �ش� �Խ������� �̵��ϵ��� ������.
		ModelAndView mav = new ModelAndView("redirect:/board/boardForm.do");
		mav.addObject("BOARD", map.get("BOARD"));
		return mav;
	}
}
