<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <meta charset="UTF-8">
    <title>Chating</title>
    <style>
        *{
            margin:0;
            padding:0;
        }
        .container{
            width: 500px;
            margin: 0 auto;
            padding: 25px
        }
        .container h1{
            text-align: left;
            padding: 5px 5px 5px 15px;
            color: #FFBB00;
            border-left: 3px solid #FFBB00;
            margin-bottom: 20px;
        }
        .chating{
            background-color: #000;
            width: 500px;
            height: 500px;
            overflow: auto;
            padding: 5px;
        }
        .chating .me{
            color: #FFE400;
            text-align: right;
            word-break: break-all;
        }
        .chating .others{
            color: #F6F6F6;
            text-align: left;
            word-break: break-all;
        }
        .chating .me-mafia{
            color: #d66fff;
            text-align: right;
            word-break: break-all;
        }
        .chating .others-mafia{
            color: #ff0000;
            text-align: left;
            word-break: break-all;
        }
        .chatting-input {
            width: 330px;
            height: 25px;
        }
        #yourMsg{
            display: none;
            width: 600px;
        }
    </style>
</head>

<script type="text/javascript">
    var socketVar;

    function wsOpen(){
        socketVar = new WebSocket("ws://" + location.host + "/chating");
        wsEvt();
    }

    function wsEvt() {
        socketVar.onopen = function(data){
            //소켓이 열리면 초기화 세팅하기
        }

        socketVar.onmessage = function(data) {
            var msg = data.data;
            if(msg != null && msg.trim() != ''){
                var jsonTemp = JSON.parse(msg);
                if(jsonTemp.type == "getId"){
                    var sessionId = jsonTemp.sessionId != null ? jsonTemp.sessionId : "";
                    if(sessionId != ''){
                        $("#sessionId").val(sessionId);
                    }
                }else if(jsonTemp.type == "message"){
                    if(jsonTemp.sessionId == $("#sessionId").val()){
                        $("#chating").append("<p class='me'>" + jsonTemp.msg + "</p>");
                        $("#chating").scrollTop($("#chating")[0].scrollHeight);
                    }else{
                        $("#chating").append("<p class='others'>" + jsonTemp.userName + " :" + jsonTemp.msg + "</p>");
                        $("#chating").scrollTop($("#chating")[0].scrollHeight);
                    }
                }else if(jsonTemp.type == "mafia"){
                    if ($('myJob').val() != 'mafia') {
                        return false;
                    }
                    if(jsonTemp.sessionId == $("#sessionId").val()){
                        $("#chating").append("<p class='me-mafia'>" + jsonTemp.msg + "</p>");
                        $("#chating").scrollTop($("#chating")[0].scrollHeight);
                    }else{
                        $("#chating").append("<p class='others-mafia'>" + jsonTemp.userName + " :" + jsonTemp.msg + "</p>");
                        $("#chating").scrollTop($("#chating")[0].scrollHeight);
                    }
                }else{
                    console.warn("unknown type!")
                }
            }
        }

        document.addEventListener("keypress", function(e){
            if(e.keyCode == 13){ //enter press
                send();
            }
        });
    }

    function chatName(){
        var userName = $("#userName").val();
        if(userName == null || userName.trim() == ""){
            alert("사용자 이름을 입력해주세요.");
            $("#userName").focus();
        }else{
            wsOpen();
            $("#yourName").hide();
            $("#yourMsg").show();
        }
    }

    function send() {
        if ($('#chatting').val() == "") return false;
        var option;
        if ($('#mafiaChat').checked()) {
            option = {
                type: "mafia",
                sessionId : $("#sessionId").val(),
                userName : $("#userName").val(),
                msg : $("#chatting").val()
            }
        } else {
            option = {
                type: "message",
                sessionId : $("#sessionId").val(),
                userName : $("#userName").val(),
                msg : $("#chatting").val()
            }
        }
        socketVar.send(JSON.stringify(option));
        $('#chatting').val("");
    }
</script>
<body>
<div id="container" class="container">
    <h1>채팅</h1>
    <input type="hidden" id="sessionId" value="">
    <input type="hidden" id="myJob" value="mafia">

    <div id="chating" class="chating">
    </div>

    <div id="yourName">
        <table class="inputTable">
            <tr>
                <th>사용자명</th>
                <th><input type="text" name="userName" id="userName"></th>
                <th><button onclick="chatName()" id="startBtn">이름 등록</button></th>
            </tr>
        </table>
    </div>
    <div id="yourMsg">
        <table class="inputTable">
            <tr>
                <th>마피아 챗</th>
                <th><input type="checkbox" id="mafiaChat"></th>
                <th>메시지</th>
                <th><input id="chatting" class="chatting-input" placeholder="보내실 메시지를 입력하세요."></th>
                <th><button onclick="send()" id="sendBtn">보내기</button></th>
            </tr>
        </table>
    </div>
</div>
</body>
</html>