document.addEventListener("DOMContentLoaded", function () {
    fetch("/header.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header-placeholder").innerHTML = data;
            updateUserInfo();
        })
        .catch(error => console.error("헤더 로드 실패:", error));
});

function updateUserInfo() {
    const userInfoDiv = document.getElementById("user-info");
    const memberId = sessionStorage.getItem("memberId");
    const username = sessionStorage.getItem("username");
    const accessToken = sessionStorage.getItem("accessToken");

    userInfoDiv.innerHTML = ""; // 초기화

    if (memberId && username && accessToken) {
        const nameSpan = document.createElement("span");
        nameSpan.textContent = `👤 ${username}`;
        nameSpan.style.fontSize = "0.9rem";
        nameSpan.style.fontWeight = "bold";

        const logoutButton = document.createElement("button");
        logoutButton.textContent = "로그아웃";
        logoutButton.onclick = logout;
        logoutButton.style.background = "transparent";
        logoutButton.style.border = "none";
        logoutButton.style.fontSize = "0.9rem";
        logoutButton.style.cursor = "pointer";
        logoutButton.style.fontWeight = "bold";

        userInfoDiv.appendChild(nameSpan);
        userInfoDiv.appendChild(logoutButton);
    } else {
        const loginButton = document.createElement("button");
        loginButton.textContent = "로그인";
        loginButton.style.background = "transparent";
        loginButton.style.border = "none";
        loginButton.style.fontSize = "0.9rem";
        loginButton.style.cursor = "pointer";
        loginButton.style.textDecoration = "none";
        loginButton.style.fontWeight = "bold";

        loginButton.onclick = () => {
            window.location.href = "/member/members.html";
        };

        userInfoDiv.appendChild(loginButton);
    }
}

function logout() {
    sessionStorage.removeItem("memberId");
    sessionStorage.removeItem("username");
    sessionStorage.removeItem("accessToken");
    location.reload(); // 새로고침하여 로그인 상태 반영
}