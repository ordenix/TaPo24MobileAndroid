package pl.tapo24.ui.tariff

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.R
import pl.tapo24.adapter.QuerySuggestionAdapter
import pl.tapo24.adapter.TariffDataAdapter
import pl.tapo24.databinding.FragmentTariffBinding


@AndroidEntryPoint

class TariffFragment: Fragment() {
    private var _binding: FragmentTariffBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var viewModel: TariffViewModel

    private val dialogMore = DialogTariffMore()
    //private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dialog =AlertDialog.Builder(requireContext())
//            .show()

       // dialog.setContentView(R.layout.dialog_overlay_loading,)
        dialogMore.show(childFragmentManager, "More")

        viewModel =
            ViewModelProvider(this).get(TariffViewModel::class.java)

        _binding = FragmentTariffBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.startApp()
        val rv = binding.RvTariffData
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = TariffDataAdapter(viewModel.tariffData.value.orEmpty(),viewModel)
        rv. adapter = viewModel.adapter
        viewModel.tariffData.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
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
        searchView.inflateMenu(R.menu.menu_search)
//        val menu = root.findViewById<TextView>(R.menu.menu_search)
        searchView.setOnMenuItemClickListener { menuItem: MenuItem? ->
            println(menuItem)
            menuItem?.setIcon(android.R.drawable.arrow_up_float)
            true }
        editTestInSearchView.setOnEditorActionListener { v, actionId, event ->
            searchBar.text = searchView.text;
            searchView.hide();
            false


        }

        val rvSuggestion = binding.rvSuggestion

        //viewModel.getData()
        rvSuggestion.layoutManager = LinearLayoutManager(activity)
        viewModel.adapterSuggestion = QuerySuggestionAdapter(viewModel.suggestionData.value.orEmpty())
        rvSuggestion.adapter = viewModel.adapterSuggestion
//
        viewModel.suggestionData.observe(viewLifecycleOwner, Observer {
            viewModel.adapterSuggestion.items = it
            viewModel.adapterSuggestion.notifyDataSetChanged()
        })


        viewModel.adapter.onItemClick = {
            println(it.id)
        }

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

//        val textView: TextView = binding.textView2
//        tariffViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun startApp() {
    }



}