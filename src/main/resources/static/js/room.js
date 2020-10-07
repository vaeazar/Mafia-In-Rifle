var ws;
window.onload = function(){
    getRoom();
    createRoom();
}

function getRoom(){
    commonAjax('/getRoom', "", 'post', function(result){
        createChatingRoom(result);
    });
}

function createRoom(){
    $("#createRoom").click(function(){
        var msg = {	roomName : $('#roomName').val()	};

        commonAjax('/createRoom', msg, 'post', function(result){
            createChatingRoom(result);
        });

        $("#roomName").val("");
    });
}

function goRoom(number, name, id){
    location.href="/moveChating?roomNumber="+number+"&"+"roomName="+name+"&"+"roomId="+id;
}

function createChatingRoom(res){
    if(res != null){
        var tag = "<tr><th class='num'>순서</th><th class='room'>방 이름</th><th class='count'>인원</th><th class='go'></th></tr>";
        res.forEach(function(newRoom, idx){
            var newRoomName = newRoom.roomName.trim();
            var newRoomNumber = newRoom.roomNumber;
            var newRoomId = newRoom.roomId;
            var newroomCount = newRoom.roomCount;
            tag += "<tr>"+
                "<td class='num'>"+(idx+1)+"</td>"+
                "<td class='room'>"+ newRoomName +"</td>"+
                "<td class='count'>"+ newroomCount +"/15</td>"+
                "<td class='go'><button type='button' onclick='goRoom(\""+newRoomNumber+"\", \""+newRoomName+"\", \""+newRoomId+"\")'>참여</button></td>" +
                "</tr>";
        });
        $("#roomList").empty().append(tag);
    }
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