package pl.tapo24.twa.ui.helpers.spb

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.SpbChoseAdapter
import pl.tapo24.twa.databinding.FragmentSpbChoseBinding
import pl.tapo24.twa.databinding.FragmentValidDocumentBinding
import pl.tapo24.twa.ui.story.StoryFragmentArgs

@AndroidEntryPoint
class SpbChoseFragment : Fragment() {

    private var _binding: FragmentSpbChoseBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SpbChoseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpbChoseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(SpbChoseViewModel::class.java)

        val args = arguments?.let { SpbChoseFragmentArgs.fromBundle(it) }
        viewModel.isSpbFragment =  args!!.isSpb
        viewModel.getData()

        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = SpbChoseAdapter(viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        viewModel.adapter.onItemClick = {
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            if (it == "Przypadki użycia ŚPB") {
                findNavController().navigate(
                    R.id.action_nav_spbChose_self
                )

            } else {
                findNavController().navigate(
                    R.id.action_nav_spbChose_to_nav_spbViewer,
                    bundleOf("type" to it)
                )
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}