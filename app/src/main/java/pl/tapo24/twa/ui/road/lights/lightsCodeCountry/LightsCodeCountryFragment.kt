package pl.tapo24.twa.ui.road.lights.lightsCodeCountry

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.adapter.LightsCodeCountryAdapter
import pl.tapo24.twa.databinding.FragmentLightsCodeCountryBinding

@AndroidEntryPoint
class LightsCodeCountryFragment : Fragment() {
    private var _binding: FragmentLightsCodeCountryBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: LightsCodeCountryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LightsCodeCountryViewModel::class.java)
        _binding = FragmentLightsCodeCountryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = LightsCodeCountryAdapter(viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}