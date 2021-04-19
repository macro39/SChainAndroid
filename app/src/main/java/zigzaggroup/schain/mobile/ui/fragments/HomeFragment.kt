package zigzaggroup.schain.mobile.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.data.ApiCallHandler
import zigzaggroup.schain.mobile.data.Resource
import zigzaggroup.schain.mobile.databinding.FragmentHomeBinding
import zigzaggroup.schain.mobile.ui.CaptureActivity
import zigzaggroup.schain.mobile.utils.DataHolder
import zigzaggroup.schain.mobile.utils.hide
import zigzaggroup.schain.mobile.utils.show
import zigzaggroup.schain.mobile.utils.toast
import java.util.*
import javax.inject.Inject
import kotlin.collections.set


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var apiCallHandler: ApiCallHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val itemId = DataHolder.getItemId()
        if (itemId?.isBlank() == false) {
            getItem(itemId)
        }

        binding.btnSearchById.setOnClickListener {
            val id = binding.etId.text ?: ""
            if (id.isBlank()) {
                requireContext().toast("Please, fill item id")
                return@setOnClickListener
            }

            getItem(id.toString())
        }

        binding.btnScanQR.setOnClickListener {
            scanQR()
        }

        binding.btnPickQR.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .start { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            getQRFromImage(data!!)
                        }
                        ImagePicker.RESULT_ERROR -> {
                            requireContext().toast("An error occurred while picking image!")
                        }
                    }
                }
        }
    }

    private fun getQRFromImage(intent: Intent) {
        val file = ImagePicker.getFile(intent)

        val bMap = BitmapFactory.decodeFile(file?.path)

        val intArray = IntArray(bMap.width * bMap.height)
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)

        val source: LuminanceSource =
            RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            val decodeHints: Hashtable<DecodeHintType, Any> = Hashtable<DecodeHintType, Any>()
            decodeHints[DecodeHintType.TRY_HARDER] = true
            decodeHints[DecodeHintType.PURE_BARCODE] = true

            val reader: Reader = MultiFormatReader()
            val result = reader.decode(bitmap, decodeHints)
            val potentiallyId = result.text

            getItem(potentiallyId)
        } catch (e: NotFoundException) {
            requireContext().toast("Cannot read QR code from selected image")
        }
    }

    private fun scanQR() {
        val integrator = IntentIntegrator.forSupportFragment(this).apply {
            captureActivity = CaptureActivity::class.java
            setOrientationLocked(false)
            setBeepEnabled(false)
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            setPrompt("Scan QR code")
        }

        startForResult(integrator.createScanIntent()) { result ->
            val res = IntentIntegrator.parseActivityResult(
                IntentIntegrator.REQUEST_CODE,
                Activity.RESULT_OK,
                result.data
            )
            if (result.data != null) getItem(res.contents)
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
                                HomeFragmentDirections.actionHomeFragmentToItemFragment(
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