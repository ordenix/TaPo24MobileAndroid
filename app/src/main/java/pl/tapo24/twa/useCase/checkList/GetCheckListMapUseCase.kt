package pl.tapo24.twa.useCase.checkList

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CheckListMap
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class GetCheckListMapUseCase @Inject constructor(private val  tapoDb: TapoDb,
                                                 private val networkClient: NetworkClient
){
    //fun restoreby id




    suspend fun getCheckListMapFromServer() {
        MainScope().async(Dispatchers.IO) {
//            val listFromDb = async {tapoDb.checkListMap().getAll()}.await()
            val response = async {networkClient.getCheckListAllMap()}.await()
            response.onSuccess {
                async {tapoDb.checkListMap().nukeTable()}.await()
                val listToDb = mutableListOf<CheckListMap>()
                it.forEach { itemFromServer ->
                    val element = CheckListMap(
                        id = itemFromServer.id,
                        sortOrder = itemFromServer.sortOrder,
                        checkListType = itemFromServer.checkListType.id,
                        checkListDictionary = itemFromServer.checkListDictionary.id,

                    )
                    listToDb.add(element)
                }
                async {tapoDb.checkListMap().insertAll(listToDb)}.await()
//                if (listFromDb.isEmpty()) {
//                    async {tapoDb.checkListMap().insertAll(listFromServer)}.await()
//                } else  {
//                    listFromServer.forEach { itemFromServer ->
//                        if (listFromDb.find { it.id == itemFromServer.id } == null) {
//                            // not in db
//                            async {tapoDb.checkListMap().insert(itemFromServer)}.await()
//                        } else {
//                           // async {tapoDb.checkListMap().update(itemFromServer)}.await()
//                        }
//                    }
//                }

            }
        }.await()

    }

}