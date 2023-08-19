package pl.tapo24.ui.road.countryDrivingLicence

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.adapter.CountryDrivingLicenceAdapter
import pl.tapo24.databinding.FragmentCountryDrivingLicenceBinding


@AndroidEntryPoint
class CountryDrivingLicenceFragment: Fragment() {
    private var _binding: FragmentCountryDrivingLicenceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var viewModel: CountryDrivingLicenceViewModel


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(CountryDrivingLicenceViewModel::class.java)

        _binding = FragmentCountryDrivingLicenceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData()
//        binding.searchBox.setOnQueryTextListener(this)


        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CountryDrivingLicenceAdapter(viewModel.data.value.orEmpty(),viewModel.data.value.orEmpty(),viewModel)
        rv.adapter = viewModel.adapter
        binding.queryText.addTextChangedListener {
            viewModel.adapter.filter.filter(binding.queryText.text)
        }
        binding.rv.setOnTouchListener(OnTouchListener { v, event ->
            val inputManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken,0)
            false
        })



        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResult = it
            viewModel.adapter.notifyDataSetChanged()
        })
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



//    override fun onQueryTextSubmit(query: String?): Boolean {
//        return false
//    }
//
//    override fun onQueryTextChange(newText: String?): Boolean {
//        viewModel.adapter.filter.filter(newText)
//        return true
//    }

}