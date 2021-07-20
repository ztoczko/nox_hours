
document.getElementById("sendMail").addEventListener("click", () => {
    fetch("/reports/mail/".concat(document.getElementById("reportInfo").dataset.id)).then(response => {
        if (response.status === 200) {
            document.getElementById("emailSent").classList.remove("d-none");
        }
    }).catch(error => console.log(error));
});