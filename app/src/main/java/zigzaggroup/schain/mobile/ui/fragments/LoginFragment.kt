package zigzaggroup.schain.mobile.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.data.ApiCallHandler
import zigzaggroup.schain.mobile.data.Resource
import zigzaggroup.schain.mobile.data.models.Credentials
import zigzaggroup.schain.mobile.databinding.FragmentLoginBinding
import zigzaggroup.schain.mobile.utils.PrefsProvider
import zigzaggroup.schain.mobile.utils.hide
import zigzaggroup.schain.mobile.utils.show
import zigzaggroup.schain.mobile.utils.toast
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

//    private var isLoginEnabled = true

    @Inject
    lateinit var prefsProvider: PrefsProvider

    @Inject
    lateinit var apiCallHandler: ApiCallHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        binding.btnAction.setOnClickListener {

            val username = binding.tvName.editText?.text.toString()
            val password = binding.tvPassword.editText?.text.toString()

            if (username.isBlank() || password.isBlank()) {
                requireContext().toast("Login credentials required!")
                return@setOnClickListener
            }

            val login = Credentials(username, password)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    binding.progressBar.show()
                    when (val response =
                        apiCallHandler.login(login)) {
                        is Resource.Error<*> -> {
                            binding.progressBar.hide()
                            requireContext().applicationContext?.toast(response.message.toString())
                        }
                        is Resource.Success<*> -> {
                            binding.progressBar.hide()
                            val user = response.data

                            if (user!!.role == "CLIENT") {
                                prefsProvider.setLoggedUser(user)

                                withContext(Dispatchers.Main) {
                                    requireContext().toast(
                                        "Successfully logged in",
                                        Toast.LENGTH_LONG
                                    )

                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                                    findNavController().navigate(action)
                                }
                            } else {
                                requireContext().toast("Unsupported role login!")
                            }
                        }
                    }
                } catch (e: Exception) {
                    binding.progressBar.hide()
                    requireContext().toast(e.message.toString())
                }
            }
        }

//        binding.btnAction.setOnClickListener {
//            val text = if (isLoginEnabled) "Login" else "Register"
//            this.requireContext().toast(text)
//        }
//
//        binding.btnReqLogin.setOnClickListener {
//            isLoginEnabled = true
//
//            binding.viewLogin.visibility = View.VISIBLE
//            binding.viewRegister.visibility = View.GONE
//            binding.btnAction.text = "Login"
//        }
//
//        binding.btnRegRegister.setOnClickListener {
//            isLoginEnabled = false
//
//            binding.viewLogin.visibility = View.GONE
//            binding.viewRegister.visibility = View.VISIBLE
//            binding.btnAction.text = "Register"
//        }
    }
}