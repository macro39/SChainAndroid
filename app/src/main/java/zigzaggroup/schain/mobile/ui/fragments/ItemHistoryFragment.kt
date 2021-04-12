package zigzaggroup.schain.mobile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.data.models.State
import zigzaggroup.schain.mobile.databinding.FragmentItemHistoryBinding
import zigzaggroup.schain.mobile.ui.adapters.ItemHistoryAdapter
import zigzaggroup.schain.mobile.utils.title

class ItemHistoryFragment : Fragment(R.layout.fragment_item_history) {

    private lateinit var itemHistory: List<State>

    private lateinit var binding: FragmentItemHistoryBinding

    private val args: ItemHistoryFragmentArgs by navArgs()

    private lateinit var itemHistoryAdapter: ItemHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemHistoryBinding.bind(view)

        itemHistory = args.itemHistory.map { it.state }

        activity?.title("${args.itemHistory.firstOrNull()?.product?.name ?: "Item"} history")

        val manager = LinearLayoutManager(this.requireContext(), RecyclerView.VERTICAL, false)

        binding.recyclerView.layoutManager = manager
        itemHistoryAdapter = ItemHistoryAdapter(itemHistory)
        binding.recyclerView.adapter = itemHistoryAdapter
    }

}