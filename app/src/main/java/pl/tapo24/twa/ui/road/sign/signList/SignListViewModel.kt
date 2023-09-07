package pl.tapo24.twa.ui.road.sign.signList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.SignListAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.Sign
import javax.inject.Inject
@HiltViewModel

class SignListViewModel@Inject constructor(
    private val dataTapoDb: DataTapoDb
) : ViewModel() {
    val data = MutableLiveData<List<Sign>>()


    lateinit var adapter: SignListAdapter


    fun getData() {
        viewModelScope.launch(Dispatchers.IO){
            var dataFromDb: List<Sign>? = null
            async { dataFromDb = dataTapoDb.sign().getAllBySignCategory("A") }.await()
            withContext(Dispatchers.Main) {
                data.value = dataFromDb!!
            }
        }
    }

    fun findRes(context: Context,path: String): Int {
        val id: Int = context.resources.getIdentifier(path,"drawable",context.packageName)
        return id
    }


}