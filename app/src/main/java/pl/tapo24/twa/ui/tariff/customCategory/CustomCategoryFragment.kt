package pl.tapo24.twa.ui.tariff.customCategory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentCustomCategoryBinding

class CustomCategoryFragment : Fragment() {
    private var _binding: FragmentCustomCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CustomCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomCategoryBinding.inflate(inflater,container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(CustomCategoryViewModel::class.java)
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}