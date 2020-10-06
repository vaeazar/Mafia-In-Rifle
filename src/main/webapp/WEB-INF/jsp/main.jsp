<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>마피아 게임 메인</title>
        <script>
            window.onload = function() {
                var buttonDom = document.getElementsByClassName("button")[0];
                buttonDom.onclick = function() {
                    window.open("resources/joinUser.html","joinUser","width=500, height=300, resizable=no, scrollbars=no");
                }
            }
        </script>
    </head>
    <body>
        <div id="main">
            <form action="/mafia/loginUser" method="post">
                아이디 : <input type="text" name="userid">
                <br>
                비밀번호 : <input type="password" name="password">
                <br>
                <input type="submit" value="로그인하기">
            </form>
            <button onclick="openJoinUserWindow();">아직 회원이 아니라면 클릭!</button>
        </div>
    </body>
</html>