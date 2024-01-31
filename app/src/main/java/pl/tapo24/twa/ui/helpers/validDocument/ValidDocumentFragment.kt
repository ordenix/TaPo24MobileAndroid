package pl.tapo24.twa.ui.helpers.validDocument

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.databinding.FragmentHelpersBinding
import pl.tapo24.twa.databinding.FragmentValidDocumentBinding
import pl.tapo24.twa.utils.PdfOpenIntent

@AndroidEntryPoint
class ValidDocumentFragment : Fragment() {

    private var _binding: FragmentValidDocumentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ValidDocumentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentValidDocumentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(ValidDocumentViewModel::class.java)

        binding.prado.setOnClickListener {
            openLink("https://www.consilium.europa.eu/prado/pl/search-by-document-country.html")
        }

        binding.id.setOnClickListener {
            PdfOpenIntent(requireContext()).openPDF("wer_do.pdf", viewModel.isPublicStorage)
        }

        binding.drivingLicenceUe.setOnClickListener {
            PdfOpenIntent(requireContext()).openPDF("wzoryPJ.pdf", viewModel.isPublicStorage)
        }

        binding.passport.setOnClickListener {
            PdfOpenIntent(requireContext()).openPDF("Paszport.pdf", viewModel.isPublicStorage)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

}