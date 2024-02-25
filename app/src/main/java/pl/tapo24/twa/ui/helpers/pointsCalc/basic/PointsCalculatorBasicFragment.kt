package pl.tapo24.twa.ui.helpers.pointsCalc.basic

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.adapter.PointCalculatorBasicAdapter
import pl.tapo24.twa.data.SelectableSearchableDialogElement
import pl.tapo24.twa.databinding.FragmentPointsCalculatorBasicBinding
import pl.tapo24.twa.dbData.entity.CodePointsNew
import pl.tapo24.twa.ui.utils.SelectableSearchableDialog


@AndroidEntryPoint
class PointsCalculatorBasicFragment : Fragment() {

    private var _binding: FragmentPointsCalculatorBasicBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PointsCalculatorBasicFragment()
    }

    private lateinit var viewModel: PointsCalculatorBasicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPointsCalculatorBasicBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(PointsCalculatorBasicViewModel::class.java)


        val dialog = SelectableSearchableDialog<CodePointsNew>()
        dialog.title = "Wybierz czyn do dodania"
        dialog.tileField1 = "Kod czynu: "
        dialog.tileField2 = "Opis: "
        dialog.tileField3 = "Punkty: "
        dialog.isMultiSelect = false

        viewModel.itemsCodePoints.observe(viewLifecycleOwner, Observer { codePointsNews ->
            if (codePointsNews!= null) {
                val listMap: List<SelectableSearchableDialogElement> = codePointsNews.map {
                    SelectableSearchableDialogElement(
                        it.id,
                        it.description?: "",
                        it.points.toString()
                    )
                }
                dialog.itemMapList = listMap
                dialog.itemList = codePointsNews
                dialog.selectedItem.observe(viewLifecycleOwner, Observer {

                    viewModel.addToList(it)

                })
//                val list = it.map { it.name }
//                dialogTest.itemList = list

            }
        })

        binding.addButton.setOnClickListener {
            dialog.show(childFragmentManager,"dialogPoints")
        }

        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = PointCalculatorBasicAdapter(viewModel.itemsCodePointsCalc.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.itemsCodePointsCalc.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                viewModel.adapter.items = it

            }
            viewModel.adapter.notifyDataSetChanged()

        })
        viewModel.sumPoints.observe(viewLifecycleOwner, Observer {
            if (24< it) {
                binding.warning.visibility = View.VISIBLE
            } else {
                binding.warning.visibility = View.GONE
            }
            if (it == 0) {
                binding.sumText.visibility = View.GONE
            } else {
                binding.sumText.visibility = View.VISIBLE
            }
            binding.sumText.text = "Suma: $it pkt"
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}