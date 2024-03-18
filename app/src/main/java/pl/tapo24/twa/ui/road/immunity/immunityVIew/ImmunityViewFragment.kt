package pl.tapo24.twa.ui.road.immunity.immunityVIew

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentImmunityViewBinding
import pl.tapo24.twa.ui.story.StoryFragmentArgs
import pl.tapo24.twa.utils.UlListBuilder

class ImmunityViewFragment : Fragment() {

    companion object {
        fun newInstance() = ImmunityViewFragment()
    }

    private var _binding: FragmentImmunityViewBinding? = null
    var title: String = ""

    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { ImmunityViewFragmentArgs.fromBundle(it) }
        title = args!!.condition

        super.onCreate(savedInstanceState)
    }

    private val binding get() = _binding!!
    private lateinit var viewModel: ImmunityViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ImmunityViewViewModel::class.java)
        _binding = FragmentImmunityViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        val args = arguments?.let { ImmunityViewFragmentArgs.fromBundle(it) }
        binding.condition.text = args!!.condition
        binding.list.text = UlListBuilder().getSpannableTextListWithoutBullet(args.list, true)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}