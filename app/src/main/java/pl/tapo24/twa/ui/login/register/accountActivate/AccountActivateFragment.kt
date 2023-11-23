package pl.tapo24.twa.ui.login.register.accountActivate

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
import pl.tapo24.twa.databinding.FragmentAccountActivateBinding
import pl.tapo24.twa.exceptions.HttpMessage

@AndroidEntryPoint
class AccountActivateFragment : Fragment() {
    private var _binding: FragmentAccountActivateBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: AccountActivateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountActivateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(AccountActivateViewModel::class.java)
        val dialogWaiting = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Proszę czekać")
            .setMessage("Serwer pracuje nad obliczeniem toru lotu rakiety")
            .create()
        val dialogRegenToken = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Błąd")
            .setMessage("Token utracił ważność lub jest błędny.")
            .setCancelable(false)
            .setPositiveButton("Wygeneruj nowy") { dialog, which ->
                dialogWaiting.show()
                viewModel.regenerateToken()

            }
            .create()
        val dialogAccountIsActivated = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Błąd")
            .setMessage("Konto jest już aktywowane")
            .setCancelable(false)
            .setPositiveButton("Zamknij") { dialog, which ->
                findNavController().navigate(R.id.nav_home)

            }
            .create()
        val args = arguments?.let { AccountActivateFragmentArgs.fromBundle(it) }


        viewModel.decodeUrl(args!!.url)
        dialogWaiting.show()
        viewModel.sendRequest()
        viewModel.status.observe(viewLifecycleOwner){ result->
            result.onSuccess {
                if (dialogWaiting.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_accountActivate_to_nav_success,
                    bundleOf("successDesc" to it)
                )
            }
            result.onFailure {
                if (dialogWaiting.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                if (it.message == HttpMessage.TokenExpired.message) {
                    viewModel.checkAccountActivated()
                } else {
                    findNavController().navigate(
                        R.id.action_nav_accountActivate_to_nav_error,
                        bundleOf("errorDesc" to it.message)
                    )
                }

            }
        }
        viewModel.statusCheckAccountActivate.observe(viewLifecycleOwner) { result->
            result.onSuccess { dialogAccountIsActivated.show() }
            result.onFailure {
                if (it.message == HttpMessage.UserNotHaveActivateAccount.message) {
                    dialogRegenToken.show()
                } else {
                    findNavController().navigate(
                        R.id.action_nav_accountActivate_to_nav_error,
                        bundleOf("errorDesc" to it.message)
                    )
                }
            }

        }
        viewModel.statusRegenerateToken.observe(viewLifecycleOwner){result->
            result.onSuccess {
                if (dialogRegenToken.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogRegenToken.dismiss()
                }
                if (dialogWaiting.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_accountActivate_to_nav_success,
                    bundleOf("successDesc" to it)
                ) }
            result.onFailure {
                if (dialogRegenToken.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogRegenToken.dismiss()
                }
                if (dialogWaiting.isShowing && !this.isStateSaved && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                R.id.action_nav_accountActivate_to_nav_error,
                bundleOf("errorDesc" to it.message)
            ) }
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}