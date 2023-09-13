package pl.tapo24.twa.ui.road.sign.signList

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
import pl.tapo24.twa.adapter.SignListAdapter
import pl.tapo24.twa.databinding.FragmentSignListBinding
import pl.tapo24.twa.ui.road.sign.signDetails.SignDetailsFragmentArgs

@AndroidEntryPoint
class SignListFragment : Fragment() {

    private var _binding: FragmentSignListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignListViewModel
    var signCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { SignListFragmentArgs.fromBundle(it) }
        signCategory = args!!.signCategory
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(SignListViewModel::class.java)
        _binding = FragmentSignListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData(signCategory)
        val rv = binding.rv
        rv.layoutManager = GridLayoutManager(activity, 3)
        viewModel.adapter = container?.context?.let { SignListAdapter(viewModel.data.value.orEmpty(), it, viewModel) }!!
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })

        viewModel.adapter.onItemClick = {
            requireView().findNavController().navigate(
                R.id.action_nav_signList_to_nav_signDetails,
                bundleOf("signDetails" to it)
            )
        }


        return root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}