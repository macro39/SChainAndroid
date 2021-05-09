package zigzaggroup.schain.mobile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val code: String,
    val description: String,
    val type: Type,
    val createdBy: String,
    val createdAt: Date,
    val updatedBy: String,
    val updatedAt: Date,
    val props: List<Prop>,
    val subProducts: List<Product>
) : Parcelable