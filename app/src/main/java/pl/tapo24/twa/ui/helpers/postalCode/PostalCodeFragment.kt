package pl.tapo24.twa.ui.helpers.postalCode

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentPostalCodeBinding

class PostalCodeFragment : Fragment() {

    private var _binding: FragmentPostalCodeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PostalCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostalCodeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PostalCodeViewModel::class.java)
        val root = binding.root
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}