package pl.tapo24.twa.ui.shop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.models.Period
import com.revenuecat.purchases.models.StoreProduct
import com.revenuecat.purchases.models.SubscriptionOption
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.tapo24.twa.adapter.ShopListAdapter
import pl.tapo24.twa.module.PremiumShopModule
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val premiumShopModule: PremiumShopModule
) : ViewModel() {
    lateinit var adapter: ShopListAdapter
    val testItem: MutableLiveData<PaywallItem> = MutableLiveData()
    val offeringsData: MutableLiveData<Offering?> = MutableLiveData()
    val itemClicked: MutableLiveData<SubscriptionOption> = MutableLiveData()


    init {
        fetchProducts()
    }

    fun clickOnItem(item: PaywallItem) {
        when (item) {
            is PaywallItem.Product -> {
                //purchaseProduct(item.storeProduct)
            }
            is PaywallItem.Option -> {
                itemClicked.value = item.subscriptionOption
            }
            is PaywallItem.Title -> {
                // Do nothing
            }
        }

    }
    fun successPurchase() {
        premiumShopModule.requestUpdatePermissionInBackendAndSetNewJWT()

    }

    private fun fetchProducts() {
        Purchases.sharedInstance.getOfferingsWith { offerings: Offerings ->
            offeringsData.value = offerings.current
        }
    }

    fun fetchProducts2() {
        Purchases.sharedInstance.getOfferingsWith { offerings: Offerings ->
            val listOf: Offering = offerings.current!!
//            val sss: SubscriptionOption = SubscriptionOption
            val s: List<PaywallItem> = listOf?.availablePackages?.flatMap {
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
            testItem.value = s[1]
        }
    }

//    sealed class PaywallItem {
//        data class Title(
//            val title: String,
//        ) : PaywallItem()
//
//        data class Product(
//            val storeProduct: StoreProduct,
//        ) : PaywallItem()
//
//        data class Option(
//            val subscriptionOption: SubscriptionOption,
//            val defaultOffer: Boolean,
//        ) : PaywallItem()
//    }
    val Period.toTitle: String?
        get() = when (unit) {
            Period.Unit.DAY -> if (value == 1) "Daily" else "Every $value days"
            Period.Unit.WEEK -> if (value == 1) "Weekly" else "Every $value weeks"
            Period.Unit.MONTH -> if (value == 1) "Monthly" else "Every $value months"
            Period.Unit.YEAR -> if (value == 1) "Yearly" else "Every $value years"
            Period.Unit.UNKNOWN -> "Unknown"
        }
}