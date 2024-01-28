package pl.tapo24.twa.useCase.checkList

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.twa.data.checkListMap.CheckListComplex
import pl.tapo24.twa.db.TapoDb
import javax.inject.Inject

class ChangeMapStateUseCase @Inject constructor(private val tapoDb: TapoDb
) {

    fun updateItem(item: CheckListComplex) {
        MainScope().launch(Dispatchers.IO) {
            val itemFromDb = async {tapoDb.checkListMap().getById(item.mapId)}.await()
            itemFromDb.apply {
                isSelected = item.isSelected
                isDeleted = item.isDeleted
                sortOrder = item.sortOrder
            }
            async {tapoDb.checkListMap().insert(itemFromDb)}.await()
        }


    }

    fun unSelectAll(idList: Int) {
        MainScope().launch(Dispatchers.IO) {
            tapoDb.checkListMap().unSelectAll(idList)
        }

    }

    suspend fun restoreList(idList: Int) {
        MainScope().async(Dispatchers.IO) {
            tapoDb.checkListMap().restoreList(idList)
        }.await()

    }
}