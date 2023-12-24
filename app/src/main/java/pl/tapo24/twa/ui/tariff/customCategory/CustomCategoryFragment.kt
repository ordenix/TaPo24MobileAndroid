package pl.tapo24.twa.ui.tariff.customCategory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.CustomCategoryAdapter
import pl.tapo24.twa.databinding.FragmentCustomCategoryBinding
import java.util.*


@AndroidEntryPoint
class CustomCategoryFragment : Fragment() {
    private var _binding: FragmentCustomCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CustomCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomCategoryBinding.inflate(inflater,container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(CustomCategoryViewModel::class.java)


        val rv = binding.rvCustomCategory
        val itemTouchHelper = ItemTouchHelper(moveCallback)
        itemTouchHelper.attachToRecyclerView(rv)
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CustomCategoryAdapter(viewModel.customCategoryList.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.customCategoryList.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val moveCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            viewModel.customCategoryList.value!!.let { Collections.swap(it, fromPosition, toPosition) }
            viewModel.adapter.notifyItemMoved(fromPosition, toPosition)


            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
        }

    }

}