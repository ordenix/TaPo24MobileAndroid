package pl.tapo24.twa.updater

import android.content.Context
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import javax.inject.Inject

class InitPackageDownloader @Inject constructor(
    private val context: Context,
    private val networkClient: NetworkClient,
    private val tapoDb: TapoDb,
    private val dataTapoDb: DataTapoDb,
)

{

    fun downloadInitPackage() {
        // TODO
    }
}