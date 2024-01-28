package pl.tapo24.twa.useCase.checkList

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.data.checkListMap.CheckListComplex
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.CheckListDictionary
import pl.tapo24.twa.db.entity.CheckListMap
import javax.inject.Inject

class GetComplexCheckListByType @Inject constructor(private val tapoDb: TapoDb
) {
    suspend fun getCheckListByTypeNonDeleted(type: Int): MutableList<CheckListComplex> {
        var returnList = mutableListOf<CheckListComplex>()
        MainScope().async(Dispatchers.IO) {
            val dictionary = async {getAllDictionary()}.await()
            val map = async {getCheckListMapByType(type)}.await()

            map.forEach { item ->
                val element = CheckListComplex(
                    mapId = item.id,
                    sortOrder = item.sortOrder,
                    name = dictionary.find { elementDictionary ->
                        elementDictionary.id == item.checkListDictionary
                    }?.description ?: "",
                    isSelected = item.isSelected,
                    isDeleted = item.isDeleted
                )
                returnList.add(element)
            }
        }.await()
        return returnList
    }

    private suspend fun getCheckListMapByType(type: Int):  List<CheckListMap> {
        var returnList = listOf<CheckListMap>()
        MainScope().async(Dispatchers.IO) {
            returnList = async { tapoDb.checkListMap().getByTypeNonDeleted(type)}.await()
        }.await()
        return returnList
    }


    private suspend fun getAllDictionary():  List<CheckListDictionary> {
        var returnList = listOf<CheckListDictionary>()
        MainScope().async(Dispatchers.IO) {
            returnList = async { tapoDb.checkListDictionary().getAll()}.await()
        }.await()
        return returnList
    }
}