package zigzaggroup.schain.mobile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class State(
    val coordinates: Coordinates,
    val date: Date,
    val info: String,
    val updatedBy: String
) : Parcelable

@Parcelize
class States : ArrayList<State>(), Parcelable