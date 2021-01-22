<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/promise-polyfill@8/dist/polyfill.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Chating</title>
    <link rel="stylesheet" href="/css/mafiaChat.css">
    <script src="/js/mafiaChat.js"></script>
    <script src="/js/gamePlay.js"></script>
</head>
<body onunload="unload()">
<div id="container" class="container">
    <h1 id="chatRoomHeader">${roomName}</h1>
    <p id="announce"></p>
    <input type="hidden" id="sessionId" value="">
    <input type="hidden" id="userId" value="${userId}">
    <input type="hidden" id="roomName" value="${roomName}">
    <input type="hidden" id="roomNumber" value="${roomNumber}">
    <input type="hidden" id="roomId" value="${roomId}">
    <input type="hidden" id="myJob" value="mafia">

    <div class="chatAndList">
        <div id="chating" class="chating">
        </div>
        <div id="memberList" class="memberList">
        </div>
    </div>

    <div id="yourName">
        <table class="inputTable">

            <tr>
                <th>사용자명</th>
                <td><input type="text" name="userName" id="userName"></td>
                <td><button onclick="resendChatName()" id="startBtn">이름 등록</button></td>
            </tr>
        </table>
    </div>
    <div id="yourMsg">
        <table class="inputTable">
            <tr id="uiBtn">
                <td>메시지</td>
                <td><input id="chatting" class="chatting-input" placeholder="보내실 메시지를 입력하세요."></td>
                <td><button onclick="send()" id="sendBtn">보내기</button></td>
            </tr>
        </table>
        <table class="inputTable">
            <tr>
                <td>메뉴</td>
                <td><button onclick="cleanChatSpace()">모든 채팅 삭제</button></td>
                <td><button onclick="backToRoomList()" id="backBtn">나가기</button></td>
            </tr>
        </table>
        <table class="chatColorTable">
            <tr>
                <th>내 채팅 색</th>
                <td><input type="radio" id="myChatColor1" name="myChatColor" value="yello" checked><label for="myChatColor1">노랑</label></td>
                <td><input type="radio" id="myChatColor2" name="myChatColor" value="red"><label for="myChatColor2">빨강</label></td>
                <td><input type="radio" id="myChatColor3" name="myChatColor" value="blue"><label for="myChatColor3">파랑</label></td>
                <td><input type="radio" id="myChatColor4" name="myChatColor" value="green"><label for="myChatColor4">초록</label></td>
                <td><input type="radio" id="myChatColor5" name="myChatColor" value="white"><label for="myChatColor5">하양</label></td>
            </tr>
            <tr>
                <th>상대 채팅 색</th>
                <td><input type="radio" id="othersChatColor1" name="othersChatColor" value="yello"><label for="othersChatColor1">노랑</label></td>
                <td><input type="radio" id="othersChatColor2" name="othersChatColor" value="red"><label for="othersChatColor2">빨강</label></td>
                <td><input type="radio" id="othersChatColor3" name="othersChatColor" value="blue"><label for="othersChatColor3">파랑</label></td>
                <td><input type="radio" id="othersChatColor4" name="othersChatColor" value="green"><label for="othersChatColor4">초록</label></td>
                <td><input type="radio" id="othersChatColor5" name="othersChatColor" value="white" checked><label for="othersChatColor5">하양</label></td>
            </tr>
        </table>
    </div>
    <div class="voteList" id="voteList" onclick="closeVoteList()">
        <div class="voteList-content">
            <span class="voteList-close">&times;</span>
            <p>투표 할 인원을 클릭해주세요</p>
            <p id="memberNameBtn">Some text in the Modal..</p>
        </div>
    </div>
</div>
</body>
</html>