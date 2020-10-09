package mk.vedmak.mancala.model

data class ChatMessage(val type: MessageType? = null,
                       val content: String? = null,
                       val sender: String? = null) {
    var game: String? = null

    enum class MessageType {
        CHAT, JOIN, LEAVE, MOVE
    }
}