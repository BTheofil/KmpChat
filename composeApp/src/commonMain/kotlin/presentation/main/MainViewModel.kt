package presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val name: String = "",
        val groupId: String = ""
    )

    fun onNameChange(text: String) = _uiState.update { it.copy(name = text) }

    fun onGroupIdChange(text: String) = _uiState.update { it.copy(groupId = text) }

    fun enterGroup() = viewModelScope.launch {
        userRepository.setUserInfo(uiState.value.name, uiState.value.groupId)
    }
}