package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.models.Period
import com.revenuecat.purchases.models.StoreProduct
import com.revenuecat.purchases.models.SubscriptionOption
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.ui.shop.PaywallItem
import pl.tapo24.twa.ui.shop.ShopViewModel
import java.util.*

class ShopListAdapter(
    var offering: Offering?,
    var viewModel: ShopViewModel
): RecyclerView.Adapter<ShopListAdapter.ShopListAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_shop_list, parent,false)
        return ShopListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return theItems.size
    }

    override fun onBindViewHolder(holder: ShopListAdapterHolder, position: Int) {
        val currentItem = theItems[position]
        holder.bind(currentItem)
    }

    inner class ShopListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PaywallItem) {
            when (item) {
                is PaywallItem.Title -> {
                    binding.setVariable(BR.title, item.title)
                }
                is PaywallItem.Product -> {
                    println(item)

                }
                is PaywallItem.Option -> {
                    println(item)
                    val option = item.subscriptionOption
                    val price = option.pricingPhases.joinToString(separator = " następnie ") { phase ->
                        "${phase.price.formatted} za ${phase.billingPeriod.toDescription} ${if (phase.billingCycleCount != 0) "(przez ${phase.billingCycleCount} miesiące)" else ""}"
                    }

                    binding.setVariable(BR.paywallItem, item)
                    binding.setVariable(BR.price, price)
                    binding.setVariable(BR.title, null)
                    binding.setVariable(BR.viewModel, viewModel)
                    // for special offers
//                    viewHolder.view.findViewById<TextView>(R.id.paywall_item_best_offer).visibility =
//                        if (it.defaultOffer) View.VISIBLE else View.GONE
                }
            }

        }
    }
    private val theItems: List<PaywallItem>
        get() = offering?.availablePackages?.flatMap {
            val product = it.product

            it.product.subscriptionOptions?.let { options ->
                val title = product.period?.toTitle ?: product.title

                mutableListOf(PaywallItem.Title(title)) + options.map { option ->
                    PaywallItem.Option(option, option == it.product.defaultOption)
                }.sortedBy { option -> !option.defaultOffer }
            } ?: run {
                listOf(
                    PaywallItem.Title(product.title),
                    PaywallItem.Product(product),
                )
            }
        } ?: emptyList()


}

val Period.toTitle: String?
    get() = when (unit) {
        Period.Unit.DAY -> if (value == 1) "Dzienny" else "Every $value days"
        Period.Unit.WEEK -> if (value == 1) "Tygodniowy" else "Every $value weeks"
        Period.Unit.MONTH -> if (value == 1) "Abonament miesięczny:" else "Every $value months"
        Period.Unit.YEAR -> if (value == 1) "Roczny" else "Every $value years"
        Period.Unit.UNKNOWN -> "Unknown"
    }

val Period.toDescription: String?
    get() = when (unit) {
        Period.Unit.DAY -> if (value == 1) "$value day" else "$value days"
        Period.Unit.WEEK -> if (value == 1) "$value week" else "$value weeks"
        Period.Unit.MONTH -> if (value == 1) "$value miesiąc" else "$value months"
        Period.Unit.YEAR -> if (value == 1) "$value year" else "$value years"
        Period.Unit.UNKNOWN -> null
    }