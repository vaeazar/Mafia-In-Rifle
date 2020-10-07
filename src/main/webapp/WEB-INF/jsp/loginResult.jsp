<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>로그인 결과창</title>
    </head>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@9"></script>
    <script src="https://cdn.jsdelivr.net/npm/promise-polyfill"></script>
    <body>
        <script>
            var result = "${result}";
            if(result == "true"){
                document.location.href = "/mafia/";
            } else {
                Swal.fire("로그인 실패하였습니다. 아이디와 비밀번호를 확인해주세요!");
                document.location.href = "${header.referer}";
            }
        </script>
    </body>
</html>