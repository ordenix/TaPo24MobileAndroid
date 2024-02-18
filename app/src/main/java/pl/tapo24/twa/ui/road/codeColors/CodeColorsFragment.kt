package pl.tapo24.twa.ui.road.codeColors

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentCodeColorsBinding

class CodeColorsFragment : Fragment() {

    companion object {
        fun newInstance() = CodeColorsFragment()
    }
    private var _binding: FragmentCodeColorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CodeColorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodeColorsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(CodeColorsViewModel::class.java)

        binding.white.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "0", "codeName" to "BIAŁY"))
        }
        binding.red77.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "77", "codeName" to "CZERWONY"))
        }
        binding.red88.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "88", "codeName" to "CZERWONY"))
        }
        binding.red99.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "99", "codeName" to "CZERWONY"))
        }

        binding.green11.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "11", "codeName" to "ZIELONY"))
        }
        binding.green22.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "22", "codeName" to "ZIELONY"))
        }
        binding.yellow.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "33", "codeName" to "ŻÓŁTY"))
        }
        binding.blue44.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "44", "codeName" to "NIEBIESKI"))
        }
        binding.blue144.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "144", "codeName" to "NIEBIESKI"))
        }

        binding.blue55.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "55", "codeName" to "NIEBIESKI"))
        }
        binding.blue155.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "155", "codeName" to "NIEBIESKI"))
        }
        binding.blue66.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "66", "codeName" to "NIEBIESKI"))
        }
        binding.orange.setOnClickListener {
            findNavController().navigate(R.id.action_nav_codeColors_to_nav_codeColorsViewer,
                bundleOf("code" to "999", "codeName" to "POMARAŃCZOWY"))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}