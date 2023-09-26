package pl.tapo24.twa.ui.helpers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentHelpersBinding

class HelpersFragment: Fragment() {
    private var _binding: FragmentHelpersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelHelpers =
            ViewModelProvider(this).get(HelpersViewModel::class.java)

        _binding = pl.tapo24.twa.databinding.FragmentHelpersBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.linkOc.setOnClickListener {
            openLink("https://www.ufg.pl/infoportal/faces/pages_home-page/Page_4d98135c_14e2b8ace27__7ff1/Pagee0e22f3_14efe6adc05__7ff1/Page4d024e07_14f0a824115__7ff6?_afrLoop=3753003479910681&_afrWindowMode=0&_adf.ctrl-state=182qsvy3xd_29")
        }
        binding.linkDrivingLicence.setOnClickListener {
            openLink("https://moj.gov.pl/uslugi/engine/ng/index?xFormsAppName=UprawnieniaKierowcow&xFormsOrigin=EXTERNAL")
        }
        binding.linkTelephoneBook.setOnClickListener {
            openLink("https://ckt.uc.ost112.gov.pl/")
        }
        binding.linkCarHistory.setOnClickListener {
            openLink("https://historiapojazdu.gov.pl/")
        }
        binding.linkLimits.setOnClickListener {
            openLink("https://www.gov.pl/web/infrastruktura/ograniczenia-w-ruchu")

        }

        binding.note.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_helpers_to_nav_note
            )
        }



        return root
    }

    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}