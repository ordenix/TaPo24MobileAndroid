package pl.tapo24.twa.ui.settings.promoCode

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentPromoCodeBinding

@AndroidEntryPoint
class PromoCodeFragment : Fragment() {


    private var _binding: FragmentPromoCodeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PromoCodeFragment()
    }

    private lateinit var viewModel: PromoCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPromoCodeBinding.inflate(inflater, container, false)
        val root = binding.root
        viewModel = ViewModelProvider(this).get(PromoCodeViewModel::class.java)
        binding.sendCodeBtn.setOnClickListener {
            viewModel.enterPromoCode(binding.codeTIET.text.toString())
        }
        viewModel.showErrorDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Błąd")
                    .setMessage(viewModel.errorMessage.value)
                    .setPositiveButton("OK") { dialog, which ->
                                        dialog.dismiss()
                                    }
                    .show()
                viewModel.showErrorDialog.value = false
            }
        })
        viewModel.showSuccessDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Sukces")
                    .setMessage("Kod został zaakceptowany")
                    .setPositiveButton("OK") { dialog, which ->
                        view?.findNavController()?.navigate(R.id.nav_home)
                                        dialog.dismiss()
                                    }
                    .show()
                viewModel.showSuccessDialog.value = false
            }
        })
        viewModel.buttonBlocked.observe(viewLifecycleOwner, Observer {
            binding.sendCodeBtn.isEnabled = !it
        })

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}