package pl.tapo24.twa.ui.helpers.spb.viewer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentSpbViewerBinding
import pl.tapo24.twa.databinding.FragmentValidDocumentBinding
import pl.tapo24.twa.ui.helpers.spb.SpbChoseFragmentArgs
import pl.tapo24.twa.ui.story.StoryFragmentArgs
import pl.tapo24.twa.utils.UlListBuilder

@AndroidEntryPoint
class SpbViewerFragment : Fragment() {

    private var _binding: FragmentSpbViewerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SpbViewerViewModel
    private var title:String = ""


    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { SpbViewerFragmentArgs.fromBundle(it) }
        title = args!!.type

        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpbViewerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(SpbViewerViewModel::class.java)
        val args = arguments?.let { SpbViewerFragmentArgs.fromBundle(it) }
        viewModel.getData(args!!.type)


        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.mainTitle.text = it.type
                if (it.listsHaveInnerDivider == false) {
                    binding.divider10.visibility = View.GONE
                    binding.divider11.visibility = View.GONE
                    binding.divider12.visibility = View.GONE
                    binding.divider13.visibility = View.GONE

                }
                if (it.title1 != null) {
                    binding.title1.text = it.title1
                } else {
                    binding.title1.visibility = View.GONE
                }
                if (it.listsHaveBullet == true) {
                    binding.list1.text = UlListBuilder().getSpannableTextBullet(it.list1)
                } else {
                    binding.list1.text = UlListBuilder().getSpannableTextListWithoutBullet(it.list1)
                }
                if (it.title2 != null) {
                    binding.title2.text = it.title2
                    if (it.listsHaveBullet == true) {
                        binding.list2.text = UlListBuilder().getSpannableTextBullet(it.list2)
                    } else {
                        binding.list2.text = UlListBuilder().getSpannableTextListWithoutBullet(it.list2)
                    }
                } else {
                    binding.title2.visibility = View.GONE
                    binding.list2.visibility = View.GONE
                }
                if (it.title3 != null) {
                    binding.title3.text = it.title3
                    if (it.listsHaveBullet == true) {
                        binding.list3.text = UlListBuilder().getSpannableTextBullet(it.list3)
                    } else {
                        binding.list3.text = UlListBuilder().getSpannableTextListWithoutBullet(it.list3)
                    }
                } else {
                    binding.title3.visibility = View.GONE
                    binding.list3.visibility = View.GONE
                }
                if (it.title4 != null) {
                    binding.title4.text = it.title4
                    if (it.listsHaveBullet == true) {
                        binding.list4.text = UlListBuilder().getSpannableTextBullet(it.list4)
                    } else {
                        binding.list4.text = UlListBuilder().getSpannableTextListWithoutBullet(it.list4)
                    }
                } else {
                    binding.title4.visibility = View.GONE
                    binding.list4.visibility = View.GONE
                }
                if (it.title5 != null) {
                    binding.title5.text = it.title5
                    if (it.listsHaveBullet == true) {
                        binding.list5.text = UlListBuilder().getSpannableTextBullet(it.list5)
                    } else {
                        binding.list5.text = UlListBuilder().getSpannableTextListWithoutBullet(it.list5)
                    }
                } else {
                    binding.title5.visibility = View.GONE
                    binding.list5.visibility = View.GONE
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}