<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>마피아 게임 메인</title>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>
        <script src="https://cdn.jsdelivr.net/npm/promise-polyfill"></script>
        <script>
            function openJoinUserWindow() {
                window.open("/resources/joinUser.html","joinUser","width=700, height=500, resizable=no, scrollbars=no");
            }

            function submitFunc() {
                var userid = document.getElementById("userid").value;
                var password = document.getElementById("password").value;
                var request = new XMLHttpRequest();
                request.onload = function() {
                    if(request.status == 200) {
                        var str = request.responseText;
                        console.log("str : "+str);
                        if(str == "true") {
                            document.location.href = "/mafia/";
                        } else {
                            console.log("Fail!");
                            Swal.fire({
                                title : "로그인에 실패했습니다!",
                                text : "아이디와 비밀번호를 확인하세요!",
                                icon : "warning"
                            })
                            return false;
                        }
                    }
                }
                request.open('POST','/mafia/login/login.do',true);
                request.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
                request.send("userid="+userid+"&password="+password);
            }
        </script>
    </head>
    <body>
        <div id="main">
            <form>
                아이디 : <input type="text" name="userid" id="userid">
                <br>
                비밀번호 : <input type="password" name="password" id="password">
                <br>
                <button type="button" onclick="submitFunc();">로그인하기</button>
            </form>
            <button onclick="openJoinUserWindow();">아직 회원이 아니라면 클릭!</button>
        </div>
    </body>
</html>