package pl.tapo24.ui.road

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.tapo24.databinding.FragmentRoadBinding
import pl.tapo24.ui.tariff.TariffViewModel

class RoadFragment: Fragment() {
    private var _binding: FragmentRoadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val roadViewModel =
            ViewModelProvider(this).get(TariffViewModel::class.java)

        _binding = FragmentRoadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textViewRoad
        roadViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}