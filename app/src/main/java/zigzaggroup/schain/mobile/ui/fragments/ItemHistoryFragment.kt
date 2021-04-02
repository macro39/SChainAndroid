package zigzaggroup.schain.mobile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.databinding.FragmentItemHistoryBinding
import zigzaggroup.schain.mobile.utils.title

class ItemHistoryFragment : Fragment(R.layout.fragment_item_history) {

    private lateinit var binding: FragmentItemHistoryBinding

    private val args: ItemHistoryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemHistoryBinding.bind(view)

        activity?.title("${args.itemHistory.firstOrNull()?.product?.name ?: "Item"} history")

        binding.tvResponse.text = args.itemHistory.map { it.state }.toString()
    }

}