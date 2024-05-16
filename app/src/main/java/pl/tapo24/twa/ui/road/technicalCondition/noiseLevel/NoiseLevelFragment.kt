package pl.tapo24.twa.ui.road.technicalCondition.noiseLevel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.NoiseLevelAdapter
import pl.tapo24.twa.databinding.FragmentNoiseLevelBinding
@AndroidEntryPoint
class NoiseLevelFragment : Fragment() {


    private var _binding: FragmentNoiseLevelBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = NoiseLevelFragment()
    }

    private lateinit var viewModel: NoiseLevelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoiseLevelBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(NoiseLevelViewModel::class.java)


        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = NoiseLevelAdapter(viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}