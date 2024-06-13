package presentation.main

import androidx.lifecycle.ViewModel
import data.KtorClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val name: String = "",
        val groupId: String = ""
    )

    fun onNameChange(text: String) = _uiState.update { it.copy(name = text) }

    fun onGroupIdChange(text: String) = _uiState.update { it.copy(groupId = text) }
}