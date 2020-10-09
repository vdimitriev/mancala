package mk.vedmak.mancala.service

import mk.vedmak.mancala.entity.Game
import mk.vedmak.mancala.entity.Player
import mk.vedmak.mancala.entity.Pocket
import mk.vedmak.mancala.repository.GameRepository
import mk.vedmak.mancala.repository.PlayerRepository
import mk.vedmak.mancala.util.getRandomString
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

const val NR_OF_STONES = 6
const val P1_LOWER_LIMIT = 0
const val P1_UPPER_LIMIT = 6
const val P2_LOWER_LIMIT = 8
const val P2_UPPER_LIMIT = 13
const val P1_MAIN_POCKET = 7
const val P2_MAIN_POCKET = 14
const val P2_LOWER_BOUNDERY = 7

private val logger = KotlinLogging.logger {}

@Service
@Transactional
class GameService {

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var playerRepository: PlayerRepository

    val positions = mapOf(1 to 13, 2 to 12, 3 to 11, 4 to 10, 5 to 9, 6 to 8, 7 to 14, 8 to 6, 9 to 5, 10 to 4, 11 to 3, 12 to 2, 13 to 1, 14 to 7)
    val mainPockets = listOf(6, 13)

    fun createNewGame(username: String): Game? {
        val player: Player? = playerRepository.findByUsername(username)
        val game = Game(status = Game.GameStatus.NEW,
                        name = getRandomString(),
                        creationDate = Date(),
                        playerTurn = 1,
                        playerTurnName = username)
        game.pockets.add(Pocket(game = game,  position = 0, elementCount = 6, oppositePocketPosition = 12))
        game.pockets.add(Pocket(game = game,  position = 1, elementCount = 6, oppositePocketPosition = 11))
        game.pockets.add(Pocket(game = game,  position = 2, elementCount = 6, oppositePocketPosition = 10))
        game.pockets.add(Pocket(game = game,  position = 3, elementCount = 6, oppositePocketPosition = 9))
        game.pockets.add(Pocket(game = game,  position = 4, elementCount = 6, oppositePocketPosition = 8))
        game.pockets.add(Pocket(game = game,  position = 5, elementCount = 6, oppositePocketPosition = 7))
        game.pockets.add(Pocket(game = game,  position = 6, elementCount = 0, oppositePocketPosition = 13))
        game.pockets.add(Pocket(game = game,  position = 7, elementCount = 6, oppositePocketPosition = 5))
        game.pockets.add(Pocket(game = game,  position = 8, elementCount = 6, oppositePocketPosition = 4))
        game.pockets.add(Pocket(game = game,  position = 9, elementCount = 6, oppositePocketPosition = 3))
        game.pockets.add(Pocket(game = game,  position = 10, elementCount = 6, oppositePocketPosition = 2))
        game.pockets.add(Pocket(game = game,  position = 11, elementCount = 6, oppositePocketPosition = 1))
        game.pockets.add(Pocket(game = game,  position = 12, elementCount = 6, oppositePocketPosition = 0))
        game.pockets.add(Pocket(game = game,  position = 13, elementCount = 0, oppositePocketPosition = 6))
        game.playerOne = player
        return gameRepository.save(game)
    }

    fun getById(gameid: String): Game? {
        return gameRepository.getById(gameid.toLong())
    }

    fun joinGame(gameid: String, username: String, playerOrderNumber: Int):Game? {
        val game:Game = gameRepository.getOne(gameid.toLong())
        val player:Player? = playerRepository.findByUsername(username)
        player?.playerNumber = playerOrderNumber
        setCurrentPlayer(game, player, playerOrderNumber)
        game.status = Game.GameStatus.IN_PROGRESS
        logger.info("game player turn name is ${game.playerTurnName}")
        return gameRepository.save(game)
    }

    fun resumeGame(gameid: String, username: String):Game? {
        return gameRepository.findByIdOrNull(gameid.toLong())
    }

    fun findMyGames(username: String?):List<Game>? {
        val games = mutableListOf<Game>()
        games.addAll(gameRepository.findAllGamesCreatedByUser(username))
        games.addAll(gameRepository.findAllGamesAsSecondPlayer(username))
        return games
    }

    fun findAvailableGames(username: String):List<Game>? {
        return gameRepository.findAllAvailableGames(username)
    }

    fun move(playerNumber: Int, position: Int, gameid: String, username: String):Game? {
        val game:Game = gameRepository.getOne(gameid.toLong())
        if(playerNumber != game.playerTurn) {
            return game
        }

        val pockets:List<Pocket>? = game.pockets
        if(pockets == null) {
            return game
        }
        var lastPosition:Int = updateGamePockets(pockets, position, playerNumber)
        updateLastPocket(pockets, lastPosition, playerNumber)
        determineNextMove(game, playerNumber, lastPosition)
        return gameRepository.save(game)
    }

