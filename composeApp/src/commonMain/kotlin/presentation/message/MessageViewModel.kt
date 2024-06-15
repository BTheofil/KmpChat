package presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.KtorClient
import domain.model.Message
import domain.repository.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel(
    private val client: KtorClient,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    data class UiState(
        val messageList: List<Message> = emptyList(),
        val message: String = "",
        val sender: String = "",
        val groupId: String = "",
    )

    sealed class UiEvent {
        data class FailedConnection(val message: String) : UiEvent()
    }

    init {
        viewModelScope.launch {
            userRepository.userInfo.collectLatest { userInfo ->
                val messages = client.getHistory(userInfo.groupId)

                if(messages.isNullOrEmpty()){
                    _uiEvent.send(UiEvent.FailedConnection("Can not get history"))
                }

                messages?.let {
                    _uiState.update { ui ->
                        ui.copy(
                            messageList = it
                        )
                    }
                }

                val isConnected = client.connectSocket(
                    userInfo.userName,
                    userInfo.groupId
                )

                if(isConnected){
                    client.observeMessages()
                        ?.onEach { message ->
                            val updatedList = uiState.value.messageList.toMutableList().apply {
                                add(0, message)
                            }
                            _uiState.update {
                                it.copy(
                                    messageList = updatedList
                                )
                            }
                        }?.launchIn(viewModelScope)
                } else {
                    _uiEvent.send(UiEvent.FailedConnection("Can not connect"))
                }
            }
        }
    }

    fun onMessageChange(text: String) = _uiState.update {
        it.copy(
            message = text
        )
    }

    fun sendMessage() {
        viewModelScope.launch {
            client.sendMessage(
                message = uiState.value.message,
            )
        }
    }
}