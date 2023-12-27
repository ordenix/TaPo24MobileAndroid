package pl.tapo24.twa.useCase.customCategoryMap

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.data.customCategory.CategoryToMap
import pl.tapo24.twa.db.TapoDb
import javax.inject.Inject

class PrepareMapListToTariffUseCase @Inject constructor(
    private val tapoDb: TapoDb
) {

    suspend fun run(tariffId: String): List<CategoryToMap> {
        val categoryMapList: MutableList<CategoryToMap> = mutableListOf()
        MainScope().async(Dispatchers.IO) {
            val categoryListFromDb = async { tapoDb.customCategory().getAllWithoutDeleted() }.await()
            categoryListFromDb.forEach { category ->
                val exist = async { tapoDb.mapCategory().existByTariffIdAndCategoryId(tariffId, category.id) }.await()
                val categoryMap = CategoryToMap(
                    selected = exist,
                    categoryId = category.id,
                    categoryName = category.categoryName
                )
                categoryMapList.add(categoryMap)
            }
        }.await()
        return categoryMapList
    }
}