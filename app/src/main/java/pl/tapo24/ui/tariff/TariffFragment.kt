package pl.tapo24.ui.tariff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.tapo24.databinding.FragmentSlideshowBinding
import pl.tapo24.databinding.FragmentTariffBinding
import pl.tapo24.ui.slideshow.SlideshowViewModel

class TariffFragment: Fragment() {
    private var _binding: FragmentTariffBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tariffViewModel =
            ViewModelProvider(this).get(TariffViewModel::class.java)

        _binding = FragmentTariffBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textView2
        tariffViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}