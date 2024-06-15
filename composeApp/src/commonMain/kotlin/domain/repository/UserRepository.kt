package domain.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

data class UserInfo(
    val userName: String = "",
    val groupId: String = ""
)

class UserRepository {

    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: SharedFlow<UserInfo> = _userInfo

    suspend fun setUserInfo(
        name: String,
        groupId: String
    ) = _userInfo.emit(UserInfo(name, groupId))

}