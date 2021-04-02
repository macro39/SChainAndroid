package zigzaggroup.schain.mobile.data.models

import java.io.Serializable

data class Item(
    val id: String,
    val state: State,
    val product: Product
) : Serializable