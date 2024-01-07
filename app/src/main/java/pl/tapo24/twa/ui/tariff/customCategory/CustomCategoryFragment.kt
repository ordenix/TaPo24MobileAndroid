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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.CustomCategoryAdapter
import pl.tapo24.twa.data.State
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

        val dialogConfirmDelete = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.areYouSureToDelete))
            .setPositiveButton("Nie") { dialog, which ->
                // Respond to positive button press
            }
            .setNegativeButton("Tak") { dialog, which ->
                // Respond to negative button press
                viewModel.deleteCustomCategory()
            }


        binding.addCategory.setOnClickListener {
            val viewDialogWithEditNameCustomCategory = layoutInflater.inflate(R.layout.dialog_custom_category_name, null)
            viewDialogWithEditNameCustomCategory.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.customCategoryName).setText("")
            val dialogAddCustomCategory = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.addCategory))
                .setView(viewDialogWithEditNameCustomCategory)
                .setPositiveButton("Dodaj") { dialog, which ->
                    // Respond to positive button press
                    val categoryName = viewDialogWithEditNameCustomCategory.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.customCategoryName).text.toString()
                    viewModel.addCustomCategory(categoryName)
                }
                .create()
            dialogAddCustomCategory.show()
        }
        val dialogInfo = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Info")
            .setPositiveButton("Ok") { dialog, which ->
                // Respond to positive button press
            }
            .create()
        viewModel.showInfoDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.showInfoDialog.value = false
                dialogInfo.setMessage(viewModel.infoDialogText)
                dialogInfo.show()
            }
        })
        val dialogError = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Błąd")
            .setPositiveButton("Ok") { dialog, which ->
                // Respond to positive button press
            }
            .create()
        viewModel.showErrorDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.showErrorDialog.value = false
                dialogError.setMessage(viewModel.errorDialogText + " \n" + "Jeżeli błąd się powtarza skontaktuj się z nami")
                dialogError.show()
            }
        })
        viewModel.showEditNameDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                val viewDialogWithEditNameCustomCategory = layoutInflater.inflate(R.layout.dialog_custom_category_name, null)
                viewModel.showEditNameDialog.value = false

                viewDialogWithEditNameCustomCategory.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.customCategoryName).setText(viewModel.customCategoryToEditName?.categoryName)
                val dialogEditCustomCategory = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.editCategoryName))
                    .setView(viewDialogWithEditNameCustomCategory)
                    .setPositiveButton("Zapisz") { dialog, which ->
                        // Respond to positive button press
                        val categoryName = viewDialogWithEditNameCustomCategory.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.customCategoryName).text.toString()
                        viewModel.changeNameCustomCategoryName(categoryName)
                    }
                    .setNegativeButton("Anuluj") { dialog, which ->
                        // Respond to negative button press
                    }
                    .create()
                dialogEditCustomCategory.show()
            }
        })

        val rv = binding.rvCustomCategory
        val itemTouchHelper = ItemTouchHelper(moveCallback)
        itemTouchHelper.attachToRecyclerView(rv)
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CustomCategoryAdapter(viewModel.customCategoryList.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.customCategoryList.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        State.showCustomOnTop.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.showCustomCategory.check(R.id.showOnTop)
            } else {
                binding.showCustomCategory.check(R.id.showOnBottom)
            }
        })
        binding.showOnTop.setOnClickListener {
            State.showCustomOnTop.value = true
            viewModel.saveShowParameters(true)
        }
        binding.showOnBottom.setOnClickListener {
            State.showCustomOnTop.value = false
            viewModel.saveShowParameters(false)
        }

        viewModel.showDeleteDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.showDeleteDialog.value = false
                dialogConfirmDelete.setTitle(getString(R.string.areYouSureToDelete)+" "+viewModel.customCategoryToDelete?.categoryName)
                dialogConfirmDelete.show()
            }
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
            viewModel.changeOrderCustomCategory()
        }

    }

}