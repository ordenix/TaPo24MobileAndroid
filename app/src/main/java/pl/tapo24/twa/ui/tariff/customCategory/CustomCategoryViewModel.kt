package pl.tapo24.twa.ui.tariff.customCategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.CustomCategoryAdapter
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.useCase.customCategory.AddCustomCategoryUseCase
import pl.tapo24.twa.useCase.customCategory.ChangeNameCustomCategoryUseCase
import javax.inject.Inject

@HiltViewModel
class CustomCategoryViewModel @Inject constructor(
    private val addCustomCategoryUseCase: AddCustomCategoryUseCase,
    private val changeNameCustomCategoryUseCase: ChangeNameCustomCategoryUseCase,
    private val customCategoryModule: CustomCategoryModule
): ViewModel() {
    var customCategoryList: MutableLiveData<List<CustomCategory>> = MutableLiveData()
    lateinit var  adapter: CustomCategoryAdapter

    init {
        MainScope().launch(Dispatchers.IO) {
            val cusomCategory = CustomCategory(35,"asdTTTghj",13)
           val sss = changeNameCustomCategoryUseCase.run(cusomCategory)
            //println(sss)
            val customCategoryResponse = customCategoryModule.getCustomCategories()
            customCategoryResponse.onSuccess {
                withContext(Dispatchers.Main) {
                    customCategoryList.value = it
                }
            }

        }

    }
}