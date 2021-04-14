package zigzaggroup.schain.mobile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prop(
    val name: String,
    val value: String
) : Parcelable