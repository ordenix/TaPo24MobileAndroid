package pl.tapo24.twa

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.dbData.entity.DataBaseVersion
import pl.tapo24.twa.dbData.entity.Law
import pl.tapo24.twa.infrastructure.NetworkClient
import java.io.File


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
                tapoDb.tariffDb().nukeTable()

            }.await()
        }
    }

    fun getPDF() {
        // ToDo: Handle error network available
        MainScope().launch(Dispatchers.IO) {
            var listLawFromServer :List<Law>? = null
            var listLawFromDb :List<Law>? = null
            async { listLawFromDb = dataTapoDb.law().getAll() }.await()
            async {
                val response = networkClient.getLawData()
                response.onSuccess {
                    listLawFromServer = it
                }
            }.await()
            listLawFromServer?.forEach {element ->
                val elementFromDb = listLawFromDb?.find { el -> el.id == element.id }
                if (element.version!! >= (elementFromDb?.version ?: 0)) {

                    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${element.type}/${element.fileName}")
                   // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24/pdf/${element.type}/${element.fileName}")

                    file.delete()

                    val request = DownloadManager.Request(Uri.parse(element.url))
                    request.setDescription("Plik pdf ${element.alias} jest w trakcie pobierania")
                    request.allowScanningByMediaScanner()
                    request.setTitle("Pobieranie pliku ${element.alias}")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    //Set the local destination for the downloaded file to a path within the application's external files directory
                    //Set the local destination for the downloaded file to a path within the application's external files directory
                    //request.set
//                    request.setDestinationInExternalPublicDir(
//                        Environment.DIRECTORY_DOWNLOADS,
//                        "tapo24/pdf/${element.type}/${element.fileName}"
//                    )
                    request.setDestinationInExternalFilesDir(
                        context,
                        Environment.DIRECTORY_DOWNLOADS,
                        "${element.type}/${element.fileName}"
                    ) //To Store file in External Public Directory use "setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)"
                    val downloadManager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE)  as DownloadManager?
                    val downoloadID = downloadManager!!.enqueue(request)
                    // TODO: progres and notify https://stackoverflow.com/questions/65164785/using-progressbar-with-downloadmanager
                    println(downoloadID)
                    async { dataTapoDb.law().insert(element) }.await()

                }

            }
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
                Thread.sleep(delay)
                getSign()
                Thread.sleep(delay)
                getTaiff()

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
                        if (it.id == "sign"){
                            getSign()
                            Thread.sleep(delay)
                        }
                        if (it.id == "tariff"){
                            getTaiff()
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

    private fun getSign() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych Znaków")
            }
            async {
                val response = networkClient.getSignData()
                response.onSuccess {
                    dataTapoDb.sign().insertList(it)
                }
            }.await()
        }
    }

    private fun getTaiff() {
        MainScope().launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                dialog.setMessage("Pobieranie danych Taryfikatora")
            }
            var responseFromDb: List<Tariff>? = null
            async { responseFromDb = tapoDb.tariffDb().getAll() }.await()
            val response = networkClient.getTariffData()

            if (responseFromDb != null) {
                response.onSuccess { element ->
                    element.forEach {
                       val elementFind =  responseFromDb!!.find { element-> element.id == it.id }
                        if (elementFind != null) {
                            elementFind.category = it.category
                            elementFind.code = it.code
                            elementFind.law = it.law
                            elementFind.maxSpeed = it.maxSpeed
                            elementFind.minSpeed = it.minSpeed
                            elementFind.name = it.name
                            elementFind.paragraph = it.paragraph
                            elementFind.path = it.path
                            elementFind.points = it.points
                            elementFind.subName = it.subName
                            elementFind.tax = it.tax
                            elementFind.taxRecidive = it.taxRecidive
                            elementFind.recidive = it.recidive
                            elementFind.tax = it.tax
                            elementFind.enginesType = it.enginesType
                            async { tapoDb.tariffDb().insert(elementFind) }.await()

                        } else {
                            //dont exist
                            async { tapoDb.tariffDb().insert(it) }.await()
                        }

                    }
                    responseFromDb!!.forEach { elementFromDb ->
                       if ( element.find { el -> el.id == elementFromDb.id } == null) {
                           async { tapoDb.tariffDb().deleteElement(elementFromDb) }.await()
                       }
                    }

                }


            } else {
                async {
                    response.onSuccess {
                        tapoDb.tariffDb().insertList(it)
                    }
                }.await()

            }



        }
    }
}