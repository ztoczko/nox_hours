//toggling edit mode:
if (document.getElementById("toggleEdit").classList.contains("d-none")) {
    activateInputs();
} else {
    document.getElementById("toggleEdit").addEventListener("click", () => {
        document.getElementById("toggleEdit").classList.add("d-none");
        document.getElementById("saveEdit").classList.remove("d-none");
        activateInputs();
    });
}

function activateInputs() {
    document.getElementById("client").removeAttribute("disabled");
    document.getElementById("dateExecuted").removeAttribute("disabled");
    document.getElementById("hours").removeAttribute("disabled");
    document.getElementById("description").removeAttribute("disabled");
}

$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    });
});