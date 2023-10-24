package pl.tapo24.twa.ui.statusForm.error

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {
    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ErrorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ErrorViewModel::class.java)
        val root: View = binding.root
        binding.goHome.setOnClickListener {
            findNavController().navigate(
                R.id.action_nav_error_to_nav_home
            )
        }
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}