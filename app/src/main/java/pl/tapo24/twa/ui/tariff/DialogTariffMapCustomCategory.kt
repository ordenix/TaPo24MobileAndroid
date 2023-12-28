package pl.tapo24.twa.ui.tariff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pl.tapo24.twa.adapter.CustomCategoryMapAdapter
import pl.tapo24.twa.data.customCategory.CategoryToMap
import pl.tapo24.twa.databinding.DialogCustomCategoryMapBinding

class DialogTariffMapCustomCategory: DialogFragment() {
    private val binding get() = _binding!!
    private var _binding:DialogCustomCategoryMapBinding? = null
    var tariffViewModel: TariffViewModel? =  null
//    lateinit var adapter: CustomCategoryMapAdapter
//    var categoryMapList: List<CategoryToMap>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= DialogCustomCategoryMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        if (tariffViewModel != null) {
            rv.adapter = tariffViewModel!!.adapterCustomCategoryMap
            tariffViewModel!!.mapListCustomCategory.observe(viewLifecycleOwner, Observer {
                tariffViewModel!!.adapterCustomCategoryMap.items = it
                tariffViewModel!!.adapterCustomCategoryMap.notifyDataSetChanged()
        })
        }
        binding.buttonClose.setOnClickListener {
            dismiss()
        }




        return root
    }
}