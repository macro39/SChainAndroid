package zigzaggroup.schain.mobile.data.models

import java.io.Serializable
import java.util.*

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val type: Type,
    val createdBy: String,
    val createdAt: Date,
    val updatedBy: String,
    val updatedAt: Date,
    val props: List<Prop>,
    val subProducts: List<Product>
) : Serializable