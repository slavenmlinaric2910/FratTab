let drinkLogId;
let member
let drink

function setDrinkLogId(event) {
    const button = event.currentTarget;
    drinkLogId = button.getAttribute("data-drink-log-id");
    member= button.getAttribute("data-member")
    drink = button.getAttribute("data-drink")
}

document.addEventListener("DOMContentLoaded", function () {
    const updateDrinkLogModal = document.getElementById("updateDrinkLog");
    updateDrinkLogModal.addEventListener("shown.bs.modal", function () {
        fetchDrinkLog()
    });
});

function fetchDrinkLog() {
    return fetch(`/api/v1/drinklog/${drinkLogId}`)
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            setDrinkLogData(data);
        })
        .catch((error) => {
            console.error("Error loading member data:", error);
            alert("Failed to load member data");
            throw error; // Optional: re-throw for further handling
        });
}

function setDrinkLogData(data) {
    document.getElementById("memberSelectionDiv").innerHTML = `
    <label for="memberSelect" class="form-label">Member</label>
                        <select class="form-select"  disabled>
                            <option value="">${member}</option>
                        </select>
    `;
    document.getElementById("drinkSelectionDiv").innerHTML = `
     <label for="drinkSelect" class="form-label">Drink</label>
                        <select class="form-select" id="drinkSelect" name="drinkId" disabled>
                            <option value="${data.drinkId}">${drink}</option>
                        </select>
    `;

    document.getElementById("quantityInputDiv").innerHTML = `
    <label for="quantityInput" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="quantityInput" name="qty" value="${data.qty}" min="1"
                            required>
                        <input value="${data.id}" id="id" name="id" hidden>
    `
    console.log(data);
}