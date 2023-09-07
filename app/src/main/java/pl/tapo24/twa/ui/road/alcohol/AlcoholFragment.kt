package pl.tapo24.twa.ui.road.alcohol

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentAlcoholBinding

class AlcoholFragment : Fragment() {

    companion object {
        fun newInstance() = AlcoholFragment()
    }

    private var _binding: FragmentAlcoholBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AlcoholViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlcoholBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(AlcoholViewModel::class.java)

        binding.onHere.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_alcohol_to_nav_story,
                bundleOf("name" to "Badany na miejscu","storyType" to "alkohol")
            )
        }

        binding.farFromHere.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_alcohol_to_nav_story,
                bundleOf("name" to "Badany na oddalił się","storyType" to "alkohol_far")
            )
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}