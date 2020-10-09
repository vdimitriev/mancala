package mk.vedmak.mancala.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "pocket")
data class Pocket(@Id @GeneratedValue @Column(name = "id")
                  var id:Long? = null,
                  @Column(name = "elementcount")
                  var elementCount:Int = 6,
                  @Column(name = "oppositepocketelementcount")
                  val oppositePocketElementCount:Int = 6,
                  @Column(name = "position")
                  var position:Int,
                  @Column(name = "oppositepocketposition")
                  var oppositePocketPosition:Int,
                  @Column(name = "main")
                  var main:Boolean = false,
                  @ManyToOne(cascade = [CascadeType.ALL])
                  @JsonBackReference
                  @JoinColumn(name = "gameid", nullable = true)
                  var game: Game? = null,
                  @OneToOne
                  @JoinColumn(name = "oppositepocketid", nullable = true)
                  var oppositePocket: Pocket? = null) : Serializable {

    companion object {
        fun of(game: Game, position: Int, elementCount: Int,
               oppositePocketPosition:Int): Pocket {
            return Pocket(game = game,
                    position = position,
                    elementCount = elementCount,
                    oppositePocketPosition = oppositePocketPosition)
        }
    }
}