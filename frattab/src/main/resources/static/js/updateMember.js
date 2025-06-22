let memberId;
function fetchMemberById(event) {
    // Get the clicked button
    const button = event.currentTarget;
    // Get the member ID from the data attribute
    memberId = button.getAttribute("data-member-id");
}

document.addEventListener("DOMContentLoaded", function () {
    const editMemberModal = document.getElementById("editMemberModal");
    editMemberModal.addEventListener("shown.bs.modal", function () {
        loadMemberData(memberId);
    });
});

function loadMemberData(memberId) {
    return fetch(`/api/v1/member/${memberId}`)
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            updateMemberModal(data);
        })
        .catch((error) => {
            console.error("Error loading member data:", error);
            alert("Failed to load member data");
            throw error; // Optional: re-throw for further handling
        });
}

function updateMemberModal(data) {
    console.log(data);

    document.getElementById("firstNameDiv").innerHTML = `
        <input id="id" name="id" value="${data.id}" hidden>
        <label for="firstName" class="form-label">First Name</label>
        <input type="text" class="form-control" id="firstName" name="firstName" value="${data.firstName}">
        <div class="invalid-feedback">Please provide a first name.</div>
    `;
    document.getElementById("lastNameDiv").innerHTML = `
        <label for="lastName" class="form-label">Last Name</label>
        <input type="text" class="form-control" id="lastName" name="lastName" value="${data.lastName}">
        <div class="invalid-feedback">Please provide a last name.</div>
    `;
    document.getElementById("nickNameDiv").innerHTML = `
        <label for="nickname" class="form-label">Nickname</label>
        <input type="text" class="form-control" id="nickName" name="nickName" placeholder="Optional" value="${data.nickName}">
    `;
    document.getElementById("emailDiv").innerHTML = `
        <label for="email" class="form-label">Email</label>
        <input type="email" class="form-control" id="email" name="email"
            placeholder="member@example.com" value="${data.email}">
        <div class="invalid-feedback">Please provide a valid email.</div>
    `;
}
