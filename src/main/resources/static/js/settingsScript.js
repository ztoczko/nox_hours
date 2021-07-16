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
    document.getElementById("firstName").removeAttribute("disabled");
    document.getElementById("lastName").removeAttribute("disabled");
    document.getElementById("email").removeAttribute("disabled");
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

