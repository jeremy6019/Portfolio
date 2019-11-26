package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import repository.MemberDAO;
import vo.Member;

public class MemberServiceImpl implements MemberService {
	
	//DAO클래스의 참조형 변수를 선언 
	private MemberDAO memberDao;
	
	//싱글톤 패턴을 처리하기 위한 코드 
	private MemberServiceImpl() {
		//DAO객체 생성 
		memberDao = MemberDAO.sharedInstance();
	}
		
	private static MemberService memberService;
		
	public static MemberService sharedInstance() {
		if(memberService == null) {
			memberService = new MemberServiceImpl();
		}
		return memberService;
	}
    
	
	@Override
	public boolean emailCheck(HttpServletRequest request) {
	 // System.out.println("이메일 중복체크 서비스 ");
		boolean result = false; 
		//파라미터 읽기 
		String email = request.getParameter("email");
	//	System.out.println("email:" + email);
		
		//email중복검사를 수행해주는 DAO메소드를 호출 
	    String r = memberDao.emailCheck(email);
	    //데이터가 리턴되면 email 있는 경우이고 null이 리턴되면 없는 email 
	    if(r == null ) {
	    	result = true;
	    }
		
		return result;
	}


	@Override
	public JSONObject nicknameCheck(HttpServletRequest request) {
	//	System.out.println("닉네임 중복체크 서비스 ");
		JSONObject obj = new JSONObject();
	    
	    //파라미터 읽기 
  		String nickname = request.getParameter("nickname");
  	//	System.out.println("nickname:" + nickname);
  		//nickname 중복검사를 수행해주는 DAO메소드를 호출 
  	    String result = memberDao.nicknameCheck(nickname);
  	    //데이터가 리턴되면 nickname 있는 경우이고 null이 리턴되면 없는 email 
  	    if(result == null ) {
  	    	obj.put("result", "true");
  	    }else {
  	    	obj.put("result", "false");
  	    }
	    
		return obj;
	}


	@Override
	public JSONObject phoneCheck(HttpServletRequest request) {
	//	System.out.println("전화번호 체크");
		JSONObject obj = new JSONObject();
	    
	    //파라미터 읽기 
  		String phone = request.getParameter("phone");
     // System.out.println("phone:" + phone);
  		//nickname 중복검사를 수행해주는 DAO메소드를 호출 
  	    String result = memberDao.phoneCheck(phone);
  	    //데이터가 리턴되면 phone 있는 경우이고 null이 리턴되면 없는 phone
  	    if(result == null ) {
  	    	obj.put("result", "true");
  	    }else {
  	    	obj.put("result", "false");
  	    }
	    
		return obj;
	}


