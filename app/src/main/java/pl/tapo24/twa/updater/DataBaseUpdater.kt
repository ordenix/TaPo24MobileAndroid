package pl.tapo24.twa.updater

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.acra.ACRA
import pl.tapo24.twa.DialogNotificationRequest
import pl.tapo24.twa.MainActivity
import pl.tapo24.twa.R
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.useCase.checkList.GetCheckListAllTypeUseCase
import pl.tapo24.twa.useCase.checkList.GetCheckListDictionaryUseCase
import pl.tapo24.twa.useCase.checkList.GetCheckListMapUseCase
import java.text.SimpleDateFormat
import kotlin.reflect.KClass

class DataBaseUpdater @Inject constructor(
    private val context: Context,
    private val networkClient: NetworkClient,
    private val tapoDb: TapoDb,
    private val dataTapoDb: DataTapoDb,
    private val  getCheckListDictionaryUseCase: GetCheckListDictionaryUseCase,
    private val  getCheckListAllTypeUseCase: GetCheckListAllTypeUseCase,
    private val  getCheckListMapUseCase: GetCheckListMapUseCase,

) {




    fun <T : Any> convert(clazz: KClass<T>, it: Any): T?{
        val json = Gson().toJsonTree(it).asJsonObject
        return Gson().fromJson<T>(json,clazz.javaObjectType)

    }
    inline fun <reified T : Any> convert(it: Any): T? =
        convert(T::class, it)
    fun deleteAllData () {

    }

    fun update(force: Boolean = false) {
        MainScope().launch(Dispatchers.IO) {
            networkClient.getAllDataBaseVersion().onSuccess { dataBaseVersionFromServer ->
                if (dataBaseVersionFromServer.isNotEmpty()){
                    val dataBaseVersionFromDb = async {dataTapoDb.dataBaseVersion().getAll()}.await()
                    DataBaseType.values().forEach { dataBaseType ->
                        val versionFromServer: Int  = dataBaseVersionFromServer.find { it.id == dataBaseType.nameInDb }?.version ?: 0
                        val versionFromDb: Int  = dataBaseVersionFromDb.find { it.id == dataBaseType.nameInDb }?.version ?: 0
                        if (versionFromServer > versionFromDb || force) {
                            withContext(Dispatchers.Main) {
                                State.dialogDownloadMessage.value = dataBaseType.info
                            }
                            if (dataBaseType.isGeneric) {
                                    val response = async {networkClient.getDynamicData(dataBaseType.path)}.await()
                                    response.onSuccess {
                                        if (it.isNotEmpty()) {
                                            when (dataBaseType) {
                                                DataBaseType.CodeDrivingLicence -> {
                                                    async { dataTapoDb.codeDrivingLicence().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.codeDrivingLicence().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }

                                                    }
                                                }
                                                DataBaseType.CodeCountryDrivingLicence -> {
                                                    async { dataTapoDb.countryDivingLicence().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.countryDivingLicence().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }

                                                    }
                                                }
                                                DataBaseType.CodeLimitsDrivingLicence -> {
                                                    async { dataTapoDb.codeLimitsDrivingLicence().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.codeLimitsDrivingLicence().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }

                                                    }
                                                }
                                                DataBaseType.CodePointsNew -> {
                                                    async { dataTapoDb.codePointsNew().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.codePointsNew().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }

                                                    }
                                                }
                                                DataBaseType.ControlList -> {
                                                    async { dataTapoDb.controlList().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.controlList().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }

                                                    }
                                                }
                                                DataBaseType.HoldingDocuments -> {
                                                    async { dataTapoDb.holdingDocuments().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.holdingDocuments().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }

                                                    }
                                                }
                                                DataBaseType.CodeLights -> {
                                                    async { dataTapoDb.lightsCodeCountry().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.lightsCodeCountry().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.LightsFront -> {
                                                    async { dataTapoDb.lightsFront().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.lightsFront().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.LightsOther -> {
                                                    async { dataTapoDb.lightsOthers().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.lightsOthers().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Sign -> {
                                                    async { dataTapoDb.sign().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.sign().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Spb -> {
                                                    async { dataTapoDb.spb().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.spb().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Status -> {
                                                    async { dataTapoDb.status().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.status().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Story -> {
                                                    async { dataTapoDb.story().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.story().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Towing -> {
                                                    async { dataTapoDb.towing().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.towing().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Uto -> {
                                                    async { dataTapoDb.uto().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.uto().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.TelephoneNumbers -> {
                                                    async { dataTapoDb.telephoneNumbers().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.telephoneNumbers().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.CodeColors -> {
                                                    async { dataTapoDb.codeColors().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.codeColors().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.AdrNumber -> {
                                                    async { dataTapoDb.adrNumber().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.adrNumber().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Company -> {
                                                    async { dataTapoDb.company().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.company().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.HinNumber -> {
                                                    async { dataTapoDb.hinNumber().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.hinNumber().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.ImmunityAdres -> {
                                                    async { dataTapoDb.immunityAdres().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.immunityAdres().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.Immunity -> {
                                                    async { dataTapoDb.immunity().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.immunity().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.NoiseLevel -> {
                                                    async { dataTapoDb.noiseLevel().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.noiseLevel().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.TechnicalCondition -> {
                                                    async { dataTapoDb.technicalConditions().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.technicalConditions().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                DataBaseType.EnginePollution -> {
                                                    async { dataTapoDb.enginePollution().deleteAll() }.await()
                                                    it.forEach {
                                                        try {
                                                            async { dataTapoDb.enginePollution().insert(convert(it)!!) }.await()
                                                        } catch (ex:Throwable) {
                                                            ACRA.errorReporter.handleSilentException(ex)
                                                        }
                                                    }
                                                }
                                                else -> {}
                                            }

                                        }
                                        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
                                        val dataBaseVersion = dataBaseVersionFromServer.find { it.id == dataBaseType.nameInDb }
                                        dataBaseVersion?.dateLastUpdate = date
                                        try {
                                            async {
                                                if (dataBaseVersion != null) {
                                                    dataTapoDb.dataBaseVersion().insert(dataBaseVersion)
                                                }
                                            }.await()

                                        } catch (ex: Exception) {
                                            ACRA.errorReporter.handleSilentException(ex)

                                        }
                                    }


                            } else {
                                // non genericType
                                when (dataBaseType) {
                                    DataBaseType.Tariff -> {
                                        async { getTaiff() }.await()
                                    }
                                    DataBaseType.CheckList -> {
                                        async { getCheckListDictionaryUseCase.getCheckListDictionaryFromServer() }.await()
                                        async { getCheckListAllTypeUseCase.getCheckListAllTypeFromServer() }.await()
                                        async { getCheckListMapUseCase.getCheckListMapFromServer() }.await()
                                    }
                                    else -> {}
                                }
                                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
                                val dataBaseVersion = dataBaseVersionFromServer.find { it.id == dataBaseType.nameInDb }
                                dataBaseVersion?.dateLastUpdate = date
                                async { dataTapoDb.dataBaseVersion().insert(dataBaseVersion!!) }.await()
                            }
                        }


                    }
                }
            }
            withContext(Dispatchers.Main) {
                State.dialogDownloadMessage.value = ""
            }

        }



    }



    private suspend fun getTaiff() {
        MainScope().async(Dispatchers.IO) {
            var responseFromDb: List<Tariff>? = null
            async { responseFromDb = tapoDb.tariffDb().getAll() }.await()
            val response = networkClient.getTariffData()

            if (responseFromDb?.isNotEmpty() == true) {
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
                            elementFind.court = it.court
                            elementFind.remarks = it.remarks
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
        }.await()
    }

}