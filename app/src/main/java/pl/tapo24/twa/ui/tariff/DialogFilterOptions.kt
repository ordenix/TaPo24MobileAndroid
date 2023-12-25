package pl.tapo24.twa.ui.tariff

import android.R as r2
import pl.tapo24.twa.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.customCategory.CategoryDictionary
import pl.tapo24.twa.data.customCategory.DataCategory
import pl.tapo24.twa.databinding.DialogTariffFilterBinding


class DialogFilterOptions: DialogFragment() {

    private var _binding: DialogTariffFilterBinding? = null
    private val binding get() = _binding!!

    var onSelected: (value: DataCategory) -> Unit = {
        it
    }

    var onClickOnManage: () -> Unit = {

    }
var selectedCategory: DataCategory = CategoryDictionary.All.element

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogTariffFilterBinding.inflate(inflater,container,false)
        val root = binding.root


        if (State.premiumVersion) {
            binding.notifyAddCustomCategoryPremium.visibility = View.GONE
            binding.editCustomCategory.isEnabled = true

        } else {
            binding.notifyAddCustomCategoryPremium.visibility = View.VISIBLE
            binding.editCustomCategory.isEnabled = false

        }
        binding.editCustomCategory.setOnClickListener {
            onClickOnManage()
        }


        val listDataCategory = prepareListDataCategory()

        //val textToSet = listDataCategory.find { it.query == selectedValue }?.name ?: "Wszystkie wykroczenia"
        binding.button8.text = selectedCategory.name

        val items = prepareListToShow(listDataCategory)

        val listPopupWindow = ListPopupWindow(requireContext(), null, r2.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = binding.button8
        val adapter2 = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
        listPopupWindow.setAdapter(adapter2)

        listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            binding.button8.text = listDataCategory[id.toInt()].name

            listPopupWindow.dismiss()
            onSelected(listDataCategory[id.toInt()])
            dialog?.dismiss()

        }
        binding.button8.setOnClickListener { v: View? -> listPopupWindow.show() }
        return root
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }
    //236
    private fun prepareListDataCategory(): List<DataCategory> {
        val list: MutableList<DataCategory> = mutableListOf()
        for (category in CategoryDictionary.values()) {
            list.add(category.element)
        }
        return list
    }

    private fun prepareListToShow(list: List<DataCategory>): List<String> {
        val listToShow: MutableList<String> = mutableListOf()
        for (category in list) {
            listToShow.add(category.name)
        }
        return listToShow
    }

}