<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/promise-polyfill@8/dist/polyfill.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
    <meta charset="UTF-8">
    <title>Room</title>
    <link rel="stylesheet" href="/css/room.css">
    <script src="/js/room.js"></script>
</head>
<body>
<div class="container">
    <h1>Mafia In Rifle</h1>
    <div id="roomContainer" class="roomContainer">
        <table class="roomList">
            <thead>
            <tr><th class='num'>순서</th><th class='room'>방 이름</th><th class='count'>인원</th><th class='go'></th></tr>
            </thead>
            <tbody id="roomList"></tbody>
        </table>
    </div>
    <div>
        <table class="inputTable">
            <tr>
                <th>닉네임</th>
                <th><input type="text" name="userId" id="userId"></th>
            </tr>
            <tr>
                <th>방 제목</th>
                <th><input type="text" name="roomName" id="roomName"></th>
                <th><button id="createRoom">방 만들기</button></th>
            </tr>
        </table>
    </div>
</div>
<input type="hidden" name="errorMessage" id="errorMessage" value="${errorMessage}">
<input type="hidden" name="errorFlag" id="errorFlag" value="${errorFlag}">
</body>
</html>