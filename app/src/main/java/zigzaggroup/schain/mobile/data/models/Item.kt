package zigzaggroup.schain.mobile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: String,
    val serialNumber: Int,
    val state: State,
    val product: Product,
    val subItems: List<Item>
) : Parcelable