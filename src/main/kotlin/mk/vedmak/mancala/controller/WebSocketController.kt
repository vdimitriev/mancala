package mk.vedmak.mancala.controller

import mk.vedmak.mancala.model.ChatMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class WebSocketController {

    @Autowired
    lateinit var simpMessagingTemplate:SimpMessagingTemplate

    @MessageMapping("/chat.sendMessage")
    fun sendMessageToUser(@Payload chatMessage: ChatMessage): ChatMessage? {
        simpMessagingTemplate.convertAndSend("/queue/privateChatRoom-" + chatMessage.game, chatMessage)
        return chatMessage
    }

    @MessageMapping("/chat.addUser")
    fun addUserToUser(@Payload chatMessage: ChatMessage, headerAccessor: SimpMessageHeaderAccessor): ChatMessage? {
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        simpMessagingTemplate.convertAndSend("/queue/privateChatRoom-" + chatMessage.game, chatMessage)
        return chatMessage
    }
}