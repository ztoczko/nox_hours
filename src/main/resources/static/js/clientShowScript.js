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

document.querySelector(".toggleTableButtonGroup").querySelectorAll("div").forEach(el => el.addEventListener("click", function () {

    console.log("TEST");
    if (!this.classList.contains("buttonSelected")) {
        this.parentElement.querySelectorAll("div").forEach(el => el.classList.remove("buttonSelected"));
        this.classList.add("buttonSelected");

        if (this.id !== "toggleButton1") {
            document.getElementById("casesTable").classList.add("d-none");
            document.getElementById("casesPagination").classList.add("d-none");
        }

        if (this.id !== "toggleButton2" && document.getElementById("ratesTable") !== null) {
            document.getElementById("ratesTable").classList.add("d-none");
            document.getElementById("ratesPagination").classList.add("d-none");
        }

        if (this.id !== "toggleButton3") {
            document.getElementById("timesheetsTable").classList.add("d-none");
            document.getElementById("timesheetsPagination").classList.add("d-none");
        }

        if (this.id === "toggleButton1") {
            document.getElementById("casesTable").classList.remove("d-none");
            document.getElementById("casesPagination").classList.remove("d-none");
        }

        if (this.id === "toggleButton2") {
            document.getElementById("ratesTable").classList.remove("d-none");
            document.getElementById("ratesPagination").classList.remove("d-none");
        }

        if (this.id === "toggleButton3") {
            document.getElementById("timesheetsTable").classList.remove("d-none");
            document.getElementById("timesheetsPagination").classList.remove("d-none");
        }
    }
}));

//show all toggle for cases

document.getElementById("allCasesToggle").addEventListener("click", function () {
    this.parentElement.submit();
});


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