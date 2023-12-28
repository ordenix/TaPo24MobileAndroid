package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.data.State
import pl.tapo24.twa.data.ThemeTypesData
import pl.tapo24.twa.ui.settings.SettingsViewModel
import java.util.*

class SettingThemeAdapter(
    var items: List<ThemeTypesData>,
    var viewModel: SettingsViewModel
): RecyclerView.Adapter<SettingThemeAdapter.SettingThemeAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingThemeAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_setting_theme, parent,false)
        return SettingThemeAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SettingThemeAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class SettingThemeAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ThemeTypesData) {
            if (item.themeType.isNonPremium) {
                binding.setVariable(BR.isEnabled, true)
            } else {
                if (State.premiumVersion) {
                    binding.setVariable(BR.isEnabled, true)
                } else {
                    binding.setVariable(BR.isEnabled, false)
                }
            }
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)


        }
    }


}