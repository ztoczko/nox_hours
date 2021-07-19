$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    });
});
//show User and Client select fields if proper fields are already checked on page load
if (document.getElementById("basedOnUser1").hasAttribute("checked")) {
    document.getElementById("baseUserDiv").classList.remove("d-none");
}

if (document.getElementById("basedOnClient1").hasAttribute("checked")) {
    document.getElementById("baseClientDiv").classList.remove("d-none");
}

if (document.getElementById("showDetails1").hasAttribute("checked")) {
    document.getElementById("showNamesDiv").classList.remove("d-none");
}

const userId = document.getElementById("baseUserDiv").dataset.baseUserId;

if (userId > 0) {
    document.getElementById("baseUserDiv").querySelector("option[value='" + userId + "']").setAttribute("selected", "true");
}

document.getElementById("basedOnUser1").addEventListener("click", () => document.getElementById("baseUserDiv").classList.toggle("d-none"));
document.getElementById("basedOnClient1").addEventListener("click", () => document.getElementById("baseClientDiv").classList.toggle("d-none"));
document.getElementById("showDetails1").addEventListener("click", () => document.getElementById("showNamesDiv").classList.toggle("d-none"));

