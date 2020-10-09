package mk.vedmak.mancala.controller

import mk.vedmak.mancala.entity.Player
import mk.vedmak.mancala.service.GameService
import mk.vedmak.mancala.service.PlayerService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@Controller
class HomeController {

    @Autowired
    private lateinit var gameService: GameService

    @Autowired
    private lateinit var playerService: PlayerService

    @RequestMapping(value = ["/", "/login"], method = [RequestMethod.GET])
    fun login(model: Model, error: String?, logout: String?): String? {
        if (error != null) model.addAttribute("error", "Your username and password is invalid.")
        if (logout != null) model.addAttribute("message", "You have been logged out successfully.")
        return "login"
    }

    @GetMapping(value = ["/game/play/{gameid}"])
    fun getGamePage(model: Model, @PathVariable("gameid") gameid: String?): String? {
        model.addAttribute("game", gameService.getById(gameid!!))
        return "game"
    }

    @GetMapping(value = ["/games"])
    fun getGames(model: Model): String? {
        val auth = SecurityContextHolder.getContext().authentication
        model.addAttribute("availablegames", gameService.findAvailableGames(auth.name))
        model.addAttribute("mygames", gameService.findMyGames(auth.name))
        return "games"
    }

    @RequestMapping(value = ["/registration"], method = [RequestMethod.GET])
    fun registration(model: Model): String? {
        model.addAttribute("userForm", Player())
        return "registration"
    }

    @RequestMapping(value = ["/registration"], method = [RequestMethod.POST])
    fun registration(@ModelAttribute("userForm") player: Player?, bindingResult: BindingResult, model: Model?): String? {
        if (bindingResult.hasErrors()) {
            return "registration"
        }
        playerService.save(player!!)
        return "redirect:/games"
    }
}