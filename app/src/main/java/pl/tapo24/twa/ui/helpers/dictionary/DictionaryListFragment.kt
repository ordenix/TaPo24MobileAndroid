package pl.tapo24.twa.ui.helpers.dictionary

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentDictionaryListBinding

class DictionaryListFragment : Fragment() {

    private var _binding: FragmentDictionaryListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DictionaryListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDictionaryListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(DictionaryListViewModel::class.java)
        return root
    }



}