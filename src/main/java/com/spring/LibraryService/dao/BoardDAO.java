package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("boardDAO")
public class BoardDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public int getNewArticleID(HashMap map) throws Exception{
		String BOARD = (String) map.get("BOARD");
		return sqlSession.selectOne("board.getNewArticleID"+BOARD);
	}
	public void addArticle(HashMap map) throws Exception{
		String BOARD = (String) map.get("BOARD");
		if(sqlSession.insert("board.addArticle", map)==0) {throw new Exception();}
	}
	public void addFiles(HashMap map) throws Exception{
		sqlSession.insert("board.addFiles", map);
	}
	
	
	
	public int getArticlesCount(HashMap map) {
		return sqlSession.selectOne("board.getArticlesCount",map);
	}

	public List getFiles(HashMap map) {
		return sqlSession.selectList("board.getFiles",map);
	}
	public List listArticles(HashMap map) {
		return sqlSession.selectList("board.listArticles", map);
	}
	
	public HashMap viewArticle(HashMap map) {
		return sqlSession.selectOne("board.viewArticle", map);
	}
	public List selectDeletedArticlesID(HashMap map) {
		return sqlSession.selectList("board.selectDeletedArticlesID", map);
	}
	
	
	
	
	public void updateViews(HashMap map) throws Exception{
		if(sqlSession.update("board.updateViews", map)==0) {throw new Exception();}
	}
	
	
	

	public void deleteArticle(HashMap map) throws Exception{
		if(sqlSession.delete("board.deleteArticle", map)==0) {throw new Exception();}
	}
	
	public void deleteFiles(HashMap map){
		sqlSession.delete("board.deleteFiles", map);
	}
	
	
	
	public void modifyAllFiles(HashMap map) throws Exception{
		sqlSession.delete("board.modifyAllFiles", map);
	}
	
	public void modifyFiles(HashMap map) throws Exception{
		sqlSession.delete("board.modifyFiles", map);
	}
	
	public void modifyArticle(HashMap map) throws Exception{
		if(sqlSession.update("board.modifyArticle",map)==0) {throw new Exception();}
	}
}