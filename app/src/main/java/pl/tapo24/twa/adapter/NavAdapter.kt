package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.data.nav.NavElement
import pl.tapo24.twa.data.State

class NavAdapter(
    var items: List<NavElement>
): RecyclerView.Adapter<NavAdapter.NavAdapterHolder>() {
    var onActiveItemClick: ((NavElement) -> Unit)? = null
    var onDeActiveItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_nav_element, parent,false)
        return NavAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NavAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class NavAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NavElement) {
            val img1 = binding.root.findViewById<ImageView>(R.id.imageViewA)
            val img2 = binding.root.findViewById<ImageView>(R.id.imageViewAA)
            val container = binding.root.findViewById<ConstraintLayout>(R.id.container)
            val container2 = binding.root.findViewById<ConstraintLayout>(R.id.container2)
            if (item.requireLogin && State.isLogin.value == false || item.requirePremium && !State.premiumVersion) {
                binding.setVariable(BR.enabled, false)
            } else {
                binding.setVariable(BR.enabled, true)
            }
            img1.setImageResource(item.navIcon)
            img2.setImageResource(item.navIcon)
            container.setOnClickListener {
                onActiveItemClick?.invoke(item)
            }
            container2.setOnClickListener {
                if (item.requireLogin) {
                    onDeActiveItemClick?.invoke("Aby korzystać z tej funkcjonalności wymagane jest zalogowanie się do aplikacji.")

                }
                if (item.requirePremium) {
                    onDeActiveItemClick?.invoke("Aby korzystać z tej funkcjonalności zostań użytkownikiem PREMIUM.")

                }
            }
            binding.setVariable(BR.data, item)
            binding.executePendingBindings()

        }
    }


}