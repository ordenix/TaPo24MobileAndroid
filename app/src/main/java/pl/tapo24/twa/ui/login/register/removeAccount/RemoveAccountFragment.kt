package pl.tapo24.twa.ui.login.register.removeAccount

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
import pl.tapo24.twa.databinding.FragmentRemoveAccountBinding


@AndroidEntryPoint
class RemoveAccountFragment : Fragment() {
    private var _binding: FragmentRemoveAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RemoveAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogWaiting = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Proszę czekać")
            .setMessage("Serwer pracuje nad obliczeniem toru lotu rakiety")
            .create()
        _binding = FragmentRemoveAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(RemoveAccountViewModel::class.java)

        val args = arguments?.let { RemoveAccountFragmentArgs.fromBundle(it) }
        viewModel.decodeUrl(args!!.url)
        viewModel.sendRequest()
        dialogWaiting.show()

        viewModel.responseStatus.observe(viewLifecycleOwner) {response->
            response.onSuccess {
                if (dialogWaiting.isShowing && !this.isStateSaved
                    && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_removeAccount_to_nav_success,
                    bundleOf("successDesc" to it)
                )
            }
            response.onFailure {
                if (dialogWaiting.isShowing && !this.isStateSaved
                    && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_removeAccount_to_nav_error,
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