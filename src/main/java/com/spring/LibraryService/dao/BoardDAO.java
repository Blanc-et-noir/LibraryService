package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("boardDAO")
public class BoardDAO {
	/*
	
	
	@Autowired
	private SqlSession sqlSession;
	
	
	//============================================================================================
	//게시판의 종류에 따라 새 게시글에 할당할 게시글 ID를 반환하는 메소드.
	//============================================================================================
	public int getNewArticleID(HashMap map) throws Exception{
		String BOARD = (String) map.get("BOARD");
		return sqlSession.selectOne("board.getNewArticleID"+BOARD);
	}
	
	
	
	//============================================================================================
	//게시글을 DB에 새로 추가하는 메소드.
	//============================================================================================
	public void addArticle(HashMap map) throws Exception{
		String BOARD = (String) map.get("BOARD");
		if(sqlSession.insert("board.addArticle", map)==0) {throw new Exception();}
	}
	
	
	
	//============================================================================================
	//게시글에 첨부된 이미지 파일들의 정보를 추가하는 메소드.
	//============================================================================================
	public void addFiles(HashMap map) throws Exception{
		sqlSession.insert("board.addFiles", map);
	}
	
	
	
	//============================================================================================
	//특정 게시판에서 조건에 맞는 게시글들의 수를 반환하는 메소드.
	//============================================================================================
	public int getArticlesCount(HashMap map) {
		return sqlSession.selectOne("board.getArticlesCount",map);
	}

	
	
	
	//============================================================================================
	//특정 게시글에 첨부된 이미지 파일들에 대한 정보를 리스트로 반환하는 메소드.
	//============================================================================================
	public List getFiles(HashMap map) {
		return sqlSession.selectList("board.getFiles",map);
	}
	
	
	
	
	//============================================================================================
	//게시글 목록을 얻어오는 메소드, 최대 10개의 게시글을 얻어오며, SECTION과 PAGE를 파라미터로 사용.
	//파라미터마다 각각 100, 10의 오프셋을 나타내는데, 오라클 11XE버전에서는 LIMIT OFFSET을 지원하지 않음.
	//============================================================================================
	public List listArticles(HashMap map) {
		return sqlSession.selectList("board.listArticles", map);
	}
	
	
	
	
	//============================================================================================
	//특정 게시글에 대한 게시글 제목, 내용등의 상세정보를 얻어오는 메소드.
	//============================================================================================
	public HashMap viewArticle(HashMap map) {
		return sqlSession.selectOne("board.viewArticle", map);
	}
	
	
	
	
	//============================================================================================
	//특정 게시글을 삭제할때, 해당 게시글과 하위 게시글들의 번호를 리스트로 반환하는 메소드.
	//============================================================================================
	public List selectDeletedArticlesID(HashMap map) {
		return sqlSession.selectList("board.selectDeletedArticlesID", map);
	}
	
	
	
	
	//============================================================================================
	//특정 게시글을 읽을때 해당 게시글의 조회수를 1증가시키는 메소드.
	//============================================================================================
	public void updateViews(HashMap map) throws Exception{
		if(sqlSession.update("board.updateViews", map)==0) {throw new Exception();}
	}
	
	
	
	
	//============================================================================================
	//특정 게시글을 삭제하는 메소드, 만약 삭제된 게시글의 수가 0이라면 자신의 게시글이 아니거나, 게시글 번호가 잘못된 것이므로 예외발생.
	//예외를 발생시켜 서비스단에서 트랜잭션을 적용하여 반영되지 않도록 설정함.
	//============================================================================================
	public void deleteArticle(HashMap map) throws Exception{
		if(sqlSession.delete("board.deleteArticle", map)==0) {throw new Exception();}
	}
	
	
	
	
	//============================================================================================
	//해당 게시글에 첨부된 이미지 파일들에 대한 정보를 제거하는 메소드.
	//============================================================================================
	public void deleteFiles(HashMap map){
		sqlSession.delete("board.deleteFiles", map);
	}
	
	
	
	
	//============================================================================================
	//해당 게시글에 첨부된 이미지가 모두 수정되었을때 이를 반영하는 메소드.
	//============================================================================================
	public void modifyAllFiles(HashMap map) throws Exception{
		sqlSession.delete("board.modifyAllFiles", map);
	}
	
	
	
	
	//============================================================================================
	//해당 게시글에 첨부된 이미지 일부만 수정되었을때 이를 반영하는 메소드.
	//============================================================================================
	public void modifyFiles(HashMap map) throws Exception{
		sqlSession.delete("board.modifyFiles", map);
	}
	
	
	
	
	//============================================================================================
	//해당 게시글의 제목, 내용등의 상세정보가 수정되었을때 이를 수정하는 메소드
	//============================================================================================
	public void modifyArticle(HashMap map) throws Exception{
		if(sqlSession.update("board.modifyArticle",map)==0) {throw new Exception();}
	}
	
	
	
	*/
}