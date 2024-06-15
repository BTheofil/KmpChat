import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import di.sharedModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import presentation.main.MainScreen
import presentation.main.MainViewModel
import presentation.message.MessageScreen
import presentation.message.MessageViewModel

@Composable
@Preview
fun App() {
    startKoin {
        modules(sharedModule)
    }
    MaterialTheme {

        val controller = rememberNavController()
        NavHost(navController = controller, startDestination = "main"){
            composable(route = "main"){
                val vm = koinInject<MainViewModel>()
                val state by vm.uiState.collectAsState()
                MainScreen(
                    state = state,
                    onNameFieldChange = vm::onNameChange,
                    onGroupFieldChange = vm::onGroupIdChange,
                    onEnterButtonClick = {
                        vm.enterGroup()
                        controller.navigate("message/${vm.uiState.value.groupId}")
                    }
                )
            }

            composable(route = "message/{groupId}"){
                val vm = koinInject<MessageViewModel>()
                val state by vm.uiState.collectAsState()
                MessageScreen(
                    state = state,
                    event = vm.uiEvent,
                    onMessageChange = vm::onMessageChange,
                    sendMessage = vm::sendMessage
                )
            }
        }
    }
}