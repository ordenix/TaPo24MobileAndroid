package pl.tapo24.twa.ui.login.register.removeAccount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentRemoveAccountBinding

class RemoveAccountFragment : Fragment() {
    private var _binding: FragmentRemoveAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RemoveAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRemoveAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(RemoveAccountViewModel::class.java)
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}