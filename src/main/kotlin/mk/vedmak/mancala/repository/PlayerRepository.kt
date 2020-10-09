package mk.vedmak.mancala.repository

import mk.vedmak.mancala.entity.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : JpaRepository<Player, Long> {
    fun findByUsername(username: String): Player?
}