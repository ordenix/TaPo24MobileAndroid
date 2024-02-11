package pl.tapo24.twa.ui.tariff

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.CustomCategoryMapAdapter
import pl.tapo24.twa.adapter.QuerySuggestionAdapter
import pl.tapo24.twa.adapter.TariffDataAdapter
import pl.tapo24.twa.data.EnvironmentType
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.customCategory.CategoryToMap
import pl.tapo24.twa.databinding.FragmentTariffBinding
import java.util.*


@AndroidEntryPoint

class TariffFragment: Fragment() {
    private var _binding: FragmentTariffBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var viewModel: TariffViewModel

    private val dialogMore = DialogTariffMore()
    private val dialogTariffMapCustomCategory = DialogTariffMapCustomCategory()
    private val dialogFilterOptions = DialogFilterOptions()
    //private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dialog =AlertDialog.Builder(requireContext())
//            .show()

       // dialog.setContentView(R.layout.dialog_overlay_loading,)
        var dataSize = 0
        var showItem = 6
        viewModel =
            ViewModelProvider(this).get(TariffViewModel::class.java)
        _binding = FragmentTariffBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.startApp()
        val rv = binding.RvTariffData
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = TariffDataAdapter(viewModel.tariffData.value.orEmpty(),viewModel,requireContext())
        rv. adapter = viewModel.adapter
        viewModel.tariffData.observe(viewLifecycleOwner, Observer {
            showItem = if (it.size > 6) {
                6
            } else {
                it.size
            }
            dataSize = it.size
            viewModel.adapter.items = it.take(showItem)
            viewModel.adapter.notifyDataSetChanged()
        })
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        viewModel.checkFavourite.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (viewModel.queryTextInSearchBar.value!!.isEmpty()) {
                    itemTouchHelper.attachToRecyclerView(rv)
                } else {
                    itemTouchHelper.attachToRecyclerView(null)
                }

                binding.favsIcon.setImageResource(resources.getIdentifier("star_solid","drawable","pl.tapo24.twa"))
                Snackbar.make(binding.root, getString(R.string.onlyFavsNotification), Snackbar.LENGTH_LONG)
                    .show()
            } else {
                itemTouchHelper.attachToRecyclerView(null)
                binding.favsIcon.setImageResource(resources.getIdentifier("star_regular","drawable","pl.tapo24.twa"))
//                Snackbar.make(binding.root, getString(R.string.allNotification), Snackbar.LENGTH_LONG)
//                    .show()
            }
        })
        binding.favsIcon.setOnClickListener {
            viewModel.changeFavState()
        }
        binding.moreFilter.setOnClickListener {
            dialogFilterOptions.selectedCategory = viewModel.categoryValue
            dialogFilterOptions.show(childFragmentManager, "DialogFilter")
            dialogFilterOptions.onSelected = { it ->
                viewModel.categoryValue = it
                viewModel.getDataOnCategoryChanged()
            }
//            Snackbar.make(binding.root, getString(R.string.not_implemented), Snackbar.LENGTH_LONG)
//                .show()
        }
        dialogFilterOptions.onClickOnManage = {
            findNavController().navigate(R.id.action_nav_tariff_to_nav_customCategory)
        }
//        rv.getViewTreeObserver().addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                //dialog.dismiss()
//                println("ACTIONNN")
//                rv.getViewTreeObserver().removeOnGlobalLayoutListener(this)
//            }
//        })
        //dialog.dismiss()
//        viewModel.tariffData.observe(viewLifecycleOwner, Observer{
//            viewModel.adapter.items = it
//            viewModel.adapter.notifyDataSetChanged()
//
//        })

        binding.nView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    var maxItem = 0
                    if (State.environmentType == EnvironmentType.Master) {
                        maxItem = State.maxVisibleItem
                    } else {
                        maxItem = viewModel.tariffData.value?.size ?: 0
                    }
                    if (viewModel.adapter.items.size < dataSize && viewModel.adapter.items.size < maxItem) {
                        showItem += 1
                        viewModel.adapter.items =
                            viewModel.tariffData.value!!.take(showItem)
                        viewModel.adapter.notifyItemInserted(viewModel.adapter.items.size)
                    }
                    //code to fetch more data for endless scrolling
//                    Snackbar.make(binding.root, getString(R.string.not_implemented), Snackbar.LENGTH_LONG)
//                        .show()
                }
            }
        } as NestedScrollView.OnScrollChangeListener)
        ///// search sugestion section
        val searchBar = binding.searchBar
        val searchView = binding.searchView
        searchView.setupWithSearchBar(searchBar)
        val editTestInSearchView = searchView.editText
        //editTestInSearchView.back
//searchView.setOn
        editTestInSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                viewModel.sendQuerySuggestion(s.toString())
            }
        })
        searchView.inflateMenu(R.menu.menu_search) //inner menu
