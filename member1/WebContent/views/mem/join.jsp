<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 반복문을 사용하기 위해서 JSTL태그 라이브러리 추가  -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입</title>
</head>
<body>
	<h3>회원 가입</h3>
	<form method="post" enctype="multipart/form-data"id="joinform">
		<table align='center' width="50%" height="400" border="1">
			<!-- image 선택영역  -->
			<tr>
				<td rowspan="7" align="center">
					<br/>
					<img src="img" id="img" width="200" height="250"
					border="1" />
					<br/>
					<input type="file" id="image" name="image"
					accept="image/*" />
				</td>
		   </tr>
		   <tr>
		   		<td align='center'>이메일</td>
		   		<td><input type="email" name="email" id="email"
		   		required="required" size="30" maxlength="40" 
		   		placeholder="필수입력입니다."/>
		   		<div id="emaildisp"></div>
		   		</td>
		   </tr>
		   <tr>
		   		<td align='center'>비밀번호</td>
		   		<td><input type="password" name="password" id="password"
		   		required="required" size="30" maxlength="40"
		   		placeholder="필수입력입니다." />
		   		<div id="passworddisp"></div>
		   		</td>
		   </tr>
		   <tr>
		   		<td align='center'>이름</td>
		   		<td><input type="name" name="name" id="name"
		   		required="required" size="30" maxlength="40" 
		   		placeholder="필수입력입니다."/>
		   		<div id="namedisp"></div>
		   		</td>
		   </tr>	
		    <tr>
		   		<td align='center'>별명</td>
		   		<td><input type="nickname" name="nickname" id="nickname"
		   		required="required" size="30" maxlength="40" 
		   		placeholder="별명을 안넣으려면 회원가입 하지마세요!!"/>
		   		<div id="nicknamedisp"></div>
		   		</td>
		   </tr>
		     <tr>
		   		<td align='center'>생년월일</td>
		   		<td>
		   			<select name="year">
		   				<c:forEach var="year" begin="1920" end="2019">
		   					<option value="${year}">${year}</option>
		   				</c:forEach>
		   			</select>년 
		   			<select name="month">
		   				<c:forEach var="month" begin="1" end="12">
		   					<option value="${13-month}">${13-month}</option>
		   				</c:forEach>
		   			</select>월  
		   			<select name="day">
		   				<c:forEach var="day" begin="1" end="31">
		   					<option value="${day}">${day}</option>
		   				</c:forEach>
		   			</select>일   
		   		</td>
		    </tr>	
		    <tr>
		   		<td align='center'>전화번호</td>
		   		<td><input type="phone" name="phone" id="phone"
		   		required="required" size="30" maxlength="40" 
		   		placeholder="필수입력입니다."/>	
		   		<div id="phonedisp"></div>	   		
		   		</td>
		   </tr>
		   
		   <tr>
		   	<td colspan = "3" align="center">
		   		<input type="submit" value="회원가입" />
		   		&nbsp;&nbsp;
		   		<input type="button" value="메인으로" id="mainbtn" />
		    </td>
		</table>
		
	</form>
