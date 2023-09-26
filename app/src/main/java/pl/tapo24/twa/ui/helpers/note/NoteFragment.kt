package pl.tapo24.twa.ui.helpers.note

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentNoteBinding


@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null


    private val binding get() = _binding!!

    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        val root = binding.root
        viewModel.getData()

        binding.noteNumbers.doOnTextChanged { text, start, before, count ->
            viewModel.saveNumber(text.toString())

        }
        binding.noteString.doOnTextChanged { text, start, before, count ->
            viewModel.saveText(text.toString())
        }
        viewModel.text.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.noteString.setText(it)
            }
        })
        viewModel.number.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.noteNumbers.setText(it)
            }
        })

        return root

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}