package pl.tapo24.twa.ui.road.sign.signDetails

import android.R
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.tapo24.twa.databinding.FragmentSignDetailsBinding
import java.io.File


class SignDetailsFragment : Fragment() {

    private var _binding: FragmentSignDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SignDetailsViewModel::class.java)
        _binding = FragmentSignDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val file: File = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test/a1.png")
        if (file.exists()) {
            val myBitmap = BitmapFactory.decodeFile(file.absolutePath)

            val myImage = binding.imageView16

            myImage.setImageBitmap(myBitmap)
        }


        return root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}