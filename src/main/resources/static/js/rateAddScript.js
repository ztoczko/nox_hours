$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    }).datepicker('update', new Date());
});

document.getElementById("rateNotExpires1").addEventListener("click", () => document.getElementById("dateToGroup").classList.toggle("d-none"));