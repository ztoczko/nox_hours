if (window.location.href.split("/").length >= 4) {
    document.getElementById("menu-" + window.location.href.split("/")[3]).classList.add("menuSelected");
}

//click event propagation for sorting functions
Array.from(document.getElementsByTagName("TH")).forEach(a => a.addEventListener("click", function () {

    if (this.getElementsByTagName("I").length > 0) {
        this.getElementsByTagName("I")[0].click();
    }
}));


