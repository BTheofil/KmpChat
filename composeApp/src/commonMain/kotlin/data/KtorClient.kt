package data

import domain.Message
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json

class KtorClient(
    private val client: HttpClient
) {

    private suspend fun getSocketSession(
        sender: String,
        groupId: String
    ): DefaultClientWebSocketSession =
        client.webSocketSession { url("ws://localhost:8080/chat/$groupId/$sender") }

    suspend fun sendMessage(
        sender: String,
        groupId: String,
        message: String
    ) {
        try {
            getSocketSession(sender, groupId).send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun observeMessages(
        sender: String,
        groupId: String,
    ): Flow<Message> {
        return try {
            getSocketSession(sender, groupId).incoming
                .receiveAsFlow()
                .filter { it is Frame.Text }
                .map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    Json.decodeFromString<Message>(json)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }
}