    private fun determineNextMove(game: Game, playerNumber: Int, lastPosition: Int) {
        if (playerNumber == 1 && lastPosition != P1_MAIN_POCKET) {
            game.playerTurn = (2)
            if (game.playerTwo != null) {
                game.playerTurnName = game.playerTwo?.username
            }
        } else if (lastPosition != P2_MAIN_POCKET) {
            game.playerTurn = (1)
            if (game.playerOne != null) {
                game.playerTurnName = game.playerOne?.username
            }
        }
        if (isEndOfGame(game)) {
            return
        }
    }

    private fun isEndOfGame(game: Game): Boolean {
        var sum = game.findSumOfPlayerOnePockets()
        if (sum == 0) {
            findWinner(1, game)
            game.status = (Game.GameStatus.FINISHED)
            return true
        }
        sum = game.findSumOfPlayerTwoPockets()
        if (sum == 0) {
            findWinner(2, game)
            game.status = (Game.GameStatus.FINISHED)
            return true
        }
        return false
    }

    private fun findWinner(playerTurn: Int, game: Game) {
        if (playerTurn == 1) {
            game.sumPlayerTwoMainPocket()
            game.setPlayerTwoPocketsToZero()
        } else {
            game.sumPlayerOneMainPocket()
            game.setPlayerOnePocketsToZero()
        }
        game.setGameWinner()
    }

    private fun updateLastPocket(pockets: List<Pocket>, lastPosition: Int, playerNumber: Int) {
        val opponentPosition: Int = positions[lastPosition] ?: return
        val opponentCount: Int = pockets[opponentPosition - 1].elementCount
        pockets[opponentPosition - 1].elementCount = 0
        pockets[lastPosition - 1].elementCount = 0
        if (isFirstPlayerLastPocket(pockets, lastPosition, playerNumber)) {
            val myCount: Int = pockets[P1_UPPER_LIMIT].elementCount
            pockets[P1_UPPER_LIMIT].elementCount = opponentCount + 1 + myCount
        } else if (isSecondPlayerLastPocket(pockets, lastPosition, playerNumber)) {
            val myCount: Int = pockets[P2_UPPER_LIMIT].elementCount
            pockets[P2_UPPER_LIMIT].elementCount = opponentCount + 1 + myCount
        }
    }

    private fun isSecondPlayerLastPocket(gamePockets: List<Pocket>, lastPosition: Int, playerNumber: Int): Boolean {
        return playerNumber == 2 && lastPosition >= P2_LOWER_LIMIT && lastPosition <= P2_UPPER_LIMIT
                && gamePockets[lastPosition - 1].elementCount == 1
    }

    private fun isFirstPlayerLastPocket(gamePockets: List<Pocket>, lastPosition: Int, playerNumber: Int): Boolean {
        return playerNumber == 1 && lastPosition >= P1_LOWER_LIMIT && lastPosition <= P1_UPPER_LIMIT
                && gamePockets[lastPosition - 1].elementCount == 1
    }

    private fun updateGamePockets(gamePockets: List<Pocket>, position: Int, playerNumber: Int): Int {
        val fromPocket = gamePockets[position - 1]
        val elementSize: Int = fromPocket.elementCount
        fromPocket.elementCount = 0
        var pos = position
        var i = 0
        while (i < elementSize) {
            if (pos == P2_MAIN_POCKET) {
                pos = 0
            }
            if (canIncreaseValue(playerNumber, pos)) {
                //We fetch pocket and we increas its value
                increasePocketValue(gamePockets, pos)
            } else {
                //If the pocket is other player main pocket we do not drop stone there, but we have to decrease
                //the index as no stone was spent
                i--
            }
            //We go to next pocket
            pos++
            i++
        }
        //Return last pocket position
        return pos
    }

    private fun canIncreaseValue(playerNumber: Int, pos: Int): Boolean {
        return (playerNumber == 1 && pos != P2_UPPER_LIMIT
                || playerNumber == 2 && pos != P1_UPPER_LIMIT)
    }

    private fun increasePocketValue(gamePockets: List<Pocket>, pos: Int) {
        val pocket = gamePockets[pos]
        val ec: Int = pocket.elementCount
        pocket.elementCount = (ec + 1)
    }
    
    private fun setCurrentPlayer(game: Game, player: Player?, playerOrderNumber: Int):Unit {
        if(playerOrderNumber == 1) {
            game.playerOne = player
        } else {
            game.playerTwo = player
        }
    }
}