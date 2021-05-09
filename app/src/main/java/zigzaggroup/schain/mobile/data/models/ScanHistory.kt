package zigzaggroup.schain.mobile.data.models

import java.util.*

data class ScanHistory(
    val id: Int,
    val item: Item,
    val at: Date
)

data class ScanHistoryBase(
    val content: List<ScanHistory>
)