package pl.tapo24.twa.ui.aboutApplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.adapter.AboutApplicationAdapter
import pl.tapo24.twa.data.State
import pl.tapo24.twa.databinding.FragmentAboutApplicationBinding

@AndroidEntryPoint
class AboutApplicationFragment: Fragment() {
    private var _binding: FragmentAboutApplicationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(AboutApplicationViewModel::class.java)
        // TODO: uncoment in xml check update etc

        _binding = pl.tapo24.twa.databinding.FragmentAboutApplicationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.uid.text = State.uid
        binding.versionCode.text = State.versionCode.toString()
        binding.versionName.text = State.versionName

        viewModel.getData()
        val rv = binding.recyclerView2
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = AboutApplicationAdapter(viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })
        binding.checkUpdate.setOnClickListener {
            viewModel.update()
        }
        binding.force.setOnClickListener {
            viewModel.forceUpdate()
        }

        binding.forcePdf.setOnClickListener {
            viewModel.forceUpdatePdf()
        }

        binding.forceAsset.setOnClickListener {
            viewModel.forceUpdateAsset()
        }

        viewModel.getRestoreButtonStatus()

        viewModel.restoreButtonStatus.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.restorePurchase.visibility = View.VISIBLE
            } else {
                binding.restorePurchase.visibility = View.GONE
            }
        })
        binding.restorePurchase.setOnClickListener {
            viewModel.restorePurchase()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}