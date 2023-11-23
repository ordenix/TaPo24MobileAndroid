package pl.tapo24.twa.ui.login.register.unsubscribeAdv

import android.content.pm.ActivityInfo
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
import pl.tapo24.twa.databinding.FragmentUnsubscribeAdvBinding

@AndroidEntryPoint
class UnsubscribeAdvFragment : Fragment() {

    private var _binding: FragmentUnsubscribeAdvBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UnsubscribeAdvViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogWaiting = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Proszę czekać")
            .setMessage("Serwer pracuje nad obliczeniem toru lotu rakiety")
            .create()
        _binding = FragmentUnsubscribeAdvBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(UnsubscribeAdvViewModel::class.java)

        val args = arguments?.let { UnsubscribeAdvFragmentArgs.fromBundle(it) }

        viewModel.decodeUrl(args!!.url)
        dialogWaiting.show()
        viewModel.sendRequest()

        viewModel.responseStatus.observe(viewLifecycleOwner) {result ->
            result.onFailure {
                if (dialogWaiting.isShowing && !this.isStateSaved
                    && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_unsubscribeAdv_to_nav_error,
                    bundleOf("errorDesc" to it.message)
                )
            }
            result.onSuccess {
                if (dialogWaiting.isShowing && !this.isStateSaved
                    && !requireActivity().isFinishing && !requireActivity().isDestroyed) {
                    dialogWaiting.dismiss()
                }
                findNavController().navigate(
                    R.id.action_nav_unsubscribeAdv_to_nav_success,
                    bundleOf("successDesc" to it)
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