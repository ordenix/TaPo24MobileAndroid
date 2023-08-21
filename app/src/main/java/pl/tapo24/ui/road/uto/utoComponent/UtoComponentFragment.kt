package pl.tapo24.ui.road.uto.utoComponent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.R
import pl.tapo24.databinding.FragmentRoadBinding
import pl.tapo24.databinding.FragmentUtoComponentBinding
import pl.tapo24.ui.story.StoryFragmentArgs

@AndroidEntryPoint
class UtoComponentFragment : Fragment() {

    private var _binding: FragmentUtoComponentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: UtoComponentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(UtoComponentViewModel::class.java)
        _binding = FragmentUtoComponentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val args = arguments?.let { UtoComponentFragmentArgs.fromBundle(it) }
        viewModel.getData(args!!.id)

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it != null){
                binding.name.text = it.name
                binding.definition.text = it.definition
            }
        })

        binding.expandRights.setOnClickListener {
            viewModel.expandRights.value = !viewModel.expandRights.value!!
        }
        viewModel.expandRights.observe(viewLifecycleOwner, Observer {
            if (it){
                // expand
                binding.textExpandRights.text = "Zwiń"
                binding.textRights.visibility = View.VISIBLE
                binding.imageExpandRights.setImageResource(R.drawable.angles_up_solid)
            } else {
                //colapse
                binding.textExpandRights.text = "Rozwiń"
                binding.textRights.visibility = View.GONE
                binding.imageExpandRights.setImageResource(R.drawable.angles_down_solid)

            }
        })


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}