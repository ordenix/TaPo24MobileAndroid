package pl.tapo24.twa.ui.login.register.accountActivate

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentAccountActivateBinding

class AccountActivateFragment : Fragment() {
    private var _binding: FragmentAccountActivateBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountActivateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountActivateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(AccountActivateViewModel::class.java)
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}