package pl.tapo24.twa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.SelectableSearchableDialogAdapter
import pl.tapo24.twa.data.SelectableSearchableDialogElement

class SelectableSearchableDialog<T>: DialogFragment(){

    var itemList: List<T> = emptyList()
    var itemMapList: List<SelectableSearchableDialogElement> = emptyList()
    var tileField1: String? = null
    var tileField2: String? = null
    var tileField3: String? = null

    var title: String = ""
    var isMultiSelect: Boolean = false

    val selectedIndex: Int = 0
    val selectedItems: MutableLiveData<List<T>> = MutableLiveData()

    val selectedItem: MutableLiveData<T> = MutableLiveData()

    override fun show(manager: FragmentManager, tag: String?) {
        if (!this.isAdded) {
            super.show(manager, tag)
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (!this.isAdded) {
            return super.show(transaction, tag)
        }
        return 0

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(pl.tapo24.twa.R.layout.dialog_selected_searchable, container, false)
        val tileTextView = view.findViewById<TextView>(R.id.titleText)
        val input = view.findViewById<TextView>(R.id.queryText)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val backButton = view.findViewById<TextView>(R.id.back)
        val okButton = view.findViewById<TextView>(R.id.ok)
        if (itemMapList.isNotEmpty()) {
            val adapter = SelectableSearchableDialogAdapter(itemMapList, tileField1!!, tileField2, tileField3, isMultiSelect)
            //adapter.setHasStableIds(true)
            rv.layoutManager = LinearLayoutManager(activity)
            rv.adapter = adapter

            adapter.onItemClick = { item ->
                selectedItem.value = itemList[adapter.items.indexOf(item)]
                dismiss()
            }
            tileTextView.text = title
            input.addTextChangedListener {
                adapter.filter.filter(input.text)
            }
            backButton.setOnClickListener {
                dismiss()
            }
            if (isMultiSelect) {
                okButton.visibility = View.VISIBLE
                okButton.setOnClickListener {
                    selectedItems.value = adapter.items.filter { it.isSelected }.map { itemList[adapter.items.indexOf(it)] }
                   println(selectedItems.value)
                }
            } else {
                okButton.visibility = View.GONE
            }
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onPause() {
        this.dismiss()
        super.onPause()
    }







}