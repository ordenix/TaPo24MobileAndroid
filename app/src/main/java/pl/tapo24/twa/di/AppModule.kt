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
import pl.tapo24.twa.SessionProvider
import pl.tapo24.twa.module.FavouriteModule
import pl.tapo24.twa.module.InitializationModule
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.EnvironmentType
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.infrastructure.NetworkClientAdmin
import pl.tapo24.twa.infrastructure.NetworkClientElastic
import pl.tapo24.twa.infrastructure.NetworkClientRegister
import pl.tapo24.twa.module.CustomCategoryModule
import pl.tapo24.twa.module.PremiumShopModule
import pl.tapo24.twa.updater.*
import pl.tapo24.twa.useCase.RegenerateJwtTokenUseCase
import pl.tapo24.twa.useCase.ResetPremiumLimitationUseCase
import pl.tapo24.twa.useCase.checkList.*
import pl.tapo24.twa.useCase.customCategory.*
import pl.tapo24.twa.useCase.customCategoryMap.PrepareMapListToTariffUseCase
import pl.tapo24.twa.useCase.customCategoryMap.SetMapCustomCategoryUseCase
import pl.tapo24.twa.useCase.customCategoryMap.SynchronizeCustomCategoryMapUseCase
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
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDataTapoDb(@ApplicationContext app: Context): DataTapoDb {
        return Room.databaseBuilder(
            app,
            DataTapoDb::class.java,
            "DataTapo24.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNetwork(tapoDb: TapoDb): NetworkClient {
        var url: String?
        runBlocking(Dispatchers.IO) {
            val settingEnvironment: Setting? = tapoDb.settingDb().getSettingByName("settingEnvironment")
            if (settingEnvironment != null) {
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
    fun provideRegisterNetwork(tapoDb: TapoDb): NetworkClientRegister {
        var url: String?
        runBlocking(Dispatchers.IO) {
            val settingEnvironment: Setting? = tapoDb.settingDb().getSettingByName("settingEnvironment")
            if (settingEnvironment != null) {
                val environment = EnvironmentType.values()[settingEnvironment.count]
                url = environment.url
            } else {
                url = "https://api3.tapo24.pl/api/"

            }


        }

        return NetworkClientRegister(url!!)
    }

    @Provides
    @Singleton
    fun provideAdminNetwork(tapoDb: TapoDb): NetworkClientAdmin {
        var url: String?
        runBlocking(Dispatchers.IO) {
            val settingEnvironment: Setting? = tapoDb.settingDb().getSettingByName("settingEnvironment")
            if (settingEnvironment != null) {
                val environment = EnvironmentType.values()[settingEnvironment.count]
                url = environment.url
            } else {
                url = "https://api3.tapo24.pl/api/"

            }


        }

        return NetworkClientAdmin(url!!)
    }

    @Provides
    @Singleton
    fun provideNetworkElastic(tapoDb: TapoDb): NetworkClientElastic {
        var url: String?
        runBlocking(Dispatchers.IO) {
            val settingEnvironment: Setting? = tapoDb.settingDb().getSettingByName("settingEngine")
            if (settingEnvironment != null) {
                val engine = EnginesType.values()[settingEnvironment.count]
                url = engine.url
            } else {
                url = "https://elastic-enterprise-search.server.tapo24.pl/api/as/v1/engines/tapo24new/"

            }
        }

        return NetworkClientElastic(url!!)
    }


    @Provides
    @Singleton
    fun bindMourningCheck(tapoDb: TapoDb, networkClient: NetworkClient): MourningCheck {
        return MourningCheck(tapoDb, networkClient)
    }


    @Provides
    @Singleton
    fun bindFavouriteModule(tapoDb: TapoDb, networkClient: NetworkClient): FavouriteModule {
        return FavouriteModule(tapoDb, networkClient)
    }

    @Provides
    @Singleton
    fun bindInitializationModule(
        tapoDb: TapoDb,
        networkClient: NetworkClient,
        @ApplicationContext app: Context
    ): InitializationModule {
        return InitializationModule(tapoDb, networkClient, app)
    }

    @Provides
    @Singleton
    fun bindAddCustomCategory(tapoDb: TapoDb, @ApplicationContext app: Context): AddCustomCategoryUseCase {
        return AddCustomCategoryUseCase(tapoDb, app)
    }


    @Provides
    @Singleton
    fun bindCustomCategoryModule(
        @ApplicationContext app: Context,
        tapoDb: TapoDb,
        networkClient: NetworkClient
    ): CustomCategoryModule {
        return CustomCategoryModule(app, tapoDb, networkClient)
    }

    @Provides
    @Singleton
    fun bindChangeNameCustomCategoryUseCase(
        @ApplicationContext app: Context,
        tapoDb: TapoDb
    ): ChangeNameCustomCategoryUseCase {
        return ChangeNameCustomCategoryUseCase(app, tapoDb)
    }

    @Provides
    @Singleton
    fun bindSynchronizeCustomCategoryUseCase(
        @ApplicationContext app: Context,
        tapoDb: TapoDb,
        networkClient: NetworkClient
    ): SynchronizeCustomCategoryUseCase {
        return SynchronizeCustomCategoryUseCase(app, tapoDb, networkClient)
    }

    @Provides
    @Singleton
    fun bindChangeOrderCustomCategoryUseCase(
        @ApplicationContext app: Context,
        tapoDb: TapoDb
    ): ChangeOrderCustomCategoryUseCase {
        return ChangeOrderCustomCategoryUseCase(app, tapoDb)
    }

    @Provides
    @Singleton
    fun bindDeleteCustomCategoryUseCase(@ApplicationContext app: Context, tapoDb: TapoDb): DeleteCustomCategoryUseCase {
        return DeleteCustomCategoryUseCase(app, tapoDb)
    }

    @Provides
    @Singleton
    fun bindPrepareMapListToTariffUseCase(tapoDb: TapoDb): PrepareMapListToTariffUseCase {
        return PrepareMapListToTariffUseCase(tapoDb)
    }

    @Provides
    @Singleton
    fun bindSetMapCustomCategoryUseCase(@ApplicationContext app: Context, tapoDb: TapoDb): SetMapCustomCategoryUseCase {
        return SetMapCustomCategoryUseCase(app, tapoDb)
    }

    @Provides
    @Singleton
    fun bindSynchronizeCustomCategoryMapUseCase(
        @ApplicationContext app: Context,
        tapoDb: TapoDb
    ): SynchronizeCustomCategoryMapUseCase {
        return SynchronizeCustomCategoryMapUseCase(app, tapoDb)
    }

    @Provides
    @Singleton
    fun bindRegenerateJwtTokenUseCase(networkClient: NetworkClient, tapoDb: TapoDb, @ApplicationContext app: Context): RegenerateJwtTokenUseCase {
        return RegenerateJwtTokenUseCase(networkClient, tapoDb, app)
    }

    @Provides
    @Singleton
    fun bindShowNotifyForTariffIconUseCase(tapoDb: TapoDb): ResetPremiumLimitationUseCase {
        return ResetPremiumLimitationUseCase(tapoDb)
    }

    @Provides
    @Singleton
    fun bindPremiumShopModule(
        tapoDb: TapoDb,
        networkClient: NetworkClient,
        @ApplicationContext app: Context
    ): PremiumShopModule {
        return PremiumShopModule(tapoDb, networkClient, app)
    }

    @Provides
    @Singleton
    fun bindGetCheckListAllTypeUseCase(tapoDb: TapoDb, networkClient: NetworkClient): GetCheckListAllTypeUseCase {
        return GetCheckListAllTypeUseCase(tapoDb, networkClient)
    }

    @Provides
    @Singleton
    fun bindGetCheckListMapUseCase(tapoDb: TapoDb, networkClient: NetworkClient): GetCheckListMapUseCase {
        return GetCheckListMapUseCase(tapoDb, networkClient)
    }

    @Provides
    @Singleton
    fun bindChangeMapState(tapoDb: TapoDb): ChangeMapStateUseCase {
        return ChangeMapStateUseCase(tapoDb)
    }

    @Provides
    @Singleton
    fun bindGetCheckListDictionaryUseCase(tapoDb: TapoDb, networkClient: NetworkClient): GetCheckListDictionaryUseCase {
        return GetCheckListDictionaryUseCase(tapoDb, networkClient)
    }

    @Provides
    @Singleton
    fun bindGetComplexCheckListByType(tapoDb: TapoDb): GetComplexCheckListByType {
        return GetComplexCheckListByType(tapoDb)
    }


    @Provides
    @Singleton
    fun bindDataBaseUpdater(
        tapoDb: TapoDb, dataTapoDb: DataTapoDb,
        networkClient: NetworkClient,
        @ApplicationContext app: Context,
        getCheckListDictionaryUseCase: GetCheckListDictionaryUseCase,
        getCheckListAllTypeUseCase: GetCheckListAllTypeUseCase,
        getCheckListMapUseCase: GetCheckListMapUseCase,
    ): DataBaseUpdater {
        return DataBaseUpdater(
            app,
            networkClient,
            tapoDb,
            dataTapoDb, getCheckListDictionaryUseCase,
            getCheckListAllTypeUseCase,
            getCheckListMapUseCase
        )
    }

    @Provides
    @Singleton
    fun bindLawUpdater(
        tapoDb: TapoDb, dataTapoDb: DataTapoDb,
        networkClient: NetworkClient,
        @ApplicationContext app: Context,

        ): LawUpdater {
        return LawUpdater(
            app,
            networkClient,
            tapoDb,
            dataTapoDb
        )
    }

//    @Provides
//    @Singleton
//    fun bindInitPackageDownloader(
//        tapoDb: TapoDb, dataTapoDb: DataTapoDb,
//        networkClient: NetworkClient,
//        @ApplicationContext app: Context,
//
//        ): InitPackageDownloader {
//        return InitPackageDownloader(
//            app,
//            networkClient,
//            tapoDb,
//            dataTapoDb
//        )
//
//    }


    @Provides
    @Singleton
    fun bindAssetUpdater(
        tapoDb: TapoDb, dataTapoDb: DataTapoDb,
        networkClient: NetworkClient,
        @ApplicationContext app: Context,

        ): AssetUpdater {
        return AssetUpdater(
            app,
            networkClient,
            tapoDb,
            dataTapoDb
        )

    }

    @Provides
    @Singleton
    fun bindSessionProvider(
        tapoDb: TapoDb,
        networkClient: NetworkClient,
        @ApplicationContext app: Context,
        favouriteModule: FavouriteModule
    ): SessionProvider {
        return SessionProvider(tapoDb, networkClient, app, favouriteModule)
    }

}