package pl.tapo24.twa.ui.law

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.adapter.PdfAdapter
import pl.tapo24.twa.databinding.FragmentLawBinding
import java.io.File


@AndroidEntryPoint

class LawFragment: Fragment() {
    private var _binding: FragmentLawBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(LawViewModel::class.java)

        _binding = FragmentLawBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = GridLayoutManager(activity,3)
        viewModel.adapter = PdfAdapter(viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        viewModel.adapter.onItemClick = {
            val file: File = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pdf/${it.fileName}")
            //  val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24/pdf/${it.type}/${it.fileName}")
            val target = Intent(Intent.ACTION_VIEW)

            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".provider",
                file
            )
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            target.setDataAndType(photoURI, "application/pdf")
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or  Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            val intent = Intent.createChooser(target, "Otwórz plik w: (nastąpi przekierowanie do zewnętrznej przeglądarki plików pdf")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Instruct the user to install a PDF reader here, or something
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}