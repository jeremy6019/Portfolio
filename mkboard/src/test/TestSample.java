package test;

import java.sql.DriverManager;

import org.junit.Test;

import VO.Board;
import dao.BoardDao;

public class TestSample {

	@Test
	public void testMethod() {
 /*
		BoardDao dao = BoardDao.sharedInstance();
		//게시글 데이터 생성 
		Board board = new Board();
		board.setTitle("게시글 제목");
		board.setContent("게시글 내용");
		board.setName("관리자");
		
		board.setTitle("손흥민과 류현진");
		board.setContent("얘네들이 내년 연봉 많이 받을까?");
		board.setName("제레미94");
		//게시글을 저장하는 메소드를 호출하고 결과 확인 
		int r = dao.boardInsert(board);
		System.out.println("결과:"+r);
*/
/*
		//전체 데이터를 가져오는 메소드 테스트	
		BoardDao dao = BoardDao.sharedInstance();	
		System.out.println(dao.boardList());
*/
		
		BoardDao dao = BoardDao.sharedInstance();	
	//	System.out.println(dao.cntUpdate(3));	
	//  System.out.println(dao.boardDetail(5));
	//	System.out.println(dao.boardDelete(7));  
		Board board = new Board();
		board.setNum(2);
		board.setTitle("제목변경");
		board.setContent("내용변경");
		System.out.println(dao.boardUpdate(board));
	
	
	}

	 
}
