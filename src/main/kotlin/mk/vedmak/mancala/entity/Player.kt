package mk.vedmak.mancala.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "player")
data class Player(@Id @GeneratedValue
                  var id:Long? = null,
                  var username:String?,
                  var password:String?,
                  var passwordConfirm:String?,
                  var playerNumber:Int = 0) : Serializable {
    constructor() : this(username = null, password = null, passwordConfirm = null)
}