package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import vo.Member;

public class MemberDAO {
    //데이터베이스 연동에 필요한 변수 
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//클래스가 처음 호출될 때 수행하는 코드 
		static {
			try{
				//드라이버 클래스 로드 
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
		    }
		}
		
		//싱글톤 패턴을 처리하기 위한 코드 
		private MemberDAO() {
			try {				
				//데이터베이스 연결  
				Context init = new InitialContext();
				DataSource ds = (DataSource)init.lookup("java:comp/env/DBConn");
				con = ds.getConnection();
				
				//트랜잭션 관련 메소드를 직접 호출하기 위해서 autoCommit해제 			
				con.setAutoCommit(false);
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		private static MemberDAO memberDao;
		
		public static MemberDAO sharedInstance() {
			if(memberDao == null) {
				memberDao = new MemberDAO();
			}
			return memberDao;
		}
		
		//email 중복검사를 위한 메소드 
		public String emailCheck(String email) {
		//	System.out.println("email:" + email);
			String result = null;
			try {
				//실행할 SQL생성 
				pstmt = con.prepareStatement(
						"select email from member where email=?"
						);
				//필요한 매개변수를 바인딩 
				pstmt.setString(1, email);
				//SQL 실행 -select 
				rs = pstmt.executeQuery();
				//email은 중복되지 않기 때문에 데이터가 2개 이상 리턴될 수 없습니다. 
				if(rs.next()) {
					result = rs.getString("email");
				}
				//사용한 자원 정리 
				rs.close();
				pstmt.close();				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		//	System.out.println(result);
			return result;
		}

		//nickname 중복검사를 위한 메소드 
		public String nicknameCheck(String nickname) {
		//	System.out.println("nickname:" + nickname);
			String result = null;
			
			try {
				pstmt = con.prepareStatement(
						"select nickname from member where nickname=?");
				pstmt.setString(1,  nickname);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = rs.getString("nickname");
				}
				
				rs.close();
				pstmt.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		//	System.out.println(result);
			return result;
		}
		
		//phone 중복검사를 위한 메소드 
		public String phoneCheck(String phone) {
		//	System.out.println("phone:" + phone);
			String result = null;
			
			try {
				pstmt = con.prepareStatement(
						"select phone from member where phone=?");
				pstmt.setString(1,  phone);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = rs.getString("phone");
				}
				
				rs.close();
				pstmt.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		//	System.out.println(result);
			return result;
		}
		
		//회원가입을 처리해주는 메소드 
		public int join(Member member) {
		//	System.out.println("DAO:"+ member);
			int result = -1;
			try {
				pstmt = con.prepareStatement(
						"insert into member("
						+ "email, password, name, nickname, image, birthday, phone)"
						+ "values(?,?,?,?,?,?,?)");
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setString(4, member.getNickname());
				pstmt.setString(5, member.getImage());
				pstmt.setDate(6, member.getBirthday());
				pstmt.setString(7, member.getPhone());
				
				result = pstmt.executeUpdate();
				
			//	System.out.println("result:" + result);				
				pstmt.close();
				//Connection의 AutoCommit속성을 false로 설정했으면 작업이 끝나고 
				//commit을 해주어야 합니다. 
				//이 경우에는 예외가 발생하면 rollback을 해주어야 합니다. 
				con.commit();
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			return result;
		}
		//로그인 처리 메소드 
		//없는 이메일이면 null이 리턴되고 
		//존재하는 이메일이면 이메일에 해당하는 정보를 Member 객체에 담아서 리턴 
		public Member login(String email) {
			Member member = null;
			try {
				pstmt = con.prepareStatement(
						"select email, password, nickname, image from member where email = ?");
				
				//?에 데이터 바인디 
				pstmt.setString(1,  email);
				
				//SQL 실행  
				rs = pstmt.executeQuery(); 
				
				// 데이터 읽기 
				if(rs.next()) {
					member = new Member();
					member.setEmail(rs.getString("email"));
					member.setPassword(rs.getString("password"));
					member.setNickname(rs.getString("nickname"));
					member.setImage(rs.getString("image"));
				}
				rs.close();
				pstmt.close();
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			return member;
		}
		
		
		
		
}
