var stompClient = null;

function connect() {
    var socket = new SockJS('/twoFactorAuth-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/twoFactorAuth', function (greeting) {
            console.log("received something...")
            console.log(JSON.parse(greeting.body).content);
            verifiedLogin(JSON.parse(greeting.body).content);
        });
    });
}

function verifiedLogin(message) {
    window.location = "/home";
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect();
});