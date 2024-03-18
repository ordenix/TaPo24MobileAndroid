package pl.tapo24.twa.ui.helpers.adrTable

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentAdrTableBinding

@AndroidEntryPoint
class AdrTableFragment : Fragment() {

    companion object {
        fun newInstance() = AdrTableFragment()
    }
    private var _binding: FragmentAdrTableBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: AdrTableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AdrTableViewModel::class.java)
        _binding = FragmentAdrTableBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.hinNumber.doOnTextChanged { text, start, before, count ->
            viewModel.getHinNumber(text.toString())
        }
        viewModel.hinNumber.observe(viewLifecycleOwner, Observer {
            binding.textHinNumber.text = it
        })

        binding.adrNumber.doOnTextChanged { text, start, before, count ->
            viewModel.getAdrNumber(text.toString())
        }
        viewModel.adrNumber.observe(viewLifecycleOwner, Observer {
            binding.textAdrNumber.text = it
        })


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}