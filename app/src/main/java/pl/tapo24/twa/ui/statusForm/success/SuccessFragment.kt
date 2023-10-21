package pl.tapo24.twa.ui.statusForm.success

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment() {

    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SuccessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SuccessViewModel::class.java)

        val root: View = binding.root

        val args = arguments?.let { SuccessFragmentArgs.fromBundle(it) }
        binding.description.text = args!!.successDesc

        binding.goHome.setOnClickListener {
            findNavController().navigate(
                R.id.action_nav_success_to_nav_home
            )
        }
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}