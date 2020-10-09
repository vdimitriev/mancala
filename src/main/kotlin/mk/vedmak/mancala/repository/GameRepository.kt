package mk.vedmak.mancala.repository

import mk.vedmak.mancala.entity.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : JpaRepository<Game, Long> {
    @Query("from Game g where g.playerOne.username = :username")
    fun findAllGamesCreatedByUser(@Param("username") username: String?): List<Game>

    @Query("from Game g where g.playerTwo <> null and g.playerTwo.username = :username")
    fun findAllGamesAsSecondPlayer(@Param("username") username: String?): List<Game>

    @Query("from Game g where g.playerOne.username <> :username and g.playerTwo = null")
    fun findAllAvailableGames(@Param("username") username: String?): List<Game>

    @Query("from Game g where g.id = :id")
    fun getById(@Param("id") id:Long): Game?
}