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
        var tag = "";
        res.forEach(function(newRoom, idx){
            var newRoomName = newRoom.roomName.trim();
            var newRoomNumber = newRoom.roomNumber;
            var newRoomId = newRoom.roomId;
            var newRoomCount = newRoom.roomCount;
            var newRoomStatus = newRoom.roomStatus;
            tag += "<tr>"+
                "<td class='num'>"+(idx+1)+"</td>"+
                "<td class='room'>"+ newRoomName +"</td>"+
                "<td class='count'>"+ newRoomCount +"/15</td>"
                if (newRoomCount > 14 || !newRoomStatus) {
                    tag += "<td class='go'><button type='button' class='fullBang'>참여</button></td>"
                } else {
                    tag += "<td class='go'><button type='button' onclick='goRoom(\""+newRoomNumber+"\", \""+newRoomName+"\", \""+newRoomId+"\")'>참여</button></td>"
                }
                tag += "</tr>";
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