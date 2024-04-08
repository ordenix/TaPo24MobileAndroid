package pl.tapo24.twa.ui.admin

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.NavAdapter
import pl.tapo24.twa.data.nav.NavHelpers
import pl.tapo24.twa.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {

    companion object {
        fun newInstance() = AdminFragment()
    }
    private var _binding: FragmentAdminBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentOrientation = resources.configuration.orientation

        viewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rv = binding.rv

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv.layoutManager = GridLayoutManager(activity,4)
        } else {

            rv.layoutManager = GridLayoutManager(activity,3)
        }

        viewModel.adapter = NavAdapter(NavAdmin.values().map {it.navElement})
        rv.adapter = viewModel.adapter

        viewModel.adapter.onActiveItemClick = { navElement ->
            if (navElement.navId != null) {
                requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
                view?.findNavController()?.navigate(
                    navElement.navId,
                    navElement?.listBundle
                )
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}