</body>
<script>
	//메인으로 버튼을 눌렀을 때 메인페이지로 이동하기 
	document.getElementById("mainbtn").addEventListener('click', function(e){
		location.href = "../";
	});
	
	//파일의 선택이 변경된 경우 선택한 파일을 img태그에 출력하기 
	document.getElementById("image").addEventListener('change', function(e){
		//선택한 파일이 있는지 확인 
		if(this.files && this.files[0]){
			//선택한 파일 이름 가져오기 
			filename = this.files[0].name;
			//파일 읽기 객체 생성 
			var reader = new FileReader();
			//파일읽기 
			reader.readAsDataURL(this.files[0]);
			//파일을 전부 읽으면 img태그에 출력 
			reader.addEventListener('load', function(e){
				var event = e || window.event; 
				document.getElementById('img').src = event.target.result;
			});
		};
	});
	
	//email입력 영역과 이메일 관련 메시지 출력 영역을 찾아오기 
	var email = document.getElementById("email");
	var emaildisp = document.getElementById("emaildisp");
	
	//이메일 체크 여부를 저장하기 위한 변수 
	var emailcheck = false; 
	
	//email입력 란에서 포커스가 사라질 때 
	email.addEventListener("focusout",function(e){
		//입력한 값 가져오기 
		var val = email.value.trim();
	//	alert(val);
		if(val.length > 1){
			//ajax 객체 생성 
			var request = new XMLHttpRequest();
			//ajax 요청 경로 생성 
			request.open('GET', 'emailcheck?email='+ val)
			//요청 전송 
			request.send('');
			
			//응답이 왔을 때 호출되는 콜백(Callback-이벤트가 발생하면 호출되는, ~했을 때 ) 메소드 등록
			request.onreadystatechange = function(e){
				if(request.readyState == 4 ){
					//서버가 정상적으로 처리하고 정상응답을 하면 
					if(request.status >= 200 && request.status < 300){
						//전송되어 온 데이터 확인 
						var result = request.responseText;
						alert(result);
						if(result.trim() == "true"){
							emaildisp.innerHTML = "사용 가능한 이메일 입니다."
							emaildisp.style.color = 'blue'
							emailcheck = true;
						}else{
							emaildisp.innerHTML = "사용 불가능한 이메일 입니다."
							emaildisp.style.color = 'red'
							emailcheck = false;
						}
					} else {
						alert(request.status);
					}
				}	
			};
		}
	});
	
	
	var nickname = document.getElementById("nickname");
	var nicknamedisp = document.getElementById("nicknamedisp");
	
	var nicknamecheck = false; 
	
	nickname.addEventListener("focusout", function(e){
		var val = nickname.value;
		//닉네임을 입력하지 않았으면 닉네임 중복체크를 하지 않음 
		if(val.trim().length <1 ){
			nicknamecheck = false;
			return;
		}
		//자바스크립트에서의 인코딩 
		//URL이나 파라미터에는 영문과 숫자를 제외하고는 인코딩이 되서 대입되어야 합니다. 
	//	val = escape(val);
		
		var request = new XMLHttpRequest();
		request.open('GET', 'nicknamecheck?nickname=' + val); 
		request.send('');
		request.onreadystatechange = function(e){
			//readyState 는 서버로 오는 응답의 상태: 4번이 응답이 완료된 경우 
			//0은 객체만 생성한 상태 ,1이면 요청을 한 상태
			//2이면 send()를 호출한 상태, 3이면 서버에서 응답이 오기 시작한 상태 
			if(request.readyState == 4){
				// 100번대 처리중, 200번대 정상응답
				//300번대는 리다이렉트 중, 400번대는 클라이언트 오류 
				//500번대는 서버 오류 
				if(request.status >= 200 && request.status < 300){
					var result = request.responseText;
					//JSON Parsing 
					var obj = JSON.parse(result);
					//동일한 닉네임이 없는 경우 
					if(obj.result == "true"){
						nicknamedisp.innerHTML = "사용 가능한 닉네임 입니다.";
						nicknamedisp.style.color = 'blue'
						nickname = true;						
					}
				    //동일한 닉네임이 있는 경우 
					else{
						nicknamedisp.innerHTML = "사용 불가능한 닉네임 입니다.";
						nicknamedisp.style.color = 'red'
						nickname = false;		
					}
					
				}
			}
		} 
	});
	
	
	var phone = document.getElementById("phone");
	var phonedisp = document.getElementById("phonedisp");
	
	var phonecheck = false; 
	
	phone.addEventListener("focusout", function(e){
		var val = phone.value;
		if(val.trim().length < 1 ){
			phonecheck = false;
			return;
		}
		//자바스크립트에서의 인코딩 
		//URL이나 파라미터에는 영문과 숫자를 제외하고는 인코딩이 되서 대입되어야 합니다. 
	//	val = escape(val);
		
		var request = new XMLHttpRequest();
		request.open('GET', 'phonecheck?phone=' + val); 
		request.send('');
		request.onreadystatechange = function(e){
			//readyState 는 서버로 오는 응답의 상태: 4번이 응답이 완료된 경우 
			//0은 객체만 생성한 상태 ,1이면 요청을 한 상태
			//2이면 send()를 호출한 상태, 3이면 서버에서 응답이 오기 시작한 상태 
			if(request.readyState == 4){
				// 100번대 처리중, 200번대 정상응답
				//300번대는 리다이렉트 중, 400번대는 클라이언트 오류 
				//500번대는 서버 오류 
				if(request.status >= 200 && request.status < 300){
					var result = request.responseText;
					//JSON Parsing 
					var obj = JSON.parse(result);
					//동일한 반호가 없는 경우 
					if(obj.result == "true"){
						phonedisp.innerHTML = "가입 가능한 번호입니다.";
						phonedisp.style.color = 'blue'
						phone = true;						
					}
				    //동일한 번호가 있는 경우 
					else{
						phonedisp.innerHTML = "기존에 있는 번호 입니다.";
						phonedisp.style.color = 'red'
						phone = false;		
					}
					
				}
			}
		} 
	});
	
	
	document.getElementById('joinform').addEventListener('submit', function(e){
		//이벤트 객체 생성 
		var event = e || window.event;
		
		if(emailcheck == false){
			email.focus();
			emaildisp.innerHTML = "이메일 중복 검사를 하세요!!";
			emaildisp.style.color = "red"; 
			//기본 이벤트 제고 - 폼의 데이터를 전송하지 않음 
			event.preventDefault();
			return;
		}
		if(nicknamecheck == false){
			nickname.focus();
			nicknamedisp.innerHTML = "별명의 중복 검사를 하세요!!";
			nicknamedisp.style.color = "red"; 
			//기본 이벤트 제고 - 폼의 데이터를 전송하지 않음 
			event.preventDefault();
			return;
		}
		if(phonecheck == false){
			phone.focus();
			phonedisp.innerHTML = "전화번호 중복 검사를 하세요!!";
			phonedisp.style.color = "red"; 
			//기본 이벤트 제고 - 폼의 데이터를 전송하지 않음 
			event.preventDefault();
			return;
		}
	});
</script>
</html>













