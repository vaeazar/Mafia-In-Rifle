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
                    $("#chatRoomHeader").append("<button onclick='startGame()' id='startBtn' class='startBtn'>시작</button>");
                }
            } else if(jsonTemp.type == "adminLeft"){
                if(jsonTemp.isAdmin){
                    $("#chatRoomHeader").append("<button onclick='startGame()' id='startBtn' class='startBtn'>시작</button>");
                }
            } else if(jsonTemp.type == "fail"){
                if (jsonTemp.failReason == 'nameExist') {
                    $("#yourMsg").hide();
                    $("#yourName").show();
                    Swal.fire({
                        icon: 'error',
                        text: jsonTemp.failMessage
                    })
                    $("#userName").focus();
                } else if (jsonTemp.failReason == 'fullBang') {
                    $("#yourMsg").hide();
                    alert();
                    Swal.fire({
                        icon: 'error',
                        text: jsonTemp.failMessage,
                        footer: '<a href="/room">방 목록으로 이동</a>'
                    });
                    //location.href("/room");
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

function startGame(){
    var roomId = {	roomId : $('#roomId').val()	};
    commonAjax('/setRoomStart', roomId, 'post', function(){
        $("#startBtn").remove();
    });
}

// 타이머 function

// Set the date we're counting down to
function timer () {

    var countDownDate = new Date();
    countDownDate.setMinutes(countDownDate.getMinutes() + 5);

// Update the count down every 1 second
    var x = setInterval(function () {

        // Get today's date and time
        var now = new Date();

        // Find the distance between now and the count down date
        var distance = countDownDate - now;

        // Time calculations for days, hours, minutes and seconds
        // var days = Math.floor(distance / (1000 * 60 * 60 * 24));
        // var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);

        // Display the result in the element with id="demo"
        document.getElementById("announce").innerHTML = "낮시간 " + minutes + "분 " + seconds + "초 남았습니다.";

        // If the count down is finished, write some text
        if (distance < 0) {
            clearInterval(x);
            document.getElementById("announce").innerHTML = "EXPIRED";
        }
    }, 1000);

}

function commonAjax(url, parameter, type, calbak, contentType){
    $.ajax({
        url: url,
        data: parameter,
        type: type,
        contentType : contentType!=null?contentType:'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (res) {
            calbak(res);
        },
        error : function(err){
            console.log('error');
            calbak(err);
        }
    });
}