//        val menu = root.findViewById<TextView>(R.menu.menu_search)
        searchView.setOnMenuItemClickListener { menuItem: MenuItem? ->
            if (menuItem != null) {
                if (menuItem.itemId == R.id.info_tariff) {
                    val dialogHelp = DialogTariffHelp()
                    dialogHelp.show(childFragmentManager,"Help")
                }
            }
//            println(menuItem)
//            menuItem?.setIcon(android.R.drawable.arrow_up_float)
            true }
        editTestInSearchView.setOnEditorActionListener { v, actionId, event ->
            //searchBar.text = searchView.text;
            viewModel.queryTextInSearchBar.value = searchView.text.toString()
            searchView.hide();
            false


        }
        binding.network.setOnClickListener {
            Snackbar.make(binding.root, getString(R.string.offline_work), Snackbar.LENGTH_LONG)
                .show()
        }

        val rvSuggestion = binding.rvSuggestion

        //viewModel.getData()
        rvSuggestion.layoutManager = LinearLayoutManager(activity)
        viewModel.adapterSuggestion = QuerySuggestionAdapter(viewModel.suggestionData.value.orEmpty(), viewModel)
        rvSuggestion.adapter = viewModel.adapterSuggestion
//
        viewModel.suggestionData.observe(viewLifecycleOwner, Observer {
            viewModel.adapterSuggestion.items = it
            viewModel.adapterSuggestion.notifyDataSetChanged()
        })


        viewModel.adapter.onItemClick = {
            println(it.id)
        }
        viewModel.showDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                showDialog()
            }

        })
        viewModel.queryTextInSearchBar.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                itemTouchHelper.attachToRecyclerView(null)
            } else {
                if (viewModel.checkFavourite.value == true) {
                    itemTouchHelper.attachToRecyclerView(rv)
                }
            }
            searchBar.text = it
            viewModel.searchTariffData(it)
            // ask viewModel for data
        })
        viewModel.clickedOnSuggestion.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                viewModel.queryTextInSearchBar.value = it
                searchView.hide();
                viewModel.clickedOnSuggestion.value = ""
            }

        })
        if (State.internetStatus.value == NetworkTypes.None) {
            Snackbar.make(binding.root, getString(R.string.offline_work), Snackbar.LENGTH_LONG)
                .show()
            binding.network.visibility = View.VISIBLE
        }
        // viewModel.mapListCustomCategory.value = listOf(CategoryToMap(true,1,"tesfdssdfsdfsdt1"),CategoryToMap(false,1,"test2"),CategoryToMap(false,1,"test3"))
        viewModel.adapterCustomCategoryMap = CustomCategoryMapAdapter(viewModel.mapListCustomCategory.value.orEmpty(),viewModel)
        viewModel.showEditMapDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.showEditMapDialog.value = false
                dialogTariffMapCustomCategory.tariffViewModel = viewModel
                dialogTariffMapCustomCategory.show(childFragmentManager, "CustomCategoryMap")
            }
        })


//        val ssss = binding.spinner

//        val item = arrayOf("Carrot", "Corn", "Cucumber", "Broccoli", "Radish")
//        var list = ArrayList<String>()
//        val adapter1 = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, item)
//        ssss.setAdapter(adapter1)
//        ssss.performClick()
//        ssss.visibility = VISIBLE
//        ssss.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                println("SSSSSSSSSS")
//                println(position)
//            }
//
//        }

//        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                // in here you can do logic when backPress is clicked
//                println("BACK PRES")
//                super.
//            }
//        })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        if (dialogFilterOptions.isVisible) {
            dialogFilterOptions.dismiss()
        }
        if (dialogMore.isVisible) {
            dialogMore.dismiss()
            viewModel.showDialogRenew = true
        }

    }

    override fun onResume() {
        super.onResume()
        if (viewModel.showDialogRenew) {
            showDialog()
            viewModel.showDialogRenew = false
        }

    }
    private fun showDialog() {
        dialogMore.item = viewModel.itemToDialog
        dialogMore.position = viewModel.positionToDialog
        dialogMore.show(childFragmentManager, "More")
        dialogMore.onAddFavClick = {
            viewModel.itemToDialog?.let { it1 -> viewModel.clickOnFavorites(it1, viewModel.positionToDialog) }
        }
        dialogMore.editMapClick = {
            viewModel.getListForDialogMap(viewModel.itemToDialog?.id ?: "")
            viewModel.showEditMapDialog.value = true
        }
        dialogMore.closeClick = {
            viewModel.showDialog.value = false
        }
    }


    var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
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
            viewModel.tariffDataAll.value!!.let { Collections.swap(it, fromPosition, toPosition) }
            viewModel.adapter.notifyItemMoved(fromPosition, toPosition)
            //viewModel.adapter.

//            viewModel.adapter.notifyItemChanged(fromPosition)
//            viewModel.adapter.notifyItemChanged(toPosition)
//            if (fromPosition < toPosition) {
//                viewModel.adapter.notifyItemRangeChanged(
//                    fromPosition,
//                    (toPosition - fromPosition) + 1
//                )
//            } else {
//                viewModel.adapter.notifyItemRangeChanged(
//                    toPosition,
//                    (fromPosition - toPosition) + 1
//                )
//            }



            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewModel.saveShiftedItems()
        }

    }

}