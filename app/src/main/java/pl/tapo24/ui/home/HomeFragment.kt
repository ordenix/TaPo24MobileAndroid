package pl.tapo24.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.adapter.HomeWhatsNewsAdapter
import pl.tapo24.databinding.FragmentHomeBinding
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
       homeViewModel.startApp()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rv = binding.RvWhatsNews
        rv.layoutManager = LinearLayoutManager(activity)

        homeViewModel.adapter = HomeWhatsNewsAdapter(homeViewModel.whatsNews.value.orEmpty())
        rv.adapter = homeViewModel.adapter
        homeViewModel.whatsNews.observe(viewLifecycleOwner, Observer {
            homeViewModel.adapter.items = it
            homeViewModel.adapter.notifyDataSetChanged()
        })

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}