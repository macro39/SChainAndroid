package zigzaggroup.schain.mobile.data.models

import java.io.Serializable
import java.util.*

data class State(
    val coordinates: Coordinates,
    val date: Date,
    val info: String,
    val updatedBy: String
) : Serializable