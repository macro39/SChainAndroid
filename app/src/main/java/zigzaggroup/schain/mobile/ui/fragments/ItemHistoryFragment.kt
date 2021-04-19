package zigzaggroup.schain.mobile.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.data.models.State
import zigzaggroup.schain.mobile.data.models.States
import zigzaggroup.schain.mobile.databinding.FragmentItemHistoryBinding
import zigzaggroup.schain.mobile.ui.adapters.ItemHistoryAdapter
import zigzaggroup.schain.mobile.utils.adapter
import zigzaggroup.schain.mobile.utils.title

class ItemHistoryFragment : Fragment(R.layout.fragment_item_history) {

    private lateinit var itemHistory: List<State>

    private lateinit var binding: FragmentItemHistoryBinding

    private val args: ItemHistoryFragmentArgs by navArgs()

    private lateinit var itemHistoryAdapter: ItemHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemHistoryBinding.bind(view)

        setHasOptionsMenu(true)

        itemHistory = args.itemHistory.map { it.state }

        activity?.title("${args.itemHistory.firstOrNull()?.product?.name ?: "Item"} history")


        binding.recyclerView.adapter(ItemHistoryAdapter(itemHistory))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item_history, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_map -> {
                val states = States()
                states.addAll(itemHistory)

                val action =
                    ItemHistoryFragmentDirections.actionItemHistoryFragmentToMapFragment(states)
                findNavController().navigate(action)
            }
        }

        return super.onOptionsItemSelected(item)
    }

}