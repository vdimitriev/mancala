package mk.vedmak.mancala.entity

import java.io.Serializable
import java.util.*
import javax.persistence.*

const val NR_OF_STONES = 6
const val P1_LOWER_LIMIT = 0
const val P1_UPPER_LIMIT = 6
const val P2_LOWER_LIMIT = 8
const val P2_UPPER_LIMIT = 13
const val P1_MAIN_POCKET = 7
const val P2_MAIN_POCKET = 14
const val P2_LOWER_BOUNDERY = 7

const val DRAW = "draw"

@Entity
@Table
data class Game(@Id @GeneratedValue(strategy = GenerationType.AUTO)
                @Column(name = "id") var id: Long? = null,
                @Column(name = "name") var name: String? = null,
                @Column(name = "creationdate") var creationDate: Date? = null,
                @Column(name = "playerturn") var playerTurn: Int = 0,
                @Column(name = "playerturnname") var playerTurnName: String? = null,
                @Column(name = "winner") var winner: String? = null,
                @ManyToOne(cascade = [CascadeType.PERSIST])
                @JoinColumn(name = "playeroneid", nullable = false)
                var playerOne: Player? = null,
                @ManyToOne(cascade = [CascadeType.PERSIST])
                @JoinColumn(name = "playertwoid")
                var playerTwo: Player? = null,
                @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL])
                var pockets: MutableList<Pocket> = mutableListOf(),
                @Enumerated(EnumType.STRING)
                var status: GameStatus? = null) : Serializable {

    fun setPlayerOnePocketsToZero() {
        for (i in P1_LOWER_LIMIT until P1_UPPER_LIMIT) {
            pockets[i].elementCount = 0
        }
    }

    fun findSumOfPlayerTwoPockets(): Int {
        var sum = 0
        for (i in P2_LOWER_BOUNDERY until P2_UPPER_LIMIT) {
            sum += pockets[i].elementCount
        }
        return sum
    }

    fun findSumOfPlayerOnePockets(): Int {
        var sum = 0
        for (i in P1_LOWER_LIMIT until P1_UPPER_LIMIT) {
            sum += pockets[i].elementCount
        }
        return sum
    }

    fun setPlayerTwoPocketsToZero() {
        for (i in P2_LOWER_BOUNDERY until P2_UPPER_LIMIT) {
            pockets[i].elementCount = 0
        }
    }

    fun sumPlayerOneMainPocket() {
        var sumOther = 0
        for (i in P1_LOWER_LIMIT until P1_UPPER_LIMIT) {
            sumOther += pockets[i].elementCount
        }
        val ec: Int = pockets[P1_UPPER_LIMIT].elementCount
        pockets[P1_UPPER_LIMIT].elementCount = ec + sumOther
    }

    fun sumPlayerTwoMainPocket() {
        var sumOther = 0
        for (i in P2_LOWER_BOUNDERY until P2_UPPER_LIMIT) {
            sumOther += pockets[i].elementCount
        }
        val ec: Int = pockets[P2_UPPER_LIMIT].elementCount
        pockets[P2_UPPER_LIMIT].elementCount = ec + sumOther
    }

    fun setGameWinner() {
        winner = if (pockets[P1_UPPER_LIMIT].elementCount > pockets[P2_UPPER_LIMIT].elementCount) {
            playerOne?.username
        } else if (pockets[P1_UPPER_LIMIT].elementCount
                < pockets[P2_UPPER_LIMIT].elementCount) {
            playerTwo?.username
        } else {
            DRAW
        }
    }

    enum class GameStatus {
        NEW, IN_PROGRESS, FINISHED
    }
}

