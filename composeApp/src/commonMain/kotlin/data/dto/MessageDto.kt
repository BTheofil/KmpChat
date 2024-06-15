package data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val sender: String,
    val message: String,
    val timeStamp: Long
)
