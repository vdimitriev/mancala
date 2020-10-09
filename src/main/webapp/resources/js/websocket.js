var stompClient = null;
var username = null;
var table = null;
var game = null;

var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('#connecting');

function connect() {
    username = $('#username').html().trim();
    game = $('#game').html().trim();
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

connect();

function onConnected() {
    stompClient.subscribe('/queue/privateChatRoom-' + game, onMessageReceived);
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({sender: username, game: game, type: 'JOIN'}))
}

function onError(error) {
    console.log(error);
}

function sendChatMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            game: game,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function sendMessage(username) {
    console.log('send message for user ' + username);
    if(stompClient) {
        var chatMessage = {
            sender: username,
            content: game,
            game: game,
            type: 'MOVE'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
    }
}

function onMessageReceived(payload) {
    console.log('message received ' + payload);
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');
    if(message.type === 'MOVE') {
        $('#form').load(document.URL +  ' #form');
        return
    } else if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');
        var usernameElement = document.createElement('strong');
        usernameElement.classList.add('nickname');
        var usernameText = document.createTextNode(message.sender);
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }
    var textElement = document.createElement('span');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
    $('#form').load(document.URL +  ' #form');
}

messageForm.addEventListener('submit', sendChatMessage, true);