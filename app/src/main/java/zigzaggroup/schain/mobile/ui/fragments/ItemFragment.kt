package zigzaggroup.schain.mobile.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.data.ApiCallHandler
import zigzaggroup.schain.mobile.data.Resource
import zigzaggroup.schain.mobile.data.models.Item
import zigzaggroup.schain.mobile.data.models.Prop
import zigzaggroup.schain.mobile.databinding.FragmentItemBinding
import zigzaggroup.schain.mobile.ui.adapters.PropsAdapter
import zigzaggroup.schain.mobile.ui.adapters.SubItemsAdapter
import zigzaggroup.schain.mobile.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class ItemFragment : Fragment(R.layout.fragment_item) {

    private lateinit var item: Item

    private lateinit var binding: FragmentItemBinding

    private val args: ItemFragmentArgs by navArgs()

    @Inject
    lateinit var apiCallHandler: ApiCallHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemBinding.bind(view)

        setHasOptionsMenu(true)

        item = args.item

        activity?.title("${item.product.name} detail")

        binding.tvId.text = item.id
        binding.tvId.movementMethod = ScrollingMovementMethod()

        binding.tvDescription.text = item.product.description
        binding.tvDescription.movementMethod = ScrollingMovementMethod()

        binding.tvType.text = item.product.type.type
        binding.tvType.movementMethod = ScrollingMovementMethod()

        binding.btnShowHistory.setOnClickListener {
            getHistory(item.id)
        }

        val propsAdapter = PropsAdapter(
            listOf(
                Prop(
                    "Serial number",
                    if (item.serialNumber.isBlank()) "-" else item.serialNumber
                ),
                Prop(
                    "Product number",
                    item.product.code ?: "-"
                )
            ) + item.product.props
        )
        binding.rvProps.adapter(propsAdapter)

        if (item.subItems.isNotEmpty()) {
            val subItemsAdapter = SubItemsAdapter(item.subItems, this::getItem)
            binding.rvSubItems.adapter(subItemsAdapter)
        } else {
            binding.tvSubItemsTitle.text = "No sub items"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_copy -> {
                val clipboard = this.requireContext()
                    .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("item id", this.item.id)
                clipboard.setPrimaryClip(clip)

                this.requireContext().toast("Item id copied to clipboard!")
            }
            R.id.action_share -> {
                ShareCompat.IntentBuilder.from(this.requireActivity())
                    .setType("text/plain")
                    .setChooserTitle("Schain item id")
                    .setText("Check out this awesome item at https://s-chain.tech/item/${this.item.id}")
                    .startChooser()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getItem(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                binding.progressBar.show()
                when (val response =
                    apiCallHandler.getItem(id)) {
                    is Resource.Error<*> -> {
                        binding.progressBar.hide()
                        activity?.applicationContext?.toast(response.message.toString())
                    }
                    is Resource.Success<*> -> {
                        binding.progressBar.hide()
                        val item = response.data

                        withContext(Dispatchers.Main) {
                            val action =
                                ItemFragmentDirections.actionItemFragmentSelf(
                                    item!!
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progressBar.hide()
                activity?.applicationContext?.toast(e.message.toString())
            }
        }
    }

    private fun getHistory(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                binding.progressBar.show()
                when (val response =
                    apiCallHandler.getItemHistory(id)) {
                    is Resource.Error<*> -> {
                        binding.progressBar.hide()
                        activity?.applicationContext?.toast(response.message.toString())
                    }
                    is Resource.Success<*> -> {
                        binding.progressBar.hide()
                        val history = response.data

                        withContext(Dispatchers.Main) {
                            val action =
                                ItemFragmentDirections.actionItemFragmentToItemHistoryFragment(
                                    history!!
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progressBar.hide()
                activity?.applicationContext?.toast(e.message.toString())
            }
        }
    }
}