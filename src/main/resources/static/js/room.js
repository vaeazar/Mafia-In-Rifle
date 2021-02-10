var ws;
window.onload = function () {
  getRoom();
  createRoom();
  errorMessage();
}

function getRoom() {
  commonAjax('/getRoom', "", 'post', function (result) {
    createChatingRoom(result);
  });
}

function errorMessage() {
  let errorFlag = document.querySelector('#errorFlag').value;
  let errorMessage = document.querySelector('#errorMessage').value;

  if (errorFlag == 'deletedRoom') {
    if (typeof (history.pushState) != "undefined") { //브라우저가 지원하는 경우
      Swal.fire({
        icon: 'error',
        allowOutsideClick: false,
        text: errorMessage
      });
      history.pushState('', '', 'room');
    } else {
      Swal.fire({
        icon: 'error',
        allowOutsideClick: false,
        text: errorMessage
      }, function (isConfirm) {
        if (isConfirm) {
          location.href = url; //브라우저가 지원하지 않는 경우 페이지 이동처리
        } else {
          location.href = url; //브라우저가 지원하지 않는 경우 페이지 이동처리
        }
      });
    }
  }
}

// function createRoom() {
//   $("#createRoom").click(function () {
//     var msg = {roomName: $('#roomName').val()};
//
//     commonAjax('/createRoom', msg, 'post', function (result) {
//       createChatingRoom(result);
//     });
//
//     $("#roomName").val("");
//   });
// }

function createRoom() {
  $("#createRoom").click(function () {
    var roomName = $('#roomName').val();
    var userId = $('#userId').val();
    if (roomName == undefined || roomName == null || roomName.trim() == '') {
      return;
    }
    if (userId == undefined || userId == null || userId.trim() == '') {
      alert ('아이디를 입력해주세요.');
      return;
    }
    var form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', '/createRoom');
    document.charset = "utf-8";
    var setRoomName = document.createElement('input');
    setRoomName.setAttribute('type', 'hidden');
    setRoomName.setAttribute('name', 'roomName');
    setRoomName.setAttribute('value', roomName);
    var setUserId = document.createElement('input');
    setUserId.setAttribute('type', 'hidden');
    setUserId.setAttribute('name', 'userId');
    setUserId.setAttribute('value', userId);
    form.appendChild(setRoomName);
    form.appendChild(setUserId);
    document.body.appendChild(form);
    form.submit();
  });
}

function goRoom(number, name, id) {
  let userId = document.querySelector('#userId').value;
  if (userId == undefined || userId == null || userId.trim() == '') {
    alert ('아이디를 입력해주세요.');
  } else {
    location.href = "/moveChating?roomNumber=" + number + "&" + "roomName=" + name
        + "&" + "roomId=" + id
        + "&" + "userId=" + userId;
  }
}

function createChatingRoom(res) {
  if (res != null) {
    var tag = "";
    res.forEach(function (newRoom, idx) {
      var newRoomName = newRoom.roomName.trim();
      var newRoomNumber = newRoom.roomNumber;
      var newRoomId = newRoom.roomId;
      var newRoomCount = newRoom.roomCount;
      var newRoomStatus = newRoom.roomStatus;
      tag += "<tr>" +
          "<td class='num'>" + (idx + 1) + "</td>" +
          "<td class='room'>" + newRoomName + "</td>" +
          "<td class='count'>" + newRoomCount + "/15</td>"
      if (newRoomCount > 14 || newRoomStatus != 'wait') {
        tag += "<td class='go'><button type='button' class='fullBang'>참여</button></td>"
      } else {
        tag += "<td class='go'><button type='button' onclick='goRoom(\""
            + newRoomNumber + "\", \"" + newRoomName + "\", \"" + newRoomId
            + "\")'>참여</button></td>"
      }
      tag += "</tr>";
    });
    $("#roomList").empty().append(tag);
  }
}

function commonAjax(url, parameter, type, calbak, contentType) {
  $.ajax({
    url: url,
    data: parameter,
    type: type,
    contentType: contentType != null ? contentType
        : 'application/x-www-form-urlencoded; charset=UTF-8',
    success: function (res) {
      calbak(res);
    },
    error: function (err) {
      console.log('error');
      calbak(err);
    }
  });
}