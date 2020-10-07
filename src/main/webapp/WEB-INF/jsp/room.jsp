<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <meta charset="UTF-8">
    <title>Room</title>
    <link rel="stylesheet" href="/css/room.css">
    <script src="/js/room.js"></script>
</head>
<body>
<div class="container">
    <h1>Mafia In Rifle</h1>
    <div id="roomContainer" class="roomContainer">
        <table id="roomList" class="roomList"></table>
    </div>
    <div>
        <table class="inputTable">
            <tr>
                <th>방 제목</th>
                <th><input type="text" name="roomName" id="roomName"></th>
                <th><button id="createRoom">방 만들기</button></th>
            </tr>
        </table>
    </div>
</div>
</body>
</html>