// Initial load
document.addEventListener("DOMContentLoaded", () => {
    displayData();
});


// Pagination configuration
const itemsPerPage = 5;
let currentPage = 1;

// DOM Elements
const tableBody = document.getElementById("tableBody");
const paginationContainer = document.getElementById("pagination");
const schedulerModal = document.getElementById("schedulerModal");
const excelModal = document.getElementById("excelModal");
// const saveExcel = document.getElementById("saveExcel");
const scheduleBtn = document.getElementById("scheduleBtn");
const excelBtn = document.getElementById("excelBtn");
const closeModal = document.getElementById("closeModal");
const cancelSchedule = document.getElementById("cancelSchedule");
const schedulerForm = document.getElementById("schedulerForm");

// Fetch and display paginated data from backend
async function displayData() {
    const loadingMessage = document.getElementById("loadingMessage");
    loadingMessage.style.display = "block";

    try {
        const response = await fetch(`/api/v1/scheduler?page=${currentPage - 1}&size=${itemsPerPage}`);
        if (!response.ok) throw new Error("Failed to load data");

        const data = await response.json();
        const users = data.content;
        const totalPages = data.totalPages;
        console.log(users);

        const noDataRow = document.querySelector(".no-data-row");
        tableBody.innerHTML = "";

        if (!users || users.length === 0) {
            noDataRow.style.display = "table-row";
            tableBody.appendChild(noDataRow);
            return;
        } else {
            noDataRow.style.display = "none";
        }

        users.forEach((user, index) => {

            const row = document.createElement("tr");
            let statusColor = {
                SCHEDULED: "#3498db",
                IN_PROGRESS: "#f1c40f",
                SENT: "#2ecc71",
                FAILED: "#e74c3c"
            }[user.status] || "#7f8c8d";

            row.innerHTML = `
                <td>${(currentPage - 1) * itemsPerPage + index + 1}</td>
                <td>${user.schedulerId.substring(0, 10)}</td>
                <td>${user.message.substring(0, 8)}</td>
                <td class="tooltip-cell" data-tooltip="${user.channels?.join(', ')}">
                    ${user.channels?.join(', ')}
                </td>
                <td class="tooltip-cell" data-tooltip="${user.phoneNumbers?.join(', ')}">
                    ${user.phoneNumbers?.join(', ')}
                </td>
                <td class="tooltip-cell" data-tooltip="${user.emailAddresses?.join(', ')}">
                    ${user.emailAddresses?.join(', ')}
                </td>
                <td>${user.scheduledAt}</td>
                
                <td><span style="color: ${statusColor}; font-weight: bold;">${user.status}</span></td>
                <td>
                    <div style="display: flex; align-items: center; gap: 8px;">
                        <span style="cursor: pointer;" onclick="modifyCurrentRow('${user.schedulerId}')">✏️</span>
                        <span style="cursor: pointer;" onclick="deleteCurrentRow('${user.schedulerId}')">🗑️</span>
                        <span style="cursor: pointer;" onclick="viewCurrentRow('${user.schedulerId}')">ⓘ</span>
                    </div>
                </td>
            `;
            tableBody.appendChild(row);
        });

        updatePagination(totalPages);
    } catch (err) {
        console.error(err);
        alert.error("Error loading data.");
    } finally {
        loadingMessage.style.display = "none";
    }
}

// Create pagination controls
function updatePagination(totalPages) {
    paginationContainer.innerHTML = "";

    const prevButton = document.createElement("button");
    prevButton.innerHTML = "&laquo;";
    prevButton.disabled = currentPage === 1;
    prevButton.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            displayData();
        }
    });
    paginationContainer.appendChild(prevButton);

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement("button");
        pageButton.innerText = i;
        pageButton.classList.toggle("active", i === currentPage);
        pageButton.addEventListener("click", () => {
            currentPage = i;
            displayData();
        });
        paginationContainer.appendChild(pageButton);
    }

    const nextButton = document.createElement("button");
    nextButton.innerHTML = "&raquo;";
    nextButton.disabled = currentPage === totalPages;
    nextButton.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            displayData();
        }
    });
    paginationContainer.appendChild(nextButton);
}

// Table edit and remove
async function modifyCurrentRow(id) {
    try {
        const res = await fetch(`/api/v1/scheduler/${id}`);
        const data = await res.json();

        // Open modal
        document.getElementById("updateSchedulerModal").style.display = "flex";

        // Fill fields
        document.getElementById("message-update").value = data.message;
        document.getElementById("datetime-update").value = data.scheduledAt?.slice(0, 16);

        document.getElementById("phone-input-update").value = (data.phoneNumbers || []).join(', ');
        document.getElementById("email-input-update").value = (data.emailAddresses || []).join(', ');

        const channels = data.channels || [];
        document.getElementById("sms-update").checked = channels.includes("SMS");
        document.getElementById("push-update").checked = channels.includes("PUSH");
        document.getElementById("voice-update").checked = channels.includes("VOICE");
        document.getElementById("whatsapp-update").checked = channels.includes("WHATSAPP");
        const emailChecked = channels.includes("EMAIL");
        document.getElementById("email-update").checked = emailChecked;
        document.getElementById("email-container-update").style.display = emailChecked ? "block" : "none";

        // Store ID for update
        window.updateSchedulerId = id;

    } catch (err) {
        console.error("Error loading scheduler:", err);
        alertify.warn("Unable to load message data.");
    }
}


function closeUpdateModal() {
    document.getElementById("updateSchedulerModal").style.display = "none";
    window.updateSchedulerId = null;
}


async function deleteCurrentRow(schedulerId) {
    alertify.confirm("Are you sure you want to delete this message?",
        async function () {
            try {
                const res = await fetch(`/api/v1/scheduler/delete/${schedulerId}`, {
                    method: 'DELETE',
                });

                if (!res.ok) {
                    throw new Error('Failed to delete message');
                }

                alertify.success("Scheduled message deleted successfully for ID: " + schedulerId);

                const row = document.querySelector(`[data-scheduler-id="${schedulerId}"]`);
                if (row) row.remove();
                window.location.reload();
            } catch (err) {
                console.error(err);
                alertify.error("Failed to delete message.");
            }
        },
        function () {
            alertify.message("Delete cancelled.");
        }
    );
}



function viewCurrentRow(id) {
    alertify.success("View clicked!" + id);
}

//Submit Update
async function submitUpdate() {
    const id = window.updateSchedulerId;

    const payload = {
        message: document.getElementById("message-update").value,
        scheduledAt: document.getElementById("datetime-update").value,
        phoneNumbers: document.getElementById("phone-input-update").value.split(',').map(e => e.trim()),
        emailAddresses: document.getElementById("email-input-update").value.split(',').map(e => e.trim()),
        channels: [
            ...(document.getElementById("sms-update").checked ? ["SMS"] : []),
            ...(document.getElementById("push-update").checked ? ["PUSH"] : []),
            ...(document.getElementById("voice-update").checked ? ["VOICE"] : []),
            ...(document.getElementById("whatsapp-update").checked ? ["WHATSAPP"] : []),
            ...(document.getElementById("email-update").checked ? ["EMAIL"] : [])
        ]
    };

    try {
        const res = await fetch(`/api/v1/scheduler/update/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!res.ok) throw new Error("Update failed");

        alertify.success("Message updated successfully!");
        closeUpdateModal();
        displayData(); // reload data table
    } catch (err) {
        console.error(err);
        alertify.error("Failed to update message.");
    }
}


// Modal open/close functions
function openModal() {
    schedulerModal.style.display = "flex";
    const now = new Date();
    now.setHours(now.getHours() + 1);
    const formattedDateTime = now.toISOString().slice(0, 16);
    document.getElementById("schedule-datetime").value = formattedDateTime;
}

// Convert Excel serial number to date string
function excelDateToJSDate(serial) {
    const utc_days = Math.floor(serial - 25569);
    const utc_value = utc_days * 86400;
    const date_info = new Date(utc_value * 1000);
    return date_info.toISOString().split('T')[0];
}

let hot;

//Excel file model open
async function openExcelModel() {

    excelModal.style.display = "block";
    // Load Excel file from backend
    const response = await fetch("/api/v1/birthday/load-excel");
    const arrayBuffer = await response.arrayBuffer();
    const workbook = XLSX.read(arrayBuffer, {type: "array"});
    const worksheet = workbook.Sheets[workbook.SheetNames[0]];
    const json = XLSX.utils.sheet_to_json(worksheet, {header: 1});

    // Convert Excel serial numbers (dates)
    const converted = json.map(row =>
        row.map(cell => {
            if (typeof cell === "number" && cell > 30000 && cell < 50000) {
                return excelDateToJSDate(cell);
            }
            return cell;
        })
    );

    // Initialize Handsontable
    const container = document.getElementById("excelContainer");
    container.innerHTML = "";
    hot = new Handsontable(container, {
        data: converted,
        rowHeaders: true,
        colHeaders: true,
        licenseKey: 'non-commercial-and-evaluation',
        stretchH: 'all',
        height: 400,
        width: '100%',
        manualColumnResize: true,
        manualRowResize: true,
        contextMenu: true
    });
}

//Save Excel data
function saveExcelFunc() {
    const data = hot.getData();
    const worksheet = XLSX.utils.aoa_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1");

    const wbout = XLSX.write(workbook, {bookType: 'xlsx', type: 'array'});

    fetch("/api/v1/birthday/save-excel", {
        method: "POST",
        body: wbout,
        headers: {
            "Content-Type": "application/octet-stream"
        }
    }).then(res => {
        if (res.ok) {
            alertify.success("Saved successfully!");
            closeExcelModal();
            displayData();
        } else {
            alertify.error("Save failed.");
        }
    });

}

function closeExcelModal() {
    document.getElementById("excelModal").style.display = "none";
}


function closeModalFunc() {
    schedulerModal.style.display = "none";
    phoneNumbers = [];
    document.getElementById('phone-tags').innerHTML = '';
    document.getElementById('phone-input').value = '';
    document.getElementById('message').value = '';
    document.getElementById('email-input').value = '';
    document.getElementById('sms-checkbox').checked = false;
    document.getElementById('push-checkbox').checked = false;
    document.getElementById('voice-checkbox').checked = false;
    document.getElementById('whatsapp-checkbox').checked = false;
    document.getElementById('email-checkbox').checked = false;
    document.getElementById('email-container').style.display = 'none';
}

// Modal button event listeners
scheduleBtn.addEventListener("click", openModal);
excelBtn.addEventListener("click", openExcelModel);
// saveExcel.addEventListener("click", saveExcelFunc);
closeModal.addEventListener("click", closeModalFunc);
cancelSchedule.addEventListener("click", closeModalFunc);

// Phone number tag management
let phoneNumbers = [];
const phoneInput = document.getElementById('phone-input');
const phoneTagsContainer = document.getElementById('phone-tags');

phoneInput.addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        const number = this.value.trim();
        if (number && !phoneNumbers.includes(number)) {
            phoneNumbers.push(number);
            const tag = document.createElement('span');
            tag.className = 'phone-tag';
            tag.innerHTML = `${number} <span class="remove" data-number="${number}">✕</span>`;
            phoneTagsContainer.appendChild(tag);
            this.value = '';
        }
    }
});

phoneTagsContainer.addEventListener('click', function (event) {
    if (event.target.classList.contains('remove')) {
        const numberToRemove = event.target.getAttribute('data-number');
        phoneNumbers = phoneNumbers.filter(num => num !== numberToRemove);
        event.target.parentElement.remove();
    }
});

// Email toggle
const emailCheckbox = document.getElementById('email-checkbox');
const emailContainer = document.getElementById('email-container');

emailCheckbox.addEventListener('change', function () {
    emailContainer.style.display = this.checked ? 'block' : 'none';
});

// Submit scheduler form
const submitButton = document.getElementById('submit-btn');
submitButton.addEventListener('click', async function () {
    const message = document.getElementById('message').value;
    const datetime = document.getElementById('schedule-datetime').value;

    const selectedChannels = [];
    if (document.getElementById('sms-checkbox').checked) selectedChannels.push("SMS");
    if (document.getElementById('push-checkbox').checked) selectedChannels.push("PUSH");
    if (document.getElementById('voice-checkbox').checked) selectedChannels.push("VOICE");
    if (document.getElementById('whatsapp-checkbox').checked) selectedChannels.push("WHATSAPP");
    if (document.getElementById('email-checkbox').checked) selectedChannels.push("EMAIL");

    let emailAddresses = [];
    if (document.getElementById('email-checkbox').checked) {
        emailAddresses = document.getElementById('email-input').value.split(',').map(e => e.trim());
    }

    const payload = {
        message: message,
        channels: selectedChannels,
        scheduledAt: datetime,
        phoneNumbers: phoneNumbers,
        emailAddresses: emailAddresses
    };

    try {
        const response = await fetch('/api/v1/scheduler/schedule', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });

        if (!response.ok) throw new Error("Failed to schedule message");
        alertify.success("Message scheduled successfully!");
        closeModalFunc();
    } catch (e) {
        console.error(e);
        alertify.error("Failed to schedule message.");
    }
});