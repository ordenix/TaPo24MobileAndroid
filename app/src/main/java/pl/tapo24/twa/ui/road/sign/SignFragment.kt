package pl.tapo24.twa.ui.road.sign

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentSignBinding
import java.io.File

@AndroidEntryPoint
class SignFragment : Fragment() {

    private var _binding: FragmentSignBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SignViewModel::class.java)
        _binding = FragmentSignBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.A.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "A")
            )
        }
        setImage(binding.imageViewA,"sign_a/A-1.png")

        setImage(binding.imageViewB,"sign_b/B-1.png")
        setImage(binding.imageViewC,"sign_c/C-12.png")
        setImage(binding.imageViewD,"sign_d/D-34.png")
        setImage(binding.imageViewE,"sign_e/E-1.png")
        setImage(binding.imageViewF,"sign_f/F-11.png")
        setImage(binding.imageViewG,"sign_g/G-3.png")
        setImage(binding.imageViewP,"sign_p/P-16.png")
        setImage(binding.imageViewS,"sign_s/S-4.png")
        setImage(binding.imageViewT,"sign_t/T-1.png")
        setImage(binding.imageViewAtbt,"sign_atbt/BT-3.png")
        setImage(binding.imageViewrr,"sign_r/R-1.png")
        setImage(binding.imageViewW,"sign_w/W-5.png")

        binding.B.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "B")
            )
        }
        binding.C.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "C")
            )
        }
        binding.D.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "D")
            )
        }
        binding.E.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "E")
            )
        }
        binding.F.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "F")
            )
        }
        binding.G.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "G")
            )
        }
        binding.P.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "P")
            )
        }
        binding.S.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "S")
            )
        }
        binding.T.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "T")
            )
        }
        binding.rr.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "R")
            )
        }
        binding.W.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "W")
            )
        }
        binding.Atbt.setOnClickListener {view ->
            view.findNavController().navigate(
                R.id.action_nav_sign_to_nav_signList,
                bundleOf("signCategory" to "ATBT")
            )
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setImage(imView: ImageView, path: String) {

        val file: File = if (viewModel.isPublicStorage) {
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24Don'tDelete/$path")
        } else {
            File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), path)
        }
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.path)
            imView.setImageBitmap(bitmap)
        }
    }

}