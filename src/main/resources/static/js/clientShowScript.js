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
    document.getElementById("name").removeAttribute("disabled");
    document.getElementById("closed1").removeAttribute("disabled");
}

//toggling tables
if (document.querySelector(".toggleTableButtonGroup") !== null) {
    document.querySelector(".toggleTableButtonGroup").querySelectorAll("div").forEach(el => el.addEventListener("click", function() {

        if (!this.classList.contains("buttonSelected")) {
            this.parentElement.querySelectorAll("div").forEach(el => el.classList.toggle("buttonSelected"));
            document.getElementById("ratesTable").classList.toggle("d-none");
            document.getElementById("ratesPagination").classList.toggle("d-none");
            document.getElementById("timesheetsTable").classList.toggle("d-none");
            document.getElementById("timesheetsPagination").classList.toggle("d-none");
        }
    }));
}

// rate delete modal fire up event:
document.querySelectorAll("button[data-rate-delete-button]").forEach(el => el.addEventListener("click", function () {
    const id = this.dataset.id;
    let deleteMsg = document.getElementById("rateDeleteMsgPart1").innerText + " " + document.querySelector("td[data-" + id + "-date-from]").innerText;
    if (document.querySelector("td[data-" + id + "-date-to]").innerText.length > 0) {
        deleteMsg += " " + document.getElementById("rateDeleteMsgPart2").innerText + " " + document.querySelector("td[data-" + id + "-date-to]").innerText;
    }
    deleteMsg += "?";
    document.getElementById("rateDeleteMsg").innerText = deleteMsg;

    document.getElementById("confirmRateDelete").onclick = () => location.href = "/clients/" + this.dataset.clientId + "/rate/delete/" + id + "";
    $("#deleteRateModal").modal("show");
}));