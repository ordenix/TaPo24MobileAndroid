package pl.tapo24

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import pl.tapo24.db.TapoDb
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.dbData.entity.DataBaseVersion
import pl.tapo24.infrastructure.NetworkClient


class DataUpdater(
    val tapoDb: TapoDb,
    val dataTapoDb: DataTapoDb,
    val networkClient: NetworkClient,
    val context: Context

) {
    private val delay: Long = 200
    private val dialog = MaterialAlertDialogBuilder(context)
        .setTitle("Pobieranie danych")
        .setMessage("Proszę czekać")
        // .setCancelable(false)
        .show()

    fun deleteData() {
        MainScope().launch(Dispatchers.IO) {

            async {
                dataTapoDb.clearAllTables()

            }.await()
        }
    }

    fun getData() {


        // ToDo: Handle error network available
        MainScope().launch(Dispatchers.IO) {
            var listDataBaseVersion: List<DataBaseVersion>? = null
            async { listDataBaseVersion = dataTapoDb.dataBaseVersion().getAll() }.await()
            var listDataBaseVersionFromDbServer: List<DataBaseVersion>? = null
            async {
                val response = networkClient.getAllDataBaseVersion()
                response.onSuccess {
                    listDataBaseVersionFromDbServer = it
                }
                response.onFailure {
                    println("ERRROROROR")
                }
            }.await()
            if (listDataBaseVersion == null || listDataBaseVersion!!.isEmpty() && listDataBaseVersionFromDbServer != null){
                //insert all database version list
                async {
                    dataTapoDb.dataBaseVersion().insertList(listDataBaseVersionFromDbServer!!)
                }.await()
                //get all data
                getCodeDrivingLicence()
                Thread.sleep(delay)
                getCountryDrivingLicence()
                Thread.sleep(delay)
                getLightsCode()
                Thread.sleep(delay)
                getLightsCode()
                Thread.sleep(delay)
                getLightsFront() // 5
                Thread.sleep(delay)
                getLightsOthers()
                Thread.sleep(delay)
                getStatus()
                Thread.sleep(delay)
                getTowing()
                Thread.sleep(delay)

                getStory()
                Thread.sleep(delay)
                getCodePointsOld()
                Thread.sleep(delay)
                getCodePointsNew()
                Thread.sleep(delay)
                getHoldingDocuments()
                Thread.sleep(delay)
                getControlList()
                Thread.sleep(delay)
                getCodeLimitsDrivingLicence()
                Thread.sleep(delay)
                getUto()

            } else {
                //getCodeDrivingLicence()
                listDataBaseVersionFromDbServer?.forEach {
                    //dialog.setMessage("Sprawdzanie aktualności baz danych")
                    val element = listDataBaseVersion!!.find { el -> el.id == it.id }
                    if (it.version > (element?.version ?: 0)) {
                        // different versions
                        if (it.id == "code_driving_licence"){
                            getCodeDrivingLicence()
                        }
                        //
                        if (it.id == "code_country_driving_licence"){
                            getCountryDrivingLicence()
                            Thread.sleep(delay)
                        }
                        if (it.id == "code_lights"){
                            getLightsCode()
                            Thread.sleep(delay)

                        }
                        if (it.id == "lights_front"){
                            getLightsFront()
                            Thread.sleep(delay)

                        }
                        if (it.id == "lights_others"){
                            getLightsOthers()
                            Thread.sleep(delay)

                        }
                        if (it.id == "status"){
                            getStatus()
                            Thread.sleep(delay)

                        }
                        if (it.id == "towing"){
                            getTowing()
                            Thread.sleep(delay)
                        }
                        //
                        if (it.id == "story"){
                            getStory()
                            Thread.sleep(delay)
                        }
                        if (it.id == "holding_documents"){
                            getHoldingDocuments()
                            Thread.sleep(delay)
                        }
                        if (it.id == "points_old"){
                            getCodePointsOld()
                            Thread.sleep(delay)
                        }
                        if (it.id == "points_new"){
                            getCodePointsNew()
                            Thread.sleep(delay)
                        }
                        if (it.id == "code_limits_driving_licence"){
                            getCodeLimitsDrivingLicence()
                            Thread.sleep(delay)
                        }
                        if (it.id == "control_list"){
                            getControlList()
                            Thread.sleep(delay)
                        }
                        if (it.id == "uto"){
                            getUto()
                            Thread.sleep(delay)
                        }
                        async { dataTapoDb.dataBaseVersion().insert(it) }.await()
                    }
                }
            }
            withContext(Dispatchers.Main) {
                dialog.dismiss()
            }

        }
    }

    private fun getCodeDrivingLicence() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie kodów prawa jazdy")
            }
            async {
                val response = networkClient.getCodeDrivingLicence()
                response.onSuccess {
                    dataTapoDb.codeDrivingLicence().insertList(it)
                }
            }.await()
        }

    }

    private fun getCountryDrivingLicence() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie dopuszczonych krajów prawa jazdy")
            }
            async {
                val response = networkClient.getCountryDrivingLicenceData()
                response.onSuccess {
                    dataTapoDb.countryDivingLicence().insertList(it)
                }
            }.await()
        }
    }
    private fun getLightsCode() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie kodów krajów homologacji UE")
            }
            async {
                val response = networkClient.getLightsCodeData()
                response.onSuccess {
                    dataTapoDb.lightsCodeCountry().insertList(it)
                }
            }.await()
        }
    }
    private fun getLightsFront() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie kodów oświetlenia głównego pojazdu")
            }
            async {
                val response = networkClient.getLightsFrontData()
                response.onSuccess {
                    dataTapoDb.lightsFront().insertList(it)
                }
            }.await()
        }
    }
    private fun getLightsOthers() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie kodów oświetlenia pozostałego pojazdu")
            }
            async {
                val response = networkClient.getLightsOthersData()
                response.onSuccess {
                    dataTapoDb.lightsOthers().insertList(it)
                }
            }.await()
        }
    }
    private fun getStatus() {
        MainScope().launch(Dispatchers.IO) {
            async {
                withContext(Dispatchers.Main) {
                    dialog.setMessage("Pobieranie statusów KSIP")
                }
                val response = networkClient.getStatusData()
                response.onSuccess {
                    dataTapoDb.status().insertList(it)
                }
            }.await()
        }
    }
    private fun getTowing() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych holowania")
            }
            async {
                val response = networkClient.getTowingData()
                response.onSuccess {
                    dataTapoDb.towing().insertList(it)
                }
            }.await()
        }
    }
    //
    private fun getStory() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych historyjek")
            }
            async {
                val response = networkClient.getStoryData()
                response.onSuccess {
                    dataTapoDb.story().insertList(it)
                }
            }.await()
        }
    }
    private fun getCodeLimitsDrivingLicence() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych kodów ograniczeń PJ")
            }
            async {
                val response = networkClient.getCodeLimitsDrivingLicenceData()
                response.onSuccess {
                    dataTapoDb.codeLimitsDrivingLicence().insertList(it)
                }
            }.await()
        }
    }
    private fun getHoldingDocuments() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych zatrzymywania dokumentów")
            }
            async {
                val response = networkClient.getHoldingDocumentsData()
                response.onSuccess {
                    dataTapoDb.holdingDocuments().insertList(it)
                }
            }.await()
        }
    }
    private fun getControlList() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych listy kontroli")
            }
            async {
                val response = networkClient.getControlListData()
                response.onSuccess {
                    dataTapoDb.controlList().insertList(it)
                }
            }.await()
        }
    }
    private fun getCodePointsOld() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych kodów wykroczeń (starych)")
            }
            async {
                val response = networkClient.getCodePointsOldData()
                response.onSuccess {
                    dataTapoDb.codePointsOld().insertList(it)
                }
            }.await()
        }
    }
    private fun getCodePointsNew() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych kodów wykroczeń (nowych)")
            }
            async {
                val response = networkClient.getCodePointsNewData()
                response.onSuccess {
                    dataTapoDb.codePointsNew().insertList(it)
                }
            }.await()
        }
    }
    private fun getUto() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych UTO")
            }
            async {
                val response = networkClient.getUtoData()
                response.onSuccess {
                    dataTapoDb.uto().insertList(it)
                }
            }.await()
        }
    }
}