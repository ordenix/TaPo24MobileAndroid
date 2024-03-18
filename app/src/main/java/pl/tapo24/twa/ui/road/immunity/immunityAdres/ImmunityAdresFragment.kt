package pl.tapo24.twa.ui.road.immunity.immunityAdres

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentImmunityAdresBinding

class ImmunityAdresFragment : Fragment() {

    companion object {
        fun newInstance() = ImmunityAdresFragment()
    }

    private var _binding: FragmentImmunityAdresBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ImmunityAdresViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ImmunityAdresViewModel::class.java)
        _binding = FragmentImmunityAdresBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}