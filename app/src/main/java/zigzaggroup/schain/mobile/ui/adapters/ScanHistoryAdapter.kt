package zigzaggroup.schain.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import zigzaggroup.schain.mobile.data.models.ScanHistory
import zigzaggroup.schain.mobile.databinding.ItemScanHistoryBinding
import zigzaggroup.schain.mobile.utils.toStandardFormat

class ScanHistoryAdapter(
    private val scanHistories: List<ScanHistory>,
    private val clickCallback: (String) -> Unit
) :
    RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemScanHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemScanHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = scanHistories[position]

        holder.binding.tvTitle.text = item.item.product.name
        holder.binding.tvDate.text = item.at.toStandardFormat()

        holder.binding.itemScanHistoryView.setOnClickListener {
            clickCallback(item.item.id)
        }
    }

    override fun getItemCount(): Int = scanHistories.size


}