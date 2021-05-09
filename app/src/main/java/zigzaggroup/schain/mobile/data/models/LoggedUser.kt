package zigzaggroup.schain.mobile.data.models

import kotlin.math.floor

data class LoggedUser(
    val role: String,
    val token: String,
    val username: String,
    val ttl: Double = floor(System.currentTimeMillis() / 1000.0) + 7200
)