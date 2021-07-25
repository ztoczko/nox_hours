class Case {
    constructor(id, clientId, name) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
    }
}

const cases = new Array();
document.getElementById("casesList").querySelectorAll("div").forEach(item => cases.push(new Case(item.dataset.caseId, item.dataset.clientId, item.dataset.caseName)));
console.log(cases);

function insertCases(clientId) {
    const listElement = document.getElementById("clientCase");
    listElement.querySelectorAll("option").forEach(item => {
        if (item.value != "-1") {
            item.remove()
        }
        ;
    })
    cases.filter(item => item.clientId == clientId).forEach(item => {
        const optionEl = document.createElement("OPTION");
        optionEl.value = item.id;
        optionEl.innerText = item.name;
        listElement.appendChild(optionEl);
    })
}

//listener for changing case list in case of client change
document.getElementById("client").addEventListener("change", function () {
    insertCases(this.value);
})

//filling case list if client is chosen at page load
if (document.getElementById("clientDiv").querySelector("option[selected]").value != "-1") {
    insertCases(document.getElementById("clientDiv").querySelector("option[selected]").value);
}

//selecting case in case case is already selected on page load
if (document.getElementById("caseDiv").dataset.caseId != "-1") {
    const caseId = document.getElementById("caseDiv").dataset.caseId;
    document.getElementById("caseDiv").querySelectorAll("option").forEach(item => {
        if (item.value == caseId) {
            item.setAttribute("selected", "true");
        }
    })
}

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
    document.getElementById("clientCase").removeAttribute("disabled");
    document.getElementById("dateExecuted").removeAttribute("disabled");
    document.getElementById("hours").removeAttribute("disabled");
    document.getElementById("minutes").removeAttribute("disabled");
    document.getElementById("description").removeAttribute("disabled");
}

$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    });
});