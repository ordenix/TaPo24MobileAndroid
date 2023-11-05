package pl.tapo24.twa.ui.login.forgotPasswordStep2

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentForgotPasswordStep2Binding

class ForgotPasswordStep2Fragment : Fragment() {
    private var _binding: FragmentForgotPasswordStep2Binding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ForgotPasswordStep2ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordStep2Binding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(ForgotPasswordStep2ViewModel::class.java)
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}