package pl.tapo24.ui.road.controlList

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.R
import pl.tapo24.adapter.ControlListAdapter
import pl.tapo24.databinding.FragmentControlListBinding


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


//        val value = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
//
//        // start activity
//
//        // start activity
//        startActivity(intent)


//        val request = DownloadManager.Request(Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"))
//        request.setTitle("fileName")
//        request.setMimeType("application/pdf")
//        request.allowScanningByMediaScanner()
//        request.setAllowedOverMetered(true)
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AldoFiles/fileName")
//        val downloadManager =
//            requireContext().getSystemService(Context.DOWNLOAD_SERVICE)  as DownloadManager?
//        downloadManager!!.enqueue(request)


        viewModel = ViewModelProvider(this).get(ControlListViewModel::class.java)
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