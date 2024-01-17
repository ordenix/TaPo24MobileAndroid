package pl.tapo24.twa.useCase.checkList

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CheckListType
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class GetCheckListAllTypeUseCase @Inject constructor(private val  tapoDb: TapoDb,
                                                     private val networkClient: NetworkClient
) {

    suspend fun returnCheckListAllType(): List<CheckListType> {
        var list = listOf<CheckListType>()
        MainScope().async(Dispatchers.IO) {
            list = async {tapoDb.checkListType().getAll()}.await()
        }.await()
        return list

    }


    suspend fun getCheckListAllTypeFromServer() {
        MainScope().async(Dispatchers.IO) {
            val response = async {networkClient.getAllTypeList()}.await()
            response.onSuccess {
                async {tapoDb.checkListType().insertAll(it)}.await()
            }
        }.await()

    }
}