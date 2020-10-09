package mk.vedmak.mancala.config

import mk.vedmak.mancala.entity.Player
import mk.vedmak.mancala.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlayerDetailsService : UserDetailsService {

    @Autowired
    lateinit var playerRepository : PlayerRepository

    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails? {
        if (username.isBlank()) {
            throw UsernameNotFoundException("Username cannot be empty")
        }
        val player: Player = playerRepository.findByUsername(username) ?: throw UsernameNotFoundException("Player $username doesn't exists")
        val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority("PLAYER"))
        //when(player.username) {
        //    "admin" -> grantedAuthorities.add(SimpleGrantedAuthority("ADMIN"))
        //}
        if (player.username == "admin") {
            grantedAuthorities.add(SimpleGrantedAuthority("ADMIN"))
        }
        return User(player.username, player.password, grantedAuthorities)
    }
}