package pl.tapo24.twa.ui.helpers

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import pl.tapo24.twa.adapter.NavAdapter
import pl.tapo24.twa.ui.utils.navigation.NavHelpers
import pl.tapo24.twa.databinding.FragmentHelpersBinding

class HelpersFragment: Fragment() {
    private var _binding: FragmentHelpersBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currentOrientation = resources.configuration.orientation

        val viewModelHelpers =
            ViewModelProvider(this).get(HelpersViewModel::class.java)

        _binding = FragmentHelpersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rv = binding.rv

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv.layoutManager = GridLayoutManager(activity,4)
        } else {

            rv.layoutManager = GridLayoutManager(activity,3)
        }

        viewModelHelpers.adapter = NavAdapter(NavHelpers.values().map {it.navElement})
        rv.adapter = viewModelHelpers.adapter

        viewModelHelpers.adapter.onActiveItemClick = { navElement ->
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
        viewModelHelpers.adapter.onDeActiveItemClick = { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        }




        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}