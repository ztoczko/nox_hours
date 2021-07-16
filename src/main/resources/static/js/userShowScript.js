//toggling edit mode:

if (document.getElementById("toggleEdit") !== null) {
    if (document.getElementById("toggleEdit").classList.contains("d-none")) {
        activateInputs();
    } else {
        document.getElementById("toggleEdit").addEventListener("click", () => {
            document.getElementById("toggleEdit").classList.add("d-none");
            document.getElementById("saveEdit").classList.remove("d-none");
            activateInputs();
        });
    }
}

function activateInputs() {
    document.getElementById("firstName").removeAttribute("disabled");
    document.getElementById("lastName").removeAttribute("disabled");
    document.getElementById("email").removeAttribute("disabled");
    document.getElementById("rank").removeAttribute("disabled");
    document.getElementById("privileges1").removeAttribute("disabled");
    document.getElementById("privileges2").removeAttribute("disabled");
    document.getElementById("privileges3").removeAttribute("disabled");
    document.getElementById("privileges4").removeAttribute("disabled");
    document.getElementById("isLocked1").removeAttribute("disabled");
    if (document.querySelector("[data-superadmin]").dataset.superadmin === "false") {
        document.getElementById("privileges4").setAttribute("onclick", "return false;");
    }
}

//force logout modal if applicable
if (document.getElementById("logoutModal") !== null) {
    window.addEventListener("DOMContentLoaded", () => {
        const elTimer = document.getElementById("timer");
        let timer = 5;
        setInterval(() => {
            elTimer.innerText = timer.toString();
            if (timer === 5) {
                $("#logoutModal").modal("show");
            }
            timer--;
            if (timer === 0) {
                window.location.href = "/logout";
            }
        }, 1000);
    });
}