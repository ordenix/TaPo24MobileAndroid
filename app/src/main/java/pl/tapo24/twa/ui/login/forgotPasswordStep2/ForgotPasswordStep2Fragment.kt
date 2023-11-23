package pl.tapo24.twa.ui.login.forgotPasswordStep2

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentForgotPasswordStep2Binding

@AndroidEntryPoint
class ForgotPasswordStep2Fragment : Fragment() {
    private var _binding: FragmentForgotPasswordStep2Binding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ForgotPasswordStep2ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogWaiting = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Proszę czekać")
            .setMessage("Serwer pracuje nad obliczeniem ilości cukru w cukrze")
            .create()
        _binding = FragmentForgotPasswordStep2Binding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(ForgotPasswordStep2ViewModel::class.java)
        val args = arguments?.let { ForgotPasswordStep2FragmentArgs.fromBundle(it) }
        viewModel.decodeUrl(args!!.url)
        binding.setNewPassword.setOnClickListener {
            val regex = Regex(pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[\\S\\d@$!%*?&]{8,}$")
            if (binding.rePassword.text.toString().isEmpty() && binding.password.text.toString().isEmpty()) {
                showDialog("Wprowadź hasło")
            } else if (binding.rePassword.text.toString() != binding.password.text.toString()) {
                showDialog("Wprowadzone hasła nie są takie same")
            } else if (!regex.matches(binding.password.text.toString())) {
                showDialog("Hasło musi mieć minimalną długość 8 znaków oraz zawierać 1 dużą i małą literę, liczbę, znak specjalny")
            } else {
                viewModel.sendRequest(binding.password.text.toString())
                dialogWaiting.show()
            }

        }
        viewModel.responseStatus.observe(viewLifecycleOwner) {result->
            result.onSuccess {
                if (dialogWaiting.isShowing && !this.isStateSaved
                    && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_forgotPasswordStep2_to_nav_success,
                    bundleOf("successDesc" to it)
                )
            }
            result.onFailure {
                if (dialogWaiting.isShowing && !this.isStateSaved
                    && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_forgotPasswordStep2_to_nav_error,
                    bundleOf("errorDesc" to it.message)
                )
            }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(message: String) {
        val dialogInfo = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Błąd")
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                // Respond to neutral button press
                dialog.dismiss()
            }
            .show()
    }

}