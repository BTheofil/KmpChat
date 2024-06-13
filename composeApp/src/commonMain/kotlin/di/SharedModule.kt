package di

import data.KtorClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module
import presentation.main.MainViewModel

val sharedModule = module {

    single { HttpClient(CIO){
        this.install(Logging)
        this.install(WebSockets)
        this.install(ContentNegotiation) {
            this.json()
        }
    } }

    single<KtorClient> { KtorClient() }

    single<MainViewModel> { MainViewModel(get()) }
}