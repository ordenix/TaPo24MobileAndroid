package pl.tapo24.twa.ui.story

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentStoryBinding
import pl.tapo24.twa.utils.UlListBuilder
import pl.tapo24.twa.ui.story.StoryFragmentArgs

@AndroidEntryPoint
class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    var title: String = ""
    companion object {
        fun newInstance() = StoryFragment()
    }

    private lateinit var viewModel: StoryViewModel
    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { StoryFragmentArgs.fromBundle(it) }
        title = args!!.name

        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StoryViewModel::class.java)
        val args = arguments?.let { StoryFragmentArgs.fromBundle(it) }
        viewModel.storyType = args!!.storyType
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
      //  requireActivity().supportFragmentManager.
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        if (viewModel.data.value.isNullOrEmpty()) {
            viewModel.getData()
        }

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && !viewModel.dataInit) {
                viewModel.dataInit = true
                viewModel.setData(it[0].link)
            }
        })
        viewModel.currentStep.observe(viewLifecycleOwner, Observer {
            binding.stageTitle.text = it.stageTitle
            if (it.finalStep){
                binding.option1.visibility = View.GONE
                binding.option2.visibility = View.GONE
            } else {
                binding.option1.text = it.optionStage1
                binding.option2.text = it.optionStage2
                binding.option1.visibility = View.VISIBLE
                binding.option2.visibility = View.VISIBLE

            }
            if (viewModel.stepsArray.size > 1 ){
                binding.stepBack.visibility = View.VISIBLE
            } else {
                binding.stepBack.visibility = View.GONE

            }
            if (it.stageDescription.isEmpty()) {
                binding.stageDescription.visibility = View.GONE
            } else {
                binding.stageDescription.visibility = View.VISIBLE
                binding.stageDescription.text = it.stageDescription
            }
            if (it.stageDescriptionEnum.isEmpty()) {
                binding.stageDestriptionEnum.visibility = View.GONE
            } else {
                binding.stageDestriptionEnum.visibility = View.VISIBLE
                binding.stageDestriptionEnum.text = UlListBuilder().getSpannableTextBullet(it.stageDescriptionEnum, true)
            }
        })
        binding.option1.setOnClickListener{ view ->
            viewModel.currentStep.value?.linkOption1?.let { viewModel.setData(it) }
        }
        binding.option2.setOnClickListener{ view ->
            viewModel.currentStep.value?.linkOption2?.let { viewModel.setData(it) }
        }
        binding.stepBack.setOnClickListener { view ->
            viewModel.stepBack()
        }
        return root

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}