package pl.tapo24.ui.law

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.tapo24.databinding.FragmentLawBinding
import pl.tapo24.ui.tariff.TariffViewModel

class LawFragment: Fragment() {
    private var _binding: FragmentLawBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lawViewModel =
            ViewModelProvider(this).get(TariffViewModel::class.java)

        _binding = FragmentLawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textViewLaw
        lawViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}