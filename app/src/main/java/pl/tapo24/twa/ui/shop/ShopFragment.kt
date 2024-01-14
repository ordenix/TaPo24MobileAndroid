package pl.tapo24.twa.ui.shop

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.models.SubscriptionOption
import com.revenuecat.purchases.purchaseWith
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.ShopListAdapter
import pl.tapo24.twa.databinding.FragmentShopBinding
@AndroidEntryPoint
class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        val root: View = binding.root
        viewModel.fetchProducts2()


        val rv = binding.rvShop
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = ShopListAdapter(viewModel.offeringsData.value, viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.offeringsData.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.offering = it
            viewModel.adapter.notifyDataSetChanged()
        })




        viewModel.itemClicked.observe(viewLifecycleOwner){
            if (it != null) {
                viewModel.itemClicked.value = null
                Purchases.sharedInstance.purchaseWith(
                    PurchaseParams.Builder(requireActivity(), it).build(),
                    onError = { error, userCancelled ->
                        if (!userCancelled) {
                            println(error)
                        }
                    },
                    onSuccess = { _, _ ->
                        viewModel.successPurchase()
                    },
                )
            }

        }
//        viewModel.testItem.observe(viewLifecycleOwner) {
//            if (it != null) {
//                var item: SubscriptionOption? = null
//                when (it) {
//                    is ShopViewModel.PaywallItem.Product -> {
//                        //purchaseProduct(item.storeProduct)
//                    }
//                    is ShopViewModel.PaywallItem.Option -> {
//                       item = it.subscriptionOption
//                    }
//                    is ShopViewModel.PaywallItem.Title -> {
//                        // Do nothing
//                    }
//                }
//
//                Purchases.sharedInstance.purchaseWith(
//                    PurchaseParams.Builder(requireActivity(), item!!).build(),
//                    onError = { error, userCancelled ->
//                        if (!userCancelled) {
//                           println(error)
//                        }
//                    },
//                    onSuccess = { _, _ ->
//                        activity?.finish()
//                    },
//                )
//
//                println(it)
//            }
//        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}