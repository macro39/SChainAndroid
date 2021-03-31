package zigzaggroup.schain.mobile.data.models

import java.util.*

data class Error(
    val timestamp: Date,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)
