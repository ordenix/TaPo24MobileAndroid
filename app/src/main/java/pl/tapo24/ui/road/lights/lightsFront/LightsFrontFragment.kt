package pl.tapo24.ui.road.lights.lightsFront

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.R
import pl.tapo24.adapter.LightsFrontAdapter
import pl.tapo24.adapter.LightsOthersAdapter
import pl.tapo24.databinding.FragmentLightsFrontBinding
import pl.tapo24.databinding.FragmentSettingBinding
import pl.tapo24.databinding.FragmentTowingBinding

@AndroidEntryPoint
class LightsFrontFragment : Fragment() {

    private var _binding: FragmentLightsFrontBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LightsFrontViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LightsFrontViewModel::class.java)
        _binding = FragmentLightsFrontBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = LightsFrontAdapter(viewModel.data.value.orEmpty())
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