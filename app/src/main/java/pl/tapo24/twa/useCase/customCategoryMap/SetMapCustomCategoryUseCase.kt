package pl.tapo24.twa.useCase.customCategoryMap

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.MapCategory
import pl.tapo24.twa.utils.CheckConnection
import javax.inject.Inject

class SetMapCustomCategoryUseCase @Inject constructor(
    private val context: Context,
    private val tapoDb: TapoDb
) {

    suspend fun runMap(tariffId: String, categoryId: Int) {
        var mapFromDb: MapCategory? = null
        MainScope().async(Dispatchers.IO) {
            mapFromDb = async { tapoDb.mapCategory().getByTariffIdAndCategoryId(tariffId, categoryId) }.await()
        }.await()
        if (mapFromDb == null) {
            // not exist
            mapFromDb = MapCategory(
                tariffId = tariffId,
                customCategoryId = categoryId
            )
            sendMap(mapFromDb!!)
        } else {
            // exist
            if (mapFromDb!!.toDelete && mapFromDb!!.dataSynchronized) {
                // exist and is to delete
                mapFromDb!!.toDelete = false
                tapoDb.mapCategory().insert(mapFromDb!!)
            } else {
                // exist and is not to delete
                mapFromDb!!.toDelete = true
                sendMap(mapFromDb!!)
            }
        }

    }
    private suspend fun sendMap(mapCategory: MapCategory): Result<String> {
        if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {

        } else {

        }
    }
}