	@Override
	public boolean join(HttpServletRequest request) {
	 //	System.out.println("회원가입 체크");
		boolean result = false;
		try {
			//파일이 업로드 될 디렉토리의 절대 경로 만들기 
			String uploadPath = 
					request.getServletContext().getRealPath("/images");
			//파일 업로드 
			MultipartRequest mRequest = 
					new MultipartRequest(request, uploadPath, 10*1024*1024
							,"utf-8", new DefaultFileRenamePolicy());
			//데이터베이스에 저장하기 위해서 파라미터 읽어오기 - MultipartRequest 로 읽기 
			String email = mRequest.getParameter("email");
			String password = mRequest.getParameter("password");
			String name = mRequest.getParameter("name");
			String nickname = mRequest.getParameter("nickname");
			String phone = mRequest.getParameter("phone");
			
			//year, month, day 값을 가지고 java.sql.Date만들기 
			String year = mRequest.getParameter("year");
			String month = mRequest.getParameter("month");
			String day = mRequest.getParameter("day");
			
			Calendar cal = new GregorianCalendar(
					Integer.parseInt(year),
					Integer.parseInt(month)-1,
					Integer.parseInt(day));
			Date birthday = new Date(cal.getTimeInMillis()); 
			
			//파일 경로 만들기 
			//업로드 되기 위해서 변경된 파일이름 가져오기 
			String image = "default.png"; 
			//업로드된 파일이 있다면 
			//전체 파일이름을 가져온 후 첫번째 데이터를 가져옵니다. 
		    Enumeration <String> files = mRequest.getFileNames(); 
		    String imsi = files.nextElement();

		    //첫번째 데이터가 있다면 그 이름으로 변경된 이름을 찾아옵니다. 
		    if(imsi != null && imsi.length() > 0) {
		    	image = mRequest.getFilesystemName(imsi);
		    	//선택한 이미지가 없다면 default.png를 대입 
		    	if(image == null) {
		    		image = "default.png";
		    	}
		    }
		    
		    //DAO의 매개변수 만들기 
		    Member member = new Member();
		    member.setEmail(email);
		    //비밀번호 암호화 해서 설정 
		    member.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
		    member.setName(name);
		    member.setNickname(nickname);
		    member.setImage(image);
		    member.setBirthday(birthday);
		    member.setPhone(phone);
		    
		 // System.out.println("DAO파라미터: "+ member);
		    //DAO 메소드 호출 
		    int r = memberDao.join(member);
		    
		    //성공한 경우 result에 true 대입 
		    if(r > 0) {
		    	result = true;
		    }			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return result;
	}

	@Override
	public boolean login(HttpServletRequest request) {
		boolean result = false;
		try {
			//파라미터 읽기 
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			//email을 가지고 회원정보를 가져옵니다. 
			Member member = memberDao.login(email);
			//비밀번호 확인 
			if(member != null && BCrypt.checkpw(
					password, member.getPassword())) {
				result = true;
				//로그인 성공한 경우에는 회원정보를 세션에 저장하고 
				//필요하다면 데이터베이스에도 기록을 해둡니다. 
				//password는 세션에 저장하지 않을 때 설정 
				//member.setPassword(null);
				request.getSession().setAttribute("member", member);
				request.getSession().setAttribute(
						"msg", null);
			}else {
				result = false;
				request.getSession().setAttribute("member", null);
				request.getSession().setAttribute(
						"msg", "없는 이메일이거나 잘못된 비밀번호입니다.");
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}		
		return result;
	}


	@Override
	public boolean getHani(HttpServletRequest request) {
		boolean result = false;
		try {
			//java로 웹에서 문자열 가져오기 - 모든 OS에서 동일하게 사용 
			String addr = "http://www.hani.co.kr/rss/";
			//위의 URL에 한글이 있으면 한글 부분만 URLEncoder.encode메소드를 
			//이용해서 인코딩을 해야합니다. 
			
			//URL을 생성 
			URL url = new URL(addr);
			
			//URL에 연결하는 객체 생성 
			HttpURLConnection con = 
					(HttpURLConnection)url.openConnection();
			
			//옵션을 설정 
			con.setConnectTimeout(30000);
			con.setUseCaches(false);
			
			//데이터를 읽어올 스트림 생성 
			BufferedReader br = 
					new BufferedReader(
							new InputStreamReader(con.getInputStream()));
			//데이터 읽기 
			StringBuilder sb = new StringBuilder();
			while(true) {
				String line = br.readLine();
				if(line == null) {
					break;
				}
				sb.append(line + "\n");
			}
			//읽은 데이터를 문자열로 변환 
			String data = sb.toString();
			//사용한 객체 정리 
			br.close();
			con.disconnect();
			
			//request객체에 데이터를 저장 
			request.setAttribute("result", data);
			result = true;
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return result;
	}


	@Override
	public void push(HttpServletRequest request, HttpServletResponse response) {
		//출력을 위한 객체 생성 
		PrintWriter pw =null;
		try {
			//웹 푸시 출력 형식 설정 
			response.setContentType("text/event-stream");		
			//인코딩 방식 설정 
			response.setCharacterEncoding("utf-8");
			//출력 객체 생성 
			pw = response.getWriter();
			//출력 내용 만들기 
		    Random r = new Random();
		  //int data = r.nextInt(10);
		    String data = "https://mimgnews.pstatic.net/image/109/2017/08/21/0003601978_001_20170821095830626.jpg?type=w540";
		    //데이터 출력 
		    pw.write("data:"+ data + "\n\n" );
		    //일정 시간마다 전송 
		    Thread.sleep(10000);
		    
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		pw.close();
	}
	
	
	
}
