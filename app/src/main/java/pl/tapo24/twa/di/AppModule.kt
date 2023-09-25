package pl.tapo24.twa.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.EnvironmentType
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.infrastructure.NetworkClientElastic
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideTapoDb(@ApplicationContext app: Context): TapoDb {
        return Room.databaseBuilder(
            app,
            TapoDb::class.java,
            "Tapo24.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataTapoDb(@ApplicationContext app: Context): DataTapoDb {
        return Room.databaseBuilder(
            app,
            DataTapoDb::class.java,
            "DataTapo24.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNetwork(tapoDb: TapoDb): NetworkClient {
        var url : String?
        runBlocking(Dispatchers.IO) {
            val settingEnvironment: Setting?  = tapoDb.settingDb().getSettingByName("settingEnvironment")
            if (settingEnvironment !=null) {
                val environment = EnvironmentType.values()[settingEnvironment.count]
                url = environment.url
            } else {
                url = "https://api3.tapo24.pl/api/"

            }


        }

        return NetworkClient(url!!)
    }

    @Provides
    @Singleton
    fun provideNetworkElastic(tapoDb: TapoDb): NetworkClientElastic {
        var url : String?
        runBlocking(Dispatchers.IO) {
            val settingEnvironment: Setting?  = tapoDb.settingDb().getSettingByName("settingEngine")
            if (settingEnvironment !=null) {
                val engine = EnginesType.values()[settingEnvironment.count]
                url = engine.url
            } else {
                url = "https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24new/"

            }
        }

        return NetworkClientElastic(url!!)
    }
}