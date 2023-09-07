package pl.tapo24.twa.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.data.State
import pl.tapo24.twa.databinding.FragmentGalleryBinding
@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
//    private val galleryViewModel: GalleryViewModel by viewModels<GalleryViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)


        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val btn: Button = binding.testbtn
        btn.setOnClickListener {
            galleryViewModel.test()
        }
        binding.button2.setOnClickListener {
            println(State.environmentType.description)
            println(State.enginesType.description)
            galleryViewModel.test2()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}