package test;

import org.junit.Test;


import repository.MemberDAO;

public class TestSample {

	@Test
	public void testMethod() {
 
		MemberDAO dao = MemberDAO.sharedInstance();
        System.out.println(dao.emailCheck("jeremy6019@daum.net"));
	 // System.out.println(dao.phoneCheck("01050386578"));

		
		
	
	
	
	}
}
