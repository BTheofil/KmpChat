package data

import data.dto.MessageDto
import domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.isSuccess
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class KtorClient(
    private val client: HttpClient,
) {

    private var socket: WebSocketSession? = null

    suspend fun connectSocket(
        sender: String,
        groupId: String
    ): Boolean {
        socket = client.webSocketSession { url("ws://192.168.0.214:8080/chat/$groupId/$sender") }
        return socket?.isActive ?: false
    }

    suspend fun getHistory(groupId: String): List<Message>? {
        val response = client.get {
            url("http://192.168.0.214:8080/info/$groupId/history")
        }
        return if (response.status.isSuccess()) {
            response.body<List<MessageDto>>().map {
                Message(
                    id = it.id,
                    sender = it.sender,
                    message = it.message,
                    timeStamp = it.timeStamp
                )
            }
        } else {
            null
        }
    }

    suspend fun sendMessage(
        message: String
    ) = try {
        socket?.send(Frame.Text(message))
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun observeMessages(): Flow<Message>? = try {
        socket?.incoming
            ?.receiveAsFlow()
            ?.filter { it is Frame.Text }
            ?.map {
                val json = (it as? Frame.Text)?.readText() ?: ""
                val dto= Json.decodeFromString<MessageDto>(json)
                Message(
                    id = dto.id,
                    sender = dto.sender,
                    message = dto.message,
                    timeStamp = dto.timeStamp
                )
            }
    } catch (e: Exception) {
        e.printStackTrace()
        flow { }
    }

}