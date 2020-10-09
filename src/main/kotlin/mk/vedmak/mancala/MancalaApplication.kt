package mk.vedmak.mancala

import mk.vedmak.mancala.entity.Player
import mk.vedmak.mancala.service.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class MancalaApplication {

    @Autowired
    private lateinit var playerservice: PlayerService

    @PostConstruct
    private fun init() {
        logger.info("init mancala application")

        playerservice.save(Player(username = "player1", password = "player1", passwordConfirm = "player1"))
        playerservice.save(Player(username = "player2", password = "player2", passwordConfirm = "player2"))
        playerservice.save(Player(username = "admin", password = "admin", passwordConfirm = "admin"))
    }
}

fun main(args: Array<String>) {
    runApplication<MancalaApplication>(*args)
}




