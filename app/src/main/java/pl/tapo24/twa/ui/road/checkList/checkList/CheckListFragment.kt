package pl.tapo24.twa.ui.road.checkList.checkList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.CheckListAdapter
import pl.tapo24.twa.databinding.FragmentCheckListBinding
import pl.tapo24.twa.ui.helpers.spb.viewer.SpbViewerFragmentArgs
import pl.tapo24.twa.utils.PdfOpenIntent

@AndroidEntryPoint
class CheckListFragment : Fragment() {

    companion object {
        fun newInstance() = CheckListFragment()
    }

    private var _binding: FragmentCheckListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CheckListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(CheckListViewModel::class.java)
        val args = arguments?.let { CheckListFragmentArgs.fromBundle(it) }
        viewModel.getCheckListByType(args!!.idList)
        viewModel.idList = args.idList
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CheckListAdapter(viewModel.items.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.items.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            binding.linearProgressIndicator2.setProgressCompat(it, true)
            binding.linearProgressIndicator22.setProgressCompat(it, true)
            binding.unselectAll.isEnabled = it != 0
        })
        binding.edit.setOnClickListener {
            viewModel.clickEditMode()
        }
        viewModel.isEditMode.observe(viewLifecycleOwner, Observer {
            if (it) {
                // in edit
                binding.restore.visibility = View.VISIBLE
                binding.edit.text = getString(R.string.done)
            } else {
                binding.restore.visibility = View.GONE
                binding.edit.text = getString(R.string.edit_list)
            }
        })
        binding.unselectAll.setOnClickListener {
            val dialogConfirmUnselectAll = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.are_you_sure_to_un_select_all))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.unselectAll()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .show()
        }
        binding.restore.setOnClickListener {
            val dialogConfirmRestoreAll = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.are_you_sure_to_restore_list))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.restoreList()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .show()
        }
        viewModel.invokeDeletedItem.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val dialogConfirmDelete = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.are_you_sure_to_delete_item))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.deleteItem(it, viewModel.invokeDeletedPosition)
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ -> }
                    .show()
            }
         })

        viewModel.itemShowPdf.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.itemShowPdf.value = null
                it.pathToPdf?.let { it1 -> PdfOpenIntent(requireContext()).openPDF(it1, viewModel.isPublicStorage) }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}