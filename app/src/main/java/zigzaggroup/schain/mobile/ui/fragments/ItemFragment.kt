package zigzaggroup.schain.mobile.ui.fragments

import android.os.Bundle
import android.view.View
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
import zigzaggroup.schain.mobile.databinding.FragmentItemBinding
import zigzaggroup.schain.mobile.utils.hide
import zigzaggroup.schain.mobile.utils.show
import zigzaggroup.schain.mobile.utils.title
import zigzaggroup.schain.mobile.utils.toast
import javax.inject.Inject

@AndroidEntryPoint
class ItemFragment : Fragment(R.layout.fragment_item) {

    private lateinit var binding: FragmentItemBinding

    private val args: ItemFragmentArgs by navArgs()

    @Inject
    lateinit var apiCallHandler: ApiCallHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemBinding.bind(view)

        activity?.title(args.item.product.name)

        binding.tvResponse.text = args.item.id

        binding.btnShowHistory.setOnClickListener {
            getHistory("1-bc46edd1-8768-4e7d-a3b7-d0ca3ae0055f")
        }

        binding.btnShowAnother.setOnClickListener {
            getItem("2-18c54724-75d2-4157-8e6c-0998ad3f4327")
        }
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