package pl.tapo24.twa.ui.utils.navigation

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.NavAdapter
import pl.tapo24.twa.databinding.FragmentInnerNavigationBinding
import pl.tapo24.twa.ui.story.StoryFragmentArgs

class InnerNavigationFragment : Fragment() {

    private var _binding: FragmentInnerNavigationBinding? = null
    private val binding get() = _binding!!

    var title: String = ""

    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        super.onResume()
    }

    companion object {
        fun newInstance() = InnerNavigationFragment()
    }

    private lateinit var viewModel: InnerNavigationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { InnerNavigationFragmentArgs.fromBundle(it) }
        title = args!!.title

        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentOrientation = resources.configuration.orientation
        _binding = FragmentInnerNavigationBinding.inflate(inflater, container,false)

        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(InnerNavigationViewModel::class.java)
        val args = arguments?.let { InnerNavigationFragmentArgs.fromBundle(it) }
        viewModel.type = args!!.treeName

        val rv = binding.rv
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv.layoutManager = GridLayoutManager(activity,4)
        } else {

            rv.layoutManager = GridLayoutManager(activity,3)
        }

        when (viewModel.type) {
            "calculator" -> {
                viewModel.adapter = NavAdapter(NavCalc.values().map { it.navElement })
                rv.adapter = viewModel.adapter

            }
            else -> {}
        }


        viewModel.adapter.onActiveItemClick = { navElement ->
            if (navElement.navId != null) {
                requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
                view?.findNavController()?.navigate(
                    navElement.navId,
                    navElement?.listBundle
                )
            } else if (navElement.navHttpLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(navElement.navHttpLink))
                if (navElement.statsDescription != null) {
                    val firebaseAnalytics = Firebase.analytics
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(FirebaseAnalytics.Param.ITEM_NAME, navElement.statsDescription)
                    }
                }
                startActivity(intent)
            }
        }
        viewModel.adapter.onDeActiveItemClick = { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}