function moveone(position, player, gameid, elementcount, username) {
    if(elementcount > 0) {
        $.ajax({
            type: "POST",
            url: "/game/moveone",
            data: {
                position: position,
                player: player,
                gameid: gameid
            },
            success: function() {
                console.log("moveone called for player " + player + " and game id " + gameid)
                sendMessage(username);
            }
        });
    }
}