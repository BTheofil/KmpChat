package domain.model

data class Message(
    val id: String,
    val sender: String,
    val message: String,
    val timeStamp: Long
)
