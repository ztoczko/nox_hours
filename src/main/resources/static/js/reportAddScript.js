$(function () {
    $(".datepicker").datepicker({
        autoclose: true,
        todayHighlight: true
    });
});

$(document).ready(function () {
    $('.js-example-basic-single').select2({language: "pl"});
    document.querySelectorAll(".select2-container").forEach(item => item.style.width = "50%");
});

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
    const listElement = document.getElementById("baseCase");
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
$('#baseClient').on('select2:select', function () {
    console.log("test");
    insertCases(this.value);
});

//filling case list if client is chosen at page load
if (document.getElementById("baseClientDiv").querySelector("option[selected]") != null && document.getElementById("baseClientDiv").querySelector("option[selected]").value != "-1") {
    insertCases(document.getElementById("baseClientDiv").querySelector("option[selected]").value);
}

//selecting case in case case is already selected on page load
if (document.getElementById("baseCaseDiv").dataset.caseId != "-1") {
    const caseId = document.getElementById("baseCaseDiv").dataset.caseId;
    document.getElementById("baseCaseDiv").querySelectorAll("option").forEach(item => {
        if (item.value == caseId) {
            item.setAttribute("selected", "true");
        }
    })
}

//show User and Client select fields if proper fields are already checked on page load
if (document.getElementById("basedOnUser1").hasAttribute("checked")) {
    document.getElementById("baseUserDiv").classList.remove("d-none");
}

if (document.getElementById("basedOnClient1").hasAttribute("checked")) {
    document.getElementById("baseClientDiv").classList.remove("d-none");
    document.getElementById("basedOnCaseDiv").classList.remove("d-none");
}

if (document.getElementById("basedOnCase1").hasAttribute("checked")) {
    document.getElementById("baseCaseDiv").classList.remove("d-none");
}

if (document.getElementById("showDetails1").hasAttribute("checked")) {
    document.getElementById("showNamesDiv").classList.remove("d-none");
}

const userId = document.getElementById("baseUserDiv").dataset.baseUserId;

if (userId > 0) {
    document.getElementById("baseUserDiv").querySelector("option[value='" + userId + "']").setAttribute("selected", "true");
}

document.getElementById("basedOnUser1").addEventListener("click", () => document.getElementById("baseUserDiv").classList.toggle("d-none"));
document.getElementById("basedOnClient1").addEventListener("click", () => {
    document.getElementById("baseClientDiv").classList.toggle("d-none");
    document.getElementById("basedOnCaseDiv").classList.toggle("d-none");
    if (document.getElementById("basedOnCase1").checked) {
        document.getElementById("basedOnCase1").click();
    }
});

document.getElementById("basedOnCase1").addEventListener("click", () => document.getElementById("baseCaseDiv").classList.toggle("d-none"));

document.getElementById("showDetails1").addEventListener("click", () => document.getElementById("showNamesDiv").classList.toggle("d-none"));

