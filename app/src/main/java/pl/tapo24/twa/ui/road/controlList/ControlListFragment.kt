package pl.tapo24.twa.ui.road.controlList

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.ControlListAdapter
import pl.tapo24.twa.databinding.FragmentControlListBinding


@AndroidEntryPoint
class ControlListFragment : Fragment() {

    private var _binding: FragmentControlListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ControlListViewModel
    private val controlListDialog = ControlListDialog()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentControlListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(ControlListViewModel::class.java)
        viewModel.colorSpan1 = MaterialColors.getColor(root, R.attr.tapoSecondColor)
        viewModel.colorSpan2 = MaterialColors.getColor(root, R.attr.tapoBackgroundColor)
        if (viewModel.data.value.isNullOrEmpty()) {
            viewModel.getData()
        }
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = ControlListAdapter(viewModel.data.value.orEmpty(), viewModel.data.value.orEmpty(),viewModel )
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResult = it
            viewModel.adapter.notifyDataSetChanged()
        })
        viewModel.showDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                controlListDialog.priorityList = viewModel.priorityList
                controlListDialog.show(childFragmentManager,"Dialog")
                controlListDialog.onOkClick = {priorityLvl ->
                    viewModel.addStandard(viewModel.codeToInsert, priorityLvl)
                    viewModel.showDialog.value = false
                }
            }
        })
        binding.queryText.addTextChangedListener {
            viewModel.searchText = binding.queryText.text.toString()
            viewModel.adapter.filter.filter(binding.queryText.text)
        }
        binding.rv.setOnTouchListener(View.OnTouchListener { v, event ->
            val inputManager: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
            false
        })
        viewModel.emptyResults.observe(viewLifecycleOwner, Observer {
            if (it) {
                rv.visibility = View.GONE
            } else {
                rv.visibility = View.VISIBLE
            }
        })



        viewModel.standardList.observe(viewLifecycleOwner, Observer {
            if (it.size >= 1) {
                var string = "Lista usterek: "

                it.forEach {
                    string  = string+ it.code + " "
                }
                binding.topTextConrolList.text = string
                binding.topControlList.visibility = View.VISIBLE
                if (it.find { element -> element.priority == 3  } != null) {
                    binding.topControlList.backgroundTintList= ColorStateList.valueOf(ContextCompat.getColor(requireView().context,R.color.standard_red))
                } else if (it.find { element -> element.priority == 2  } != null) {
                    binding.topControlList.backgroundTintList= ColorStateList.valueOf(ContextCompat.getColor(requireView().context,R.color.standard_orange))
                } else if (it.find { element -> element.priority == 1  } != null) {
                    binding.topControlList.backgroundTintList= ColorStateList.valueOf(ContextCompat.getColor(requireView().context,R.color.standard_green))
                }
            } else {
                binding.topControlList.visibility = View.GONE
            }

        })

        return root
    }

    override fun onPause() {
        super.onPause()
        if (controlListDialog.isVisible) {
            controlListDialog.dismiss()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}