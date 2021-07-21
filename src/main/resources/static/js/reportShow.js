
document.getElementById("sendMail").addEventListener("click", () => {
    fetch("/reports/mail/".concat(document.getElementById("reportInfo").dataset.id)).then(response => {
        if (response.status === 200) {
            document.getElementById("emailSent").classList.remove("d-none");
        }
    }).catch(error => console.log(error));
});

document.getElementById("getPdf").addEventListener("click", () => {
    if (document.getElementById("pdfDownload").selected) {
        window.location.href = "/reports/pdf/".concat(document.getElementById("reportInfo").dataset.id);
    } else {
        fetch("/reports/mail/pdf/".concat(document.getElementById("reportInfo").dataset.id)).then(response => {
            if (response.status === 200) {
                document.getElementById("emailSent").classList.remove("d-none");
            }
        }).catch(error => console.log(error));
    }
})