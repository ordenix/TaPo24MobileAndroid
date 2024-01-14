package pl.tapo24.twa.ui.shop

import com.revenuecat.purchases.models.StoreProduct
import com.revenuecat.purchases.models.SubscriptionOption

sealed class PaywallItem {
    data class Title(
        val title: String,
    ) : PaywallItem()

    data class Product(
        val storeProduct: StoreProduct,
    ) : PaywallItem()

    data class Option(
        val subscriptionOption: SubscriptionOption,
        val defaultOffer: Boolean,
    ) : PaywallItem()
}