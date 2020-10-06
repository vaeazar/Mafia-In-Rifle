<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>마피아 게임 방목록</title>
        <style>
            table {
                border : 1px solid #444444;
                width: 100%;
            }
            td, th {
                border: 1px solid #444444;
            }
        </style>
    </head>
    <body>
        <table id="roomTable">
            <tr>
                <th>방 번호</th>
                <th>방제</th>
                <th>인원수</th>
                <th>방장</th>
                <th>비번</th>
            </tr>
            <c:if test="${ roomList != null }">
                <c:forEach items="${ roomList }" var="room" varStatus="status">
                    <tr>
                        <th>${status.count}</th>
                        <th><a href="/mafia/joinroom?title='${room.postId}'">${room.title}</a></th>
                        <th>${room.peopleCnt}</th>
                        <th>${room.host}</th>
                        <th>${room.hasPW}</th>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
    </body>
</html>