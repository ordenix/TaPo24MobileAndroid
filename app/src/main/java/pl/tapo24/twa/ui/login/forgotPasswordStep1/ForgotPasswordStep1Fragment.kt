package pl.tapo24.twa.ui.login.forgotPasswordStep1

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
import pl.tapo24.twa.databinding.FragmentForgotPasswordStep1Binding

@AndroidEntryPoint
class ForgotPasswordStep1Fragment : Fragment() {

    private var _binding: FragmentForgotPasswordStep1Binding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ForgotPasswordStep1ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogWaiting = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Proszę czekać")
            .setMessage("Serwer pracuje nad obliczeniem masy atomowej atomu Helu")
            .create()
        _binding = FragmentForgotPasswordStep1Binding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(ForgotPasswordStep1ViewModel::class.java)

        binding.forgotButton.setOnClickListener {
            if (binding.forgotLogin.text.isNullOrEmpty()) {
                val dialogInfo = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Błąd")
                    .setMessage(getString(R.string.hintLoginOrEmail))
                    .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                        // Respond to neutral button press
                        dialog.dismiss()
                    }
                    .show()
            } else {
                viewModel.sendRequest(binding.forgotLogin.text.toString())
                dialogWaiting.show()
            }
        }
        viewModel.responseStatus.observe(viewLifecycleOwner) {result->
            result.onSuccess {
                if (dialogWaiting.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_forgotPasswordStep1_to_nav_success,
                    bundleOf("successDesc" to it)
                )
            }
            result.onFailure {
                if (dialogWaiting.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_forgotPasswordStep1_to_nav_error,
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

}