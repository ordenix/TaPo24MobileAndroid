package pl.tapo24.twa.ui.road.status

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.adapter.StatusAdapter
import pl.tapo24.twa.databinding.FragmentStatusBinding

@AndroidEntryPoint
class StatusFragment : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        viewModel.getData()
        val root: View = binding.root

        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = StatusAdapter(viewModel, viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
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