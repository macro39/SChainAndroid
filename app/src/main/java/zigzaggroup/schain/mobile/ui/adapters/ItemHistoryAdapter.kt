package zigzaggroup.schain.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.data.models.State
import zigzaggroup.schain.mobile.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class ItemHistoryAdapter(private val states: List<State>) :
    RecyclerView.Adapter<ItemHistoryAdapter.TimeLineViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), viewType
        )
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val state = states[position]

        holder.binding.tvTitle.text = state.info

        val df = SimpleDateFormat("dd.MM.yyyy, HH:mm:ss", Locale.US)
        holder.binding.tvDate.text = df.format(state.date)

        val context = holder.binding.timeline.context
        holder.timeline.marker =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_marker, null)
    }

    override fun getItemCount() = states.size

    inner class TimeLineViewHolder(val binding: ItemHistoryBinding, viewType: Int) :
        RecyclerView.ViewHolder(binding.root) {

        val timeline = binding.timeline

        init {
            timeline.initLine(viewType)
        }
    }
}