package pl.tapo24.twa.ui.road.sign.signDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.databinding.FragmentSignDetailsBinding

class SignDetailsFragment : Fragment() {

    private var _binding: FragmentSignDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SignDetailsViewModel::class.java)
        _binding = FragmentSignDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}