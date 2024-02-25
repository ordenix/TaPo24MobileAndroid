package pl.tapo24.twa.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.data.FontTypesData
import pl.tapo24.twa.data.State
import pl.tapo24.twa.ui.settings.SettingsViewModel
import java.util.*

class SettingFontAdapter(
    var items: List<FontTypesData>,
    var viewModel: SettingsViewModel,
    var context: Context
): RecyclerView.Adapter<SettingFontAdapter.SettingFontAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingFontAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_setting_font, parent,false)
        return SettingFontAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SettingFontAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class SettingFontAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FontTypesData) {
            val radio = binding.root.findViewById<RadioButton>(R.id.radioFont)

            if (item.fontType.rIdFont != null) {
                val font = ResourcesCompat.getFont(context, item.fontType.rIdFont)
                radio.typeface = font

            } else {
                val font = Typeface.SANS_SERIF
                radio.typeface = font
            }

            if (item.fontType.isNonPremium) {
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