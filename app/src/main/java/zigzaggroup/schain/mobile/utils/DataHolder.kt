package zigzaggroup.schain.mobile.utils

object DataHolder {
    private var itemId: String? = null

    fun getItemId(): String? {
        val id = itemId
        itemId = null
        return id
    }

    fun setItemId(value: String) {
        itemId = value
    }
}