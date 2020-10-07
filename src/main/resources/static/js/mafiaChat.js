var socketVar;
var jbRandom = Math.random();
document.addEventListener("DOMContentLoaded", function(){
    if (Math.floor( jbRandom * 2 ) == 1) {
        $('#myJob').val('mafia');
    } else {
        $('#myJob').val('citizen');
    }
});

function wsOpen(){
    //socketVar = new WebSocket("ws://" + location.host + "/chating");
    socketVar = new WebSocket("ws://" + location.host + "/chating/"+$("#roomId").val()+"_"+$("#userName").val());
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
                if(jsonTemp.isAdmin){
                    $("#chatRoomHeader").append("<button onclick='start()' id='startBtn' class='startBtn'>시작</button>");
                }
            } else if(jsonTemp.type == "adminLeft"){
                if(jsonTemp.isAdmin){
                    $("#chatRoomHeader").append("<button onclick='start()' id='startBtn' class='startBtn'>시작</button>");
                }
            } else if(jsonTemp.type == "fail"){
                if (jsonTemp.failReason == 'nameExist') {
                    $("#yourMsg").hide();
                    $("#yourName").show();
                    alert(jsonTemp.failMessage);
                    $("#userName").focus();
                } else if (jsonTemp.failReason == 'fullBang') {
                    $("#yourMsg").hide();
                    alert(jsonTemp.failMessage);
                    location.href("/room");
                }
            } else if(jsonTemp.type == "message"){
                if(jsonTemp.sessionId == $("#sessionId").val()){
                    $("#chating").append("<p class='me'>" + jsonTemp.msg + "</p>");
                    $("#chating").scrollTop($("#chating")[0].scrollHeight);
                }else{
                    $("#chating").append("<p class='others'>" + jsonTemp.userName + " :" + jsonTemp.msg + "</p>");
                    $("#chating").scrollTop($("#chating")[0].scrollHeight);
                }
            } else if(jsonTemp.type == "mafia"){
                if ($('#myJob').val() != 'mafia') {
                    return false;
                }
                if (jsonTemp.sessionId == $("#sessionId").val()){
                    $("#chating").append("<p class='me-mafia'>" + jsonTemp.msg + "</p>");
                    $("#chating").scrollTop($("#chating")[0].scrollHeight);
                } else {
                    $("#chating").append("<p class='others-mafia'>" + jsonTemp.userName + " :" + jsonTemp.msg + "</p>");
                    $("#chating").scrollTop($("#chating")[0].scrollHeight);
                }
            } else {
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
    if ($('#mafiaChat').prop("checked")) {
        option = {
            type: "mafia",
            roomNumber: $("#roomNumber").val(),
            roomId: $("#roomId").val(),
            sessionId : $("#sessionId").val(),
            userName : $("#userName").val(),
            msg : $("#chatting").val()
        }
    } else {
        option = {
            type: "message",
            roomNumber: $("#roomNumber").val(),
            roomId: $("#roomId").val(),
            sessionId : $("#sessionId").val(),
            userName : $("#userName").val(),
            msg : $("#chatting").val()
        }
    }
    socketVar.send(JSON.stringify(option));
    $('#chatting').val("");
}