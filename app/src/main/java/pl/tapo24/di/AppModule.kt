package pl.tapo24.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import pl.tapo24.db.TapoDb
import pl.tapo24.db.entity.Setting
import pl.tapo24.infrastructure.NetworkClient
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
    fun provideNetwork(tapoDb: TapoDb): NetworkClient {
        var url : String?
        runBlocking(Dispatchers.IO) {
            val settingEnvironmentTest: Setting?  = tapoDb.settingDb().getSettingByName("environmentTest")
            if (settingEnvironmentTest == null) {
                url = "https://api3.tapo24.pl/api/"
            } else {
                if (settingEnvironmentTest.state) {
                    url = "https://develop.api3.tapo24.pl/api/"

                } else {
                    url = "https://api3.tapo24.pl/api/"
                }
            }

        }

        return NetworkClient(url!!)
    }
}