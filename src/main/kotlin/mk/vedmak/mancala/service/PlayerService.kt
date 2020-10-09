package mk.vedmak.mancala.service

import mk.vedmak.mancala.entity.Player
import mk.vedmak.mancala.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PlayerService(private val playerRepository: PlayerRepository) {

    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Transactional
    fun save(player: Player): Player? {
        player.password = bCryptPasswordEncoder.encode(player.password)
        return playerRepository.save(player)
    }
}