package zigzaggroup.schain.mobile.ui.adapters

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import zigzaggroup.schain.mobile.data.models.Prop
import zigzaggroup.schain.mobile.databinding.ItemPropBinding


class PropsAdapter(private val props: List<Prop>) :
    RecyclerView.Adapter<PropsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPropBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPropBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prop = props[position]

        holder.binding.tvName.text = prop.name
        holder.binding.tvName.setOnTouchListener { v, _ ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        holder.binding.tvName.movementMethod = ScrollingMovementMethod()

        holder.binding.tvValue.text = prop.value
        holder.binding.tvValue.setOnTouchListener { v, _ ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        holder.binding.tvValue.movementMethod = ScrollingMovementMethod()
    }

    override fun getItemCount(): Int = props.size
}