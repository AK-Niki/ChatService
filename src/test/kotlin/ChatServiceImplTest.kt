import org.junit.Assert.*
import org.junit.Test

    class ChatServiceTest {

        @Test
        fun `should return zero unread chats initially`() {
            val chatService = ChatServiceImpl()
            assertEquals(0, chatService.getUnreadChatsCount())
        }

        @Test
        fun `should create and return a chat`() {
            val chatService = ChatServiceImpl()
            val chat = chatService.createChat(1, 2)
            assertNotNull(chat)
        }

        @Test
        fun `should create and return a message`() {
            val chatService = ChatServiceImpl()
            val chat = chatService.createChat(1, 2)
            val message = chatService.createMessage(chat.id, 1, "Test message")
            assertNotNull(message)
        }

        @Test
        fun `should delete a message`() {
            val chatService = ChatServiceImpl()
            val chat = chatService.createChat(1, 2)
            val message = chatService.createMessage(chat.id, 1, "Test message")
            chatService.deleteMessage(chat.id, message.id)
            assertEquals(0, chat.messages.size)
        }

        @Test
        fun `should delete a chat`() {
            val chatService = ChatServiceImpl()
            val chat = chatService.createChat(1, 2)
            chatService.deleteChat(chat.id)
            assertEquals(0, chatService.getChats().size)
        }

        @Test
        fun `should return last messages`() {
            val chatService = ChatServiceImpl()
            val chat = chatService.createChat(1, 2)
            chatService.createMessage(chat.id, 1, "Test message 1")
            chatService.createMessage(chat.id, 2, "Test message 2")
            val lastMessages = chatService.getLastMessages()
            assertEquals(1, lastMessages.size)
            assertEquals("Test message 2", lastMessages[0])
        }

        @Test
        fun `should return messages from a chat`() {
            val chatService = ChatServiceImpl()
            val chat = chatService.createChat(1, 2)
            chatService.createMessage(chat.id, 1, "Test message 1")
            chatService.createMessage(chat.id, 2, "Test message 2")
            val messagesFromChat = chatService.getMessagesFromChat(chat.id, 1, 1) // Запрашиваем только 1 сообщение
            assertEquals(1, messagesFromChat.size)
            assertEquals("Test message 1", messagesFromChat[0].content) // Проверяем второе добавленное сообщение
        }


        // Тесты на исключения

        @Test(expected = IllegalArgumentException::class)
        fun `should throw exception when creating message with non-existent chat`() {
            val chatService = ChatServiceImpl()
            val nonExistentChatId = 999
            val senderId = 1
            val content = "Test message"

            chatService.createMessage(nonExistentChatId, senderId, content)
        }

        @Test(expected = IllegalArgumentException::class)
        fun `should throw exception when deleting non-existent message`() {
            val chatService = ChatServiceImpl()
            chatService.deleteMessage(999, 1)
        }
    }
