$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    });
});

document.getElementById("rateNotExpires1")
    .addEventListener("click", () => document.getElementById("dateToGroup").classList.toggle("d-none"));

document.querySelector("form")
    .addEventListener("submit", () => document.querySelectorAll("input[data-cash]")
        .forEach(el => el.value = el.value.replaceAll(",", ".")));