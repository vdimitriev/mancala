package mk.vedmak.mancala.interceptor

import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class HttpHandshakeInterceptor : HandshakeInterceptor {
    override fun beforeHandshake(request: ServerHttpRequest,
                                 response: ServerHttpResponse,
                                 wsHandler: WebSocketHandler,
                                 attributes: MutableMap<String, Any>): Boolean {
        if (request is ServletServerHttpRequest) {
            val session = request.servletRequest.session
            attributes["sessionId"] = session.id
        }
        return true
    }

    override fun afterHandshake(request: ServerHttpRequest,
                                response: ServerHttpResponse,
                                wsHandler: WebSocketHandler,
                                ex: Exception?) {
        println("call aftershake")
    }
}