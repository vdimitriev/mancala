package mk.vedmak.mancala.listener

import mk.vedmak.mancala.model.ChatMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener {

    @Autowired
    lateinit var messagingTemplate: SimpMessageSendingOperations

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent?) {
        println("Received a new web socket connection")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes!!["username"] as String?
        if (username != null) {
            println("User Disconnected : $username")
            val chatMessage = ChatMessage(type = ChatMessage.MessageType.LEAVE, sender = username)
            messagingTemplate.convertAndSend("/topic/publicChatRoom", chatMessage)
        }
    }
}