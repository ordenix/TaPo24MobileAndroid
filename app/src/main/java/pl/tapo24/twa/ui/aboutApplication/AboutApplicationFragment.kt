package pl.tapo24.twa.ui.aboutApplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.tapo24.twa.data.State
import pl.tapo24.twa.databinding.FragmentAboutApplicationBinding

class AboutApplicationFragment: Fragment() {
    private var _binding: FragmentAboutApplicationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelAboutApplication =
            ViewModelProvider(this).get(AboutApplicationViewModel::class.java)

        _binding = pl.tapo24.twa.databinding.FragmentAboutApplicationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.uid.text = State.uid
        binding.versionCode.text = State.versionCode.toString()
        binding.versionName.text = State.versionName

//        val textView: TextView = binding.textViewAboutApp
//        viewModelAboutApplication.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}