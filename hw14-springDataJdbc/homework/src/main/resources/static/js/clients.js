function getClientById() {
    const clientIdTextBox = document.getElementById('clientIdTextBox');
    const clientDataContainer = document.getElementById('clientDataContainer');
    const id = clientIdTextBox.value;
    fetch('api/clients/' + id)
        .then(response => response.json())
        .then(client => clientDataContainer.innerHTML = JSON.stringify(client));
}

async function saveClient() {
    const client = {
        name: document.getElementById('client-name').value,
        address: {
            street: document.getElementById('client-address').value
        },
        phones: [
            {
                number: document.getElementById('client-primary-phone').value
            }
        ]
    };
    const response = await fetch('/api/clients', {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(client)
    });
    const savedClient = await response.json();
    window.location.reload();
}