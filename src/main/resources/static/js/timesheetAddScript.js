$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    });
});

const clientId = document.getElementById("clientDiv").dataset.clientId;

if (clientId > 0) {
    document.querySelector("option[value='" + clientId + "']").setAttribute("selected", "true");
}

if (document.querySelector("select#user") !== null) {
    const userId = document.getElementById("user").dataset.userId;
    document.getElementById("user").querySelector("option[value='" + userId + "']").setAttribute("selected", "true");
}