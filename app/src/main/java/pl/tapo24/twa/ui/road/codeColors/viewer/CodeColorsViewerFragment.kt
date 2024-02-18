package pl.tapo24.twa.ui.road.codeColors.viewer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentCodeColorsViewerBinding
import pl.tapo24.twa.ui.pdfFileView.PdfFileViewFragmentArgs
import pl.tapo24.twa.utils.UlListBuilder

@AndroidEntryPoint
class CodeColorsViewerFragment : Fragment() {

    companion object {
        fun newInstance() = CodeColorsViewerFragment()
    }

    private var _binding: FragmentCodeColorsViewerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CodeColorsViewerViewModel
    private var title = "KOD"
    private var code = "00"

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { CodeColorsViewerFragmentArgs.fromBundle(it) }
        title = "${args!!.codeName} ${args!!.code}"
        code = args!!.code
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodeColorsViewerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(CodeColorsViewerViewModel::class.java)

        viewModel.getData(code)
        viewModel.data.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.codeNameTitle.text = "${it.codeName} ${it.code}"
                binding.mean.text = it.mean
                if (it.remarks.isNotEmpty()) {
                    binding.remarks.text = it.remarks
                } else {
                    binding.remarks.visibility = View.GONE
                }
                binding.toDo.text = UlListBuilder().getSpannableTextBulletFromCustomText(it.toDo)
            }
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        super.onResume()
    }

}