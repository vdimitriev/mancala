package mk.vedmak.mancala.controller

import mk.vedmak.mancala.entity.Game
import mk.vedmak.mancala.service.GameService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
class GameController() {

    @Autowired
    private lateinit var gameService: GameService

    @PostMapping(value = ["/game/moveone"], params = ["position", "player", "gameid"])
    fun makeAMove(@RequestParam("position") position: Int,
                  @RequestParam("player") player: Int,
                  @RequestParam("gameid") gameid: String?) {
        //The user should already by logged in to the application
        val auth = SecurityContextHolder.getContext().authentication
        gameService.move(player, position, gameid!!, auth.name)
    }

    @PostMapping(value = ["/game/create"], params = ["username"])
    fun createGame(@RequestParam("username") username: String?): String? {
        val game: Game? = gameService.createNewGame(username!!)
        logger.info("Game created [${game?.id},${game?.name}]")
        //println("Game created [${game?.id},${game?.name}]")
        return game?.id.toString()
    }

    @PostMapping(value = ["/game/join"], params = ["gameid", "username", "player"])
    fun joinGame(@RequestParam("gameid") gameid: String?,
                 @RequestParam("username") username: String?,
                 @RequestParam("player") player: Int) {
        logger.info("Player ${username} is joining game with id ${gameid} as player number ${player}.")
        gameService.joinGame(gameid!!, username!!, player)
    }

    @PostMapping(value = ["/game/resume"], params = ["gameid", "username"])
    fun resumeGame(@RequestParam("gameid") gameid: String?,
                   @RequestParam("username") username: String?) {
        gameService.resumeGame(gameid!!, username!!)
    }
}