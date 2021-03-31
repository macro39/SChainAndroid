package zigzaggroup.schain.mobile.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import zigzaggroup.schain.mobile.databinding.ActivityMainBinding
import zigzaggroup.schain.mobile.main.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGet.setOnClickListener {
            binding.tvResponse.text = ""
            viewModel.getItem("1-bc46edd1-8768-4e7d-a3b7-d0ca3ae0055f")
        }

        binding.btnGetHistory.setOnClickListener {
            binding.tvResponse.text = ""
            viewModel.getHistory("1-bc46edd1-8768-4e7d-a3b7-d0ca3ae0055f")
        }

        lifecycleScope.launchWhenStarted {
            viewModel.item.collect { event ->
                when (event) {
                    is MainViewModel.Event.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvResponse.setTextColor(Color.BLACK)
                        binding.tvResponse.text = event.item.toString()
                    }
                    is MainViewModel.Event.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvResponse.setTextColor(Color.RED)
                        binding.tvResponse.text = event.errorText
                    }
                    is MainViewModel.Event.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.history.collect { event ->
                when (event) {
                    is MainViewModel.Event.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvResponse.setTextColor(Color.BLACK)
                        binding.tvResponse.text = event.item.map { item -> item.state }.toString()
                    }
                    is MainViewModel.Event.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvResponse.setTextColor(Color.RED)
                        binding.tvResponse.text = event.errorText
                    }
                    is MainViewModel.Event.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }
}