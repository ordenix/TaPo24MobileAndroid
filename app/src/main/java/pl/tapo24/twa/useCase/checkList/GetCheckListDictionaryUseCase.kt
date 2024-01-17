package pl.tapo24.twa.useCase.checkList

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CheckListDictionary
import pl.tapo24.twa.db.entity.CheckListType
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class GetCheckListDictionaryUseCase @Inject constructor(private val  tapoDb: TapoDb,
                                                        private val networkClient: NetworkClient
){

    suspend fun returnCheckListDictionary(): List<CheckListDictionary> {
        var list = listOf<CheckListDictionary>()
        MainScope().async(Dispatchers.IO) {
            list = async {tapoDb.checkListDictionary().getAll()}.await()
        }.await()
        return list

    }


    suspend fun getCheckListDictionaryFromServer() {
        MainScope().async(Dispatchers.IO) {
            val response = async {networkClient.getCheckListDictionary()}.await()
            response.onSuccess {
                async {tapoDb.checkListDictionary().insertAll(it)}.await()
            }
        }.await()

    }

}