package presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    state: MainViewModel.UiState,
    onNameFieldChange: (String) -> Unit,
    onGroupFieldChange: (String) -> Unit,
    onEnterButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp)
    ) {
        Text(
            "Type your name",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameFieldChange
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Enter group id",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.groupId,
            onValueChange = onGroupFieldChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(32.dp))
        OutlinedButton(
            onClick = onEnterButtonClick,
            content = {
                Text(
                    "Enter",
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary
                )
            }
        )
    }
}