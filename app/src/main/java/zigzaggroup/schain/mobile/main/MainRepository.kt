package zigzaggroup.schain.mobile.main

import zigzaggroup.schain.mobile.data.models.Item
import zigzaggroup.schain.mobile.data.models.ItemHistory
import zigzaggroup.schain.mobile.utils.Resource

interface MainRepository {

    suspend fun getItem(id: String): Resource<Item>
    suspend fun getItemHistory(id: String): Resource<ItemHistory>

}