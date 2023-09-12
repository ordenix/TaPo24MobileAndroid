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
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentSignBinding
import java.io.File

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




        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setImage(imView: ImageView, path: String) {
        val file: File = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), path)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imView.setImageBitmap(bitmap)
        }
    }

}