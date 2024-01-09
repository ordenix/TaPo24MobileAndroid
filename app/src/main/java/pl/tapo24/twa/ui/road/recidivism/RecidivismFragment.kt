package pl.tapo24.twa.ui.road.recidivism

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.TariffDataAdapter
import pl.tapo24.twa.databinding.FragmentRecidivismBinding

@AndroidEntryPoint
class RecidivismFragment : Fragment() {

    private lateinit var viewModel: RecidivismViewModel
    private var _binding: FragmentRecidivismBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecidivismBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RecidivismViewModel::class.java)
        val root = binding.root
        viewModel.getData()

        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = TariffDataAdapter(viewModel.data.value.orEmpty(), null, requireContext())
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