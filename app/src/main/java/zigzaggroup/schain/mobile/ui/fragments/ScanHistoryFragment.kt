package zigzaggroup.schain.mobile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.data.ApiCallHandler
import zigzaggroup.schain.mobile.data.Resource
import zigzaggroup.schain.mobile.databinding.FragmentScanHistoryBinding
import zigzaggroup.schain.mobile.ui.adapters.ScanHistoryAdapter
import zigzaggroup.schain.mobile.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class ScanHistoryFragment : Fragment(R.layout.fragment_scan_history) {

    private lateinit var binding: FragmentScanHistoryBinding

    @Inject
    lateinit var apiCallHandler: ApiCallHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScanHistoryBinding.bind(view)

        activity?.title("Scan history")

        binding.progressBar.show()

        getData()
    }

    private fun getData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                binding.progressBar.show()
                when (val response =
                    apiCallHandler.getScanHistory()) {
                    is Resource.Error<*> -> {
                        binding.progressBar.hide()
                        requireContext().applicationContext?.toast(response.message.toString())
                    }
                    is Resource.Success<*> -> {
                        binding.progressBar.hide()
                        val data = response.data

                        withContext(Dispatchers.Main) {
                            if (data != null) {
                                binding.recyclerView.addItemDecoration(
                                    DividerItemDecoration(
                                        context,
                                        DividerItemDecoration.VERTICAL
                                    )
                                )

                                binding.recyclerView.adapter(
                                    ScanHistoryAdapter(
                                        data.content,
                                        this@ScanHistoryFragment::getItem
                                    )
                                )
                            } else {
                                requireContext().toast("Error while parsing data")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progressBar.hide()
                requireContext().toast(e.message.toString())
            }
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
                                ScanHistoryFragmentDirections.actionScanHistoryFragmentToItemFragment(
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


}