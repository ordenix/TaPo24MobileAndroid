package pl.tapo24.ui.road.controlList

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
import pl.tapo24.adapter.ControlListAdapter
import pl.tapo24.databinding.FragmentAlcoholBinding
import pl.tapo24.databinding.FragmentControlListBinding

@AndroidEntryPoint
class ControlListFragment : Fragment() {

    private var _binding: FragmentControlListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ControlListViewModel
    private val controlListDialog = ControlListDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentControlListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(ControlListViewModel::class.java)

        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = ControlListAdapter(viewModel.data.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        // TODO: add clicked on add button and sum control fallout's
        controlListDialog.show(childFragmentManager,"Dialog")


        return root
    }

    override fun onPause() {
        controlListDialog.dismiss()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}