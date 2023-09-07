package pl.tapo24.twa.ui.helpers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.tapo24.twa.databinding.FragmentHelpersBinding

class HelpersFragment: Fragment() {
    private var _binding: FragmentHelpersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelHelpers =
            ViewModelProvider(this).get(HelpersViewModel::class.java)

        _binding = pl.tapo24.twa.databinding.FragmentHelpersBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textViewhelpers
//        viewModelHelpers.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}