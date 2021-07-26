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
    if (this.value == -1) {
        document.getElementById("caseDiv").classList.add("d-none");
    } else {
        document.getElementById("caseDiv").classList.remove("d-none");
        insertCases(this.value);
    }
})

//setting client if client is send in URL parameter
const clientId = document.getElementById("clientDiv").dataset.clientId;

if (clientId > 0) {
    document.querySelector("option[value='" + clientId + "']").setAttribute("selected", "true");
    document.getElementById("caseDiv").classList.remove("d-none");
    insertCases(clientId);
}

//filling case list if client is chosen at page load
if (document.getElementById("clientDiv").querySelector("option[selected]") != null && document.getElementById("clientDiv").querySelector("option[selected]").value != "-1") {
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

$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    });
});


if (document.querySelector("select#user") !== null) {
    const userId = document.getElementById("user").dataset.userId;
    document.getElementById("user").querySelector("option[value='" + userId + "']").setAttribute("selected", "true");
}