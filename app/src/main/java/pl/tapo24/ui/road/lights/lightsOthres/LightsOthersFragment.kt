package pl.tapo24.ui.road.lights.lightsOthres

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.InvalidationTracker
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.R
import pl.tapo24.adapter.CountryDrivingLicenceAdapter
import pl.tapo24.adapter.LightsOthersAdapter
import pl.tapo24.databinding.FragmentLightsOthersBinding
import pl.tapo24.databinding.FragmentSettingBinding
import pl.tapo24.databinding.FragmentTowingBinding

@AndroidEntryPoint
class LightsOthersFragment : Fragment() {

    private var _binding: FragmentLightsOthersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LightsOthersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LightsOthersViewModel::class.java)
        _binding = FragmentLightsOthersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = LightsOthersAdapter(viewModel.data.value.orEmpty())
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