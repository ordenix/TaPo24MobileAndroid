package pl.tapo24.twa.ui.tariff.customCategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import pl.tapo24.twa.adapter.CustomCategoryAdapter
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CustomCategory
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.useCase.customCategory.AddCustomCategoryUseCase
import pl.tapo24.twa.useCase.customCategory.ChangeNameCustomCategoryUseCase
import pl.tapo24.twa.useCase.customCategory.ChangeOrderCustomCategoryUseCase
import pl.tapo24.twa.useCase.customCategory.DeleteCustomCategoryUseCase
import javax.inject.Inject

@HiltViewModel
class CustomCategoryViewModel @Inject constructor(
    private val addCustomCategoryUseCase: AddCustomCategoryUseCase,
    private val changeNameCustomCategoryUseCase: ChangeNameCustomCategoryUseCase,
    private val customCategoryModule: CustomCategoryModule,
    private val deleteCustomCategoryUseCase: DeleteCustomCategoryUseCase,
    private val changeOrderCustomCategoryUseCase: ChangeOrderCustomCategoryUseCase,

): ViewModel() {
    var customCategoryList: MutableLiveData<List<CustomCategory>> = MutableLiveData()
    lateinit var  adapter: CustomCategoryAdapter
    var customCategoryToDelete: CustomCategory? = null
    var customCategoryToEditName: CustomCategory? = null
    val showEditNameDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    val showDeleteDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    val showInfoDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    var infoDialogText: String = ""
    val showErrorDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    var errorDialogText: String = ""

    init {
        getData()
        getShowParameter()
    }

    fun saveShowParameters(showOnTop: Boolean) {
        customCategoryModule.changeShowOnTopParameter(showOnTop)
    }

    private fun getShowParameter() {
        viewModelScope.launch(Dispatchers.IO) {
            val showOnTop = async { customCategoryModule.getShowOnTopParameter() }.await()
            withContext(Dispatchers.Main) {
                State.showCustomOnTop.value = showOnTop
            }
        }
    }

    fun getData() {
        MainScope().launch(Dispatchers.IO) {
            val customCategoryResponse = customCategoryModule.getCustomCategories()
            customCategoryResponse.onSuccess {
                withContext(Dispatchers.Main) {
                    customCategoryList.value = it
                }
            }

        }
    }

    fun performChangeNameCustomCategory(customCategory: CustomCategory) {
        customCategoryToEditName = customCategory
        showEditNameDialog.value = true
    }
    fun changeNameCustomCategoryName(name: String) {
        customCategoryToEditName?.apply {
            categoryName = name
        }
        viewModelScope.launch(Dispatchers.IO) {
            val response = changeNameCustomCategoryUseCase.run(customCategoryToEditName!!)
            response.onSuccess {
                getData()
                infoDialogText = it
                withContext(Dispatchers.Main) {
                    showInfoDialog.value = true
                }
            }
            response.onFailure {
                errorDialogText = it.message!!
                withContext(Dispatchers.Main) {
                    showErrorDialog.value = true
                }
            }
        }
    }

    fun performDeleteCustomCategory(customCategory: CustomCategory) {
        customCategoryToDelete = customCategory
        showDeleteDialog.value = true
    }
    fun addCustomCategory(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var sortId = 0
            async { sortId = customCategoryModule.getLastSortId() }.await()
            val response = addCustomCategoryUseCase.run(name, sortId)
            response.onSuccess {
                getData()
                infoDialogText = it
                withContext(Dispatchers.Main) {
                    showInfoDialog.value = true
                }
            }
            response.onFailure {
                errorDialogText = it.message!!
                withContext(Dispatchers.Main) {
                    showErrorDialog.value = true
                }
            }
        }

    }

    fun deleteCustomCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = deleteCustomCategoryUseCase.run(customCategoryToDelete!!)
            response.onSuccess {
                getData()
                infoDialogText = it
                withContext(Dispatchers.Main) {
                    showInfoDialog.value = true
                }
            }
            response.onFailure {
                errorDialogText = it.message!!
                withContext(Dispatchers.Main) {
                    showErrorDialog.value = true
                }
            }
        }
    }



    fun changeOrderCustomCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = changeOrderCustomCategoryUseCase.run(customCategoryList.value!!)
            response.onFailure {
                errorDialogText = it.message!!
                withContext(Dispatchers.Main) {
                    showErrorDialog.value = true
                }
            }
        }

    }
}