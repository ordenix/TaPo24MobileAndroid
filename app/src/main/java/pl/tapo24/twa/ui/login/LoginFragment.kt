package pl.tapo24.twa.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import pl.tapo24.twa.R
import pl.tapo24.twa.data.login.ToLoginData
import pl.tapo24.twa.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logowanie")
            .setMessage("Proszę czekać")
            .setCancelable(false)
            .create()
        val dialogProcessRegister = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Brak konta")
            .setMessage("Nie wykryliśmy konta w naszym serwisie, czy chcesz się zarejestrować?")
            .setNegativeButton("Anuluj") {
                    dialog, which ->
                dialog.dismiss()

            }
            .setPositiveButton("Zarejestruj się") {
                dialog, which ->
                view?.findNavController()?.navigate(R.id.action_nav_login_to_nav_register,
                    bundleOf("googleIdToken" to viewModel.tokenGoogle))
                dialog.dismiss()
            }
            .create()
        binding.ButtonRegister.setOnClickListener{view->
            view.findNavController().navigate(
                R.id.action_nav_login_to_nav_register,
                bundleOf("googleIdToken" to null)
            )
        }
        viewModel.buttonBlockedLogin.observe(viewLifecycleOwner, Observer {
            binding.loginButton.isEnabled = !it
        })
        viewModel.buttonBlockedGogoleLogin.observe(viewLifecycleOwner, Observer {
            binding.googleLoginButton.isEnabled = !it
        })
        binding.loginButton.setOnClickListener { view ->
            if (binding.login.text.isNullOrEmpty() || binding.password.text.isNullOrEmpty()) {
                showDialog(getString(R.string.enter_login_or_passwd))
            } else {
                val loginData = ToLoginData(binding.login.text.toString().trim(),binding.password.text.toString())
                viewModel.login(loginData)
            }
        }
        binding.googleLoginButton.setOnClickListener {
            viewModel.loginViaGoogle(requireContext())
        }
        viewModel.showLoader.observe(viewLifecycleOwner, Observer {
            if (it) {
                try {
                    dialog.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    dialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        viewModel.showError.observe(viewLifecycleOwner , Observer {
            if (it) {
                viewModel.showError.value = false
                showDialog(viewModel.errorMessage)
            }
        })
        viewModel.successLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "Poprawnie zalogowano", Snackbar.LENGTH_LONG).show()


                    findNavController().popBackStack()


            }
        })

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_forgotPasswordStep1)
        }

        viewModel.procesRegister.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.procesRegister.value = false
                dialogProcessRegister.show()
            }
        })


//        val textView: TextView = binding.textViewLogin
//        loginViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                // Respond to neutral button press
                dialog.dismiss()
            }
            .show()
    }

}