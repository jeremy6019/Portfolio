<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 조건문을 작성하기 위한 JSTL Core라이브러릴를 import  -->
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
</head>
<body>
	
	
	<a href="member/join">회원 가입</a><br/>
	<c:if test="${member == null}">
		<a href="member/login">로그인</a><br/>
	</c:if>
	<c:if test="${member != null}">
		<img src="images/${member.image}" width="50"  height="60" />
		${member.nickname}님<a href="member/logout">로그아웃</a><br/>
	</c:if>
	
	<!--  웹  푸시 출력 영역  -->
	<h3>Web Push</h3>
<!--<div id="pushdisp"></div>-->
    <img src='' width='400' hejght='400' id='pushdisp'/>
	
	<!-- 신문기사 출력 영역  -->
	<h3>한겨레 신문 실시간 기사</h3>
	<ul id="article">
	</ul>
</body>

<!--jQuery사용을 위한 링크 설정 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
   
    //5분마다 동작하는 타이머를 생성 
	setInterval(function(){
		$.ajax({
			url:'member/hani',
			data:{},
			dataType:'xml',
			success:function(data){
				//item태그를 전부 찾아서 순서대로 item에 대입하고 
				//index에 번호를 대입 
				output = "";
				//item태그 안에서 title 태그의 내용을 가져와서 출력 코드를 생성 
				$(data).find('item').each(function(index, item){
					output += '<p>' + $(this).find('title').text() +
					'</p>';
				});
				$('#article').html(output);
			},
			error:function(req, err){
		    	alert('실패');
			}
		});
	}, 1000*5*60);
    
    //웹 푸시를 요청하는 코드 작성 
    var eventSource = new EventSource('member/push');
    eventSource.addEventListener('message', function(e){
    //document.getElementById('pushdisp').innerHTML = e.data;
      document.getElementById('pushdisp').src = e.data;
    });
   
</script>

</html>













