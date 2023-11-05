package pl.tapo24.twa.ui.login.register.unsubscribeAdv

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentUnsubscribeAdvBinding

class UnsubscribeAdvFragment : Fragment() {

    private var _binding: FragmentUnsubscribeAdvBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UnsubscribeAdvViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnsubscribeAdvBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(UnsubscribeAdvViewModel::class.java)
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}