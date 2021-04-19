package zigzaggroup.schain.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import zigzaggroup.schain.mobile.data.models.Item
import zigzaggroup.schain.mobile.databinding.ItemSubitemBinding

class SubItemsAdapter(private val subItems: List<Item>, val getItem: (String) -> Unit) :
    RecyclerView.Adapter<SubItemsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSubitemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSubitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = subItems[position]

        holder.binding.tvTitle.text = item.product.name

        holder.binding.btnShowDetail.setOnClickListener {
            getItem(item.id)
        }
    }

    override fun getItemCount(): Int = subItems.size
}