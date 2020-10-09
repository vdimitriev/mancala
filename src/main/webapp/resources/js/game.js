function joingame(gameid, username, player) {
    $.ajax({
        type: "POST",
        url: "/game/join",
        data: {
            gameid: gameid,
            username: username,
            player
        },
        success: function() {
            console.log("Player " + username + ", has joined game " + gameid)
            location.href = '/game/play/' + gameid;
        }
    });
}

function createnewgame(username) {
    $.ajax({
        type: "POST",
        url: "/game/create",
        data: {
            username: username
        },
        success: function(response) {
            console.log("game created response = " + response)
            location.href = '/game/play/'+response;
        }
    });
}

function resumegame(gameid, username) {
    $.ajax({
        type: "POST",
        url: "/game/resume",
        data: {
            gameid: gameid,
            username: username
        },
        success: function() {
            location.href = '/game/play/' + gameid;
        }
    });
}