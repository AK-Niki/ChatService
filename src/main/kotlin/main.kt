import java.util.*
fun main() {
    val chatService = ChatServiceImpl()

    val user1Id = 1
    val user2Id = 2

    // Создаем первый чат
    val chat1 = chatService.createChat(user1Id, user2Id)
    chatService.createMessage(chat1.id, user1Id, "Привет, как дела?")
    chatService.createMessage(chat1.id, user2Id, "Привет, все отлично, спасибо!")
    chatService.createMessage(chat1.id, user1Id, "Что делаешь сейчас?")
    chatService.createMessage(chat1.id, user2Id, "Просто читаю книгу.")

    // Создаем второй чат
    val user3Id = 3
    val chat2 = chatService.createChat(user1Id, user3Id)
    chatService.createMessage(chat2.id, user1Id, "Привет, как ты?")
    chatService.createMessage(chat2.id, user3Id, "Привет, все хорошо, спасибо!")

    println("Количество непрочитанных чатов: ${chatService.getUnreadChatsCount()}")
    println(chatService.getChatsInfo())
    println("Последние сообщения из чатов: ${chatService.getLastMessages()}")

    val messagesFromChat1 = chatService.getMessagesFromChat(chat1.id, user1Id, 2)
    println("Последние сообщения из чата с id ${chat1.id} от пользователя с id $user1Id: $messagesFromChat1")

    val messagesFromChat2 = chatService.getMessagesFromChat(chat2.id, user1Id, 2)
    println("Последние сообщения из чата с id ${chat2.id} от пользователя с id $user1Id: $messagesFromChat2")

    chatService.deleteMessage(chat1.id, 3)
    println("Сообщение с id 3 из чата ${chat1.id} удалено.")

    chatService.deleteChat(chat1.id)
    println("Чат с id ${chat1.id} удалён.")

    println(chatService.getChatsInfo())
}

data class Message(val id: Int, val senderId: Int, val content: String, var isRead: Boolean = false)

interface ChatService {
    fun getUnreadChatsCount(): Int
    fun getChats(): List<Chat>
    fun getChatsInfo(): String
    fun getLastMessages(): List<String>
    fun getMessagesFromChat(chatId: Int, interlocutorId: Int, count: Int): List<Message>
    fun createMessage(chatId: Int, senderId: Int, content: String): Message
    fun deleteMessage(chatId: Int, messageId: Int)
    fun createChat(senderId: Int, interlocutorId: Int): Chat
    fun deleteChat(chatId: Int)
}

class Chat(val id: Int, val participants: List<Int>, val messages: MutableList<Message> = mutableListOf())

class ChatServiceImpl : ChatService {
    private val chats = mutableListOf<Chat>()

    override fun getUnreadChatsCount(): Int {
        return chats.count { chat -> chat.messages.any { !it.isRead } }
    }

    override fun getChats(): List<Chat> {
        return chats
    }

    override fun getChatsInfo(): String {
        val totalChats = chats.size
        val unreadChats = getUnreadChatsCount()
        return "Список чатов: $totalChats, из них $unreadChats не прочитаны"
    }

    override fun getLastMessages(): List<String> {
        return chats.asSequence()
            .mapNotNull { chat -> chat.messages.lastOrNull()?.content ?: "Нет сообщений" }
            .toList()
    }

    override fun getMessagesFromChat(chatId: Int, interlocutorId: Int, count: Int): List<Message> {
        val chat = chats.find { it.id == chatId }
        val messages = chat?.messages
            ?.asSequence()
            ?.filter { it.senderId == interlocutorId }
            ?.toList()
            ?.takeLast(count)
        messages?.forEach { it.isRead = true }
        return messages ?: emptyList()
    }
    override fun createMessage(chatId: Int, senderId: Int, content: String): Message {
        val chat = chats.find { it.id == chatId } ?: throw IllegalArgumentException("Чат не найден")
        val message = Message(id = chat.messages.size + 1, senderId = senderId, content = content)
        chat.messages.add(message)
        return message
    }

    override fun deleteMessage(chatId: Int, messageId: Int) {
        val chat = chats.find { it.id == chatId } ?: throw IllegalArgumentException("Чат не найден")
        chat.messages.removeIf { it.id == messageId }
    }

    override fun createChat(senderId: Int, interlocutorId: Int): Chat {
        val chat = Chat(id = chats.size + 1, participants = listOf(senderId, interlocutorId))
        chats.add(chat)
        return chat
    }

    override fun deleteChat(chatId: Int) {
        chats.removeIf { it.id == chatId }
    }
}



