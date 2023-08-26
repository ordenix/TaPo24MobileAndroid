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
import pl.tapo24.utils.UlListBuilder

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

                binding.textRights.text = UlListBuilder().getTextNumerator(it.rights)
                binding.textWhereAllowed.text = UlListBuilder().getTextNumerator(it.whereAllowed)
                binding.textSpeed.text = UlListBuilder().getTextNumerator(it.speed)
                binding.textProhibition.text = UlListBuilder().getTextNumerator(it.prohibition)
                binding.textBehavior.text = UlListBuilder().getTextNumerator(it.behavior)
                binding.textStop.text = UlListBuilder().getTextNumerator(it.stop)

            }
        })
        binding.expandAll.setOnClickListener {
            viewModel.expandRights.value = true
            viewModel.expandWhereAllowed.value = true
            viewModel.expandSpeed.value = true
            viewModel.expandProhibition.value = true
            viewModel.expandBehavior.value = true
            viewModel.expandStop.value = true

        }

        binding.expandRights.setOnClickListener {
            viewModel.expandRights.value = !viewModel.expandRights.value!!
        }

        binding.expandWhereAllowed.setOnClickListener {
            viewModel.expandWhereAllowed.value = !viewModel.expandWhereAllowed.value!!
        }
        binding.expandSpeed.setOnClickListener {
            viewModel.expandSpeed.value = !viewModel.expandSpeed.value!!
        }
        binding.expandProhibition.setOnClickListener {
            viewModel.expandProhibition.value = !viewModel.expandProhibition.value!!
        }
        binding.expandBehavior .setOnClickListener {
            viewModel.expandBehavior.value = !viewModel.expandBehavior.value!!
        }
        binding.expandStop.setOnClickListener {
            viewModel.expandStop.value = !viewModel.expandStop.value!!
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
        //
        viewModel.expandWhereAllowed.observe(viewLifecycleOwner, Observer {
            if (it){
                // expand
                binding.textExpandWhereAllowed.text = "Zwiń"
                binding.textWhereAllowed.visibility = View.VISIBLE
                binding.imageExpandWhereAllowed.setImageResource(R.drawable.angles_up_solid)
            } else {
                //colapse
                binding.textExpandWhereAllowed.text = "Rozwiń"
                binding.textWhereAllowed.visibility = View.GONE
                binding.imageExpandWhereAllowed.setImageResource(R.drawable.angles_down_solid)

            }
        })
        viewModel.expandSpeed.observe(viewLifecycleOwner, Observer {
            if (it){
                // expand
                binding.textExpandSpeed.text = "Zwiń"
                binding.textSpeed.visibility = View.VISIBLE
                binding.imageExpandSpeed.setImageResource(R.drawable.angles_up_solid)
            } else {
                //colapse
                binding.textExpandSpeed.text = "Rozwiń"
                binding.textSpeed.visibility = View.GONE
                binding.imageExpandSpeed.setImageResource(R.drawable.angles_down_solid)

            }
        })
        viewModel.expandProhibition.observe(viewLifecycleOwner, Observer {
            if (it){
                // expand
                binding.textExpandProhibition.text = "Zwiń"
                binding.textProhibition.visibility = View.VISIBLE
                binding.imageExpandProhibition.setImageResource(R.drawable.angles_up_solid)
            } else {
                //colapse
                binding.textExpandProhibition.text = "Rozwiń"
                binding.textProhibition.visibility = View.GONE
                binding.imageExpandProhibition.setImageResource(R.drawable.angles_down_solid)

            }
        })
        viewModel.expandBehavior.observe(viewLifecycleOwner, Observer {
            if (it){
                // expand
                binding.textExpandBehavior.text = "Zwiń"
                binding.textBehavior.visibility = View.VISIBLE
                binding.imageExpandBehavior.setImageResource(R.drawable.angles_up_solid)
            } else {
                //colapse
                binding.textExpandBehavior.text = "Rozwiń"
                binding.textBehavior.visibility = View.GONE
                binding.imageExpandBehavior.setImageResource(R.drawable.angles_down_solid)

            }
        })
        viewModel.expandStop.observe(viewLifecycleOwner, Observer {
            if (it){
                // expand
                binding.textExpandStop.text = "Zwiń"
                binding.textStop.visibility = View.VISIBLE
                binding.imageExpandStop.setImageResource(R.drawable.angles_up_solid)
            } else {
                //colapse
                binding.textExpandStop.text = "Rozwiń"
                binding.textStop.visibility = View.GONE
                binding.imageExpandStop.setImageResource(R.drawable.angles_down_solid)

            }
        })


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}