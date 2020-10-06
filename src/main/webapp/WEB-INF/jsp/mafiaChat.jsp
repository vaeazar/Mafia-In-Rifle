<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Chating</title>
    <link rel="stylesheet" href="/css/mafiaChat.css">
    <script src="/js/mafiaChat.js"></script>
</head>
<body>
<div id="container" class="container">
    <h1>${roomName}</h1>
    <input type="hidden" id="sessionId" value="">
    <input type="hidden" id="roomNumber" value="${roomNumber}">
    <input type="hidden" id="myJob" value="mafia">

    <div id="chating" class="chating">
    </div>

    <div id="yourName">
        <table class="inputTable">
            <tr>
                <th>사용자명</th>
                <td><input type="text" name="userName" id="userName"></td>
                <td><button onclick="chatName()" id="startBtn">이름 등록</button></td>
            </tr>
        </table>
    </div>
    <div id="yourMsg">
        <table class="inputTable">
            <tr>
                <th>마피아 챗</th>
                <td><input type="checkbox" id="mafiaChat"></td>
                <td>메시지</td>
                <td><input id="chatting" class="chatting-input" placeholder="보내실 메시지를 입력하세요."></td>
                <td><button onclick="send()" id="sendBtn">보내기</button></td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>