package pl.tapo24.twa.ui.road.immunity.immunityPersonList

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.ImmunityPersonListAdapter
import pl.tapo24.twa.databinding.FragmentImmunityPersonListBinding

@AndroidEntryPoint
class ImmunityPersonListFragment : Fragment() {

    companion object {
        fun newInstance() = ImmunityPersonListFragment()
    }
    private var _binding: FragmentImmunityPersonListBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ImmunityPersonListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentOrientation = resources.configuration.orientation
        viewModel = ViewModelProvider(this).get(ImmunityPersonListViewModel::class.java)
        _binding = FragmentImmunityPersonListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rv = binding.rv
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv.layoutManager = GridLayoutManager(activity,3)
        } else {
            rv.layoutManager = GridLayoutManager(activity,2)
        }
        viewModel.adapter = ImmunityPersonListAdapter(viewModel.personList.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.personList.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filter.filter("")
            viewModel.adapter.notifyDataSetChanged()
        })

        binding.queryText.addTextChangedListener {
            viewModel.adapter.filter.filter(binding.queryText.text)
            viewModel.adapter.notifyDataSetChanged()
        }

        viewModel.selectedPerson.observe(viewLifecycleOwner, Observer{
            if (it != null) {
                viewModel.selectedPerson.value = null
                view?.findNavController()?.navigate(R.id.action_nav_immunityPersonList_to_nav_immunityList,
                    bundleOf("immunity" to it))
            }
        })




        return root
    }

}