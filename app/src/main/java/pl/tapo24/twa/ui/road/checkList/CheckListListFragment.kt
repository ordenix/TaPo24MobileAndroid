package pl.tapo24.twa.ui.road.checkList

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.CheckListListAdapter
import pl.tapo24.twa.databinding.FragmentCheckListListBinding


@AndroidEntryPoint
class CheckListListFragment : Fragment() {
    private var _binding: FragmentCheckListListBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: CheckListListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentOrientation = resources.configuration.orientation
        _binding = FragmentCheckListListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(CheckListListViewModel::class.java)
        viewModel.getData()
        val rv = binding.rv
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv.layoutManager = GridLayoutManager(activity,3)
        } else {

            rv.layoutManager = GridLayoutManager(activity,2)
        }

        viewModel.adapter = CheckListListAdapter(viewModel.checkListAllType.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.checkListAllType.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        viewModel.selectedCheckListTypeId.observe(viewLifecycleOwner, Observer {
            if (it != 0 ) {
                viewModel.selectedCheckListTypeId.value = 0
                view?.findNavController()?.navigate(
                    R.id.action_nav_checkListList_to_nav_checkList,
                    bundleOf("idList" to it)
                )
            }
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}