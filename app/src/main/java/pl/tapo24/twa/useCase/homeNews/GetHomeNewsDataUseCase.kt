package pl.tapo24.twa.useCase.homeNews

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.WhatsNews
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.utils.CheckConnection
import javax.inject.Inject

class GetHomeNewsDataUseCase @Inject constructor(
    private val networkClient: NetworkClient,
    private val tapoDb: TapoDb,
    @ApplicationContext private val context: Context
) {


    suspend fun getData(): List<WhatsNews>? {
        var listToReturn: List<WhatsNews>? = null
        MainScope().async(Dispatchers.IO) {
            if (CheckConnection().getConnectionType(context) != NetworkTypes.None) {
                networkClient.getHomeNewsData().onSuccess { listDataHomeNews ->
                    if (listDataHomeNews.isNotEmpty()) {
                        async { tapoDb.whatsNewsDb().deleteAll() }.await()
                        val listDataToDb: MutableList<WhatsNews> = mutableListOf()
                        listDataHomeNews.forEach { dataHomeNews ->
                            listDataToDb.add(
                                WhatsNews(
                                 id =dataHomeNews.id!!.toInt(),
                                 date = dataHomeNews.date!!.toInt(),
                                 title = "",
                                 p1 = dataHomeNews.htmlText!!,
                                 p2 = "",
                                 p3 = "",
                                 p4 = "",
                                 footerBold = "",
                                 footer = "",
                                 author = dataHomeNews.author!!
                                )
                            )
                        }
                        listToReturn = listDataToDb
                        async { tapoDb.whatsNewsDb().insertAll(listDataToDb) }.await()
                    }
                }
            } else {
                listToReturn = async { tapoDb.whatsNewsDb().getAll() }.await()
            }
        }.await()
        return listToReturn?.sortedByDescending { it.id }
    }
}