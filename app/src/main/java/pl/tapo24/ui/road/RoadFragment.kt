package pl.tapo24.ui.road

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pl.tapo24.R
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
            ViewModelProvider(this).get(RoadViewModel::class.java)

        _binding = FragmentRoadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.CategoryDrivingLicence.setOnClickListener {view->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_category_driving_licence
            )
        }

//        val textView: TextView = binding.textViewRoad
//        roadViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}