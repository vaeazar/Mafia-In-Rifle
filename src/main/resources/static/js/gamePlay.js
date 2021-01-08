document.addEventListener("DOMContentLoaded", function () {
  var modal = document.getElementById('voteList');
  var btn = document.getElementById("modalBtn");
  var span = document.getElementsByClassName("voteList-close")[0];

  btn.onclick = function() {
    modal.style.display = "block";
  }

  span.onclick = function() {
    modal.style.display = "none";
  }
});
