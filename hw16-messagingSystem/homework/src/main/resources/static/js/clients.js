let stompClient = null;
let connected = false;

const switchConnection = (connect) => {
    connected = connect;
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/ws'));
    stompClient.connect({}, (frame) => {
        switchConnection(true)
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/currentTime', (time) => document.getElementById("currentTime").innerHTML = time.body);
        stompClient.subscribe('/topic/client/response/get/clients', (response) => showClients(response));
        stompClient.subscribe('/topic/client/response/save/client', (isClientSaved) =>  {
            if (isClientSaved) {
                stompClient.send("/app/get/clients", {}, '')
            }
        });
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    switchConnection(false)
    console.log("Disconnected");
}

const save = () => {
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
    if (connected) {
        stompClient.send("/app/save/client", {}, JSON.stringify(client))
    } else {
        saveClient(client);
    }
}

const showClients = (response) => {
    window.document.getElementById("clients").innerHTML = null
    for (const client of JSON.parse(response.body).clients) {
          $("#clients")
                      .append("<tr>")
                      .append("<td>" + client.id + "</td>")
                      .append("<td>" + client.name + "</td>")
                      .append("<td>" + client.address.street + "</td>")
                      .append("<td>" + client.phones[0].number + "</td>")
                      .append("</tr>")
    }
}

const showClient = (client) => {
    $("#clients")
        .append("<tr>")
        .append("<td>" + client.id + "</td>")
        .append("<td>" + client.name + "</td>")
        .append("<td>" + client.address.street + "</td>")
        .append("<td>" + client.phones[0].number + "</td>")
        .append("</tr>")
}

async function saveClient(client) {
    const response = await fetch('/api/clients', {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(client)
    });
    const savedClient = await response.json();
    showClient(savedClient);
}

function getClientById() {
    const clientIdTextBox = document.getElementById('clientIdTextBox');
    const clientDataContainer = document.getElementById('clientDataContainer');
    const id = clientIdTextBox.value;
    fetch('api/clients/' + id)
        .then(response => response.json())
        .then(client => clientDataContainer.innerHTML = JSON.stringify(client));
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#save").click(save);
});