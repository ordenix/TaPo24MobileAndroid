package pl.tapo24.ui.road.categoryDrivingLicence

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.adapter.CategoryDrivingLicenceAdapter
import pl.tapo24.databinding.FragmentCategoryDrivingLicenceBinding
import pl.tapo24.databinding.FragmentHelpersBinding
import pl.tapo24.ui.helpers.HelpersViewModel
@AndroidEntryPoint
class CategoryDrivingLicenceFragment: Fragment() {
    private var _binding: FragmentCategoryDrivingLicenceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(CategoryDrivingLicenceViewModel::class.java)

        _binding = pl.tapo24.databinding.FragmentCategoryDrivingLicenceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getData()
//        val textView: TextView = binding.textViewhelpers
//        viewModelHelpers.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val rv = binding.RvCategoryLicnece
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CategoryDrivingLicenceAdapter(viewModel.data.value.orEmpty(), viewModel)
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