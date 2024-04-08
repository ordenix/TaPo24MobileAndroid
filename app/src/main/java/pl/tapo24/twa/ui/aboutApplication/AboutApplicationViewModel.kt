package pl.tapo24.twa.ui.aboutApplication

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.tapo24.twa.adapter.AboutApplicationAdapter
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.DataBaseVersion
import pl.tapo24.twa.updater.AssetUpdater
import pl.tapo24.twa.updater.DataBaseUpdater
import pl.tapo24.twa.updater.LawUpdater
import javax.inject.Inject


@HiltViewModel
class AboutApplicationViewModel @Inject constructor(
    private val dataBaseUpdater: DataBaseUpdater,
    private val dataTapoDb: DataTapoDb,
    private val assetUpdater: AssetUpdater,
    private val lawUpdater: LawUpdater,
    @ApplicationContext private val context: Context
): ViewModel() {
    private lateinit var sharedPreferences: SharedPreferences

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is ABOUT APLICATION"
//    }
//    val text: LiveData<String> = _text
    lateinit var adapter: AboutApplicationAdapter
    val data = MutableLiveData<List<DataBaseVersion>>()

    fun getData () {
        viewModelScope.launch(Dispatchers.IO) {
            val list = async { dataTapoDb.dataBaseVersion().getAll() }.await()
            withContext(Dispatchers.Main) {
                if (list != null) {
                    data.value = list
                }
            }
        }

    }

    fun forceUpdateAsset() {
        assetUpdater.update(true)
    }

    fun forceUpdatePdf() {
        sharedPreferences = context.getSharedPreferences(
            "Jwt",
            Context.MODE_PRIVATE)
        val jwt: String? = sharedPreferences.getString("token", null)

        lawUpdater.update(true, jwt = jwt)
    }

    fun forceUpdate() {
        dataBaseUpdater.update(true)
    }

    fun update() {
        dataBaseUpdater.update()
    }
}