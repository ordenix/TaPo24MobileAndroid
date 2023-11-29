package pl.tapo24.twa.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import pl.tapo24.twa.db.dao.*
import pl.tapo24.twa.db.entity.*

@Database(
    entities = [
        Setting::class,
        WhatsNews::class,
        Tariff::class,
        LastSearch::class,
        AssetList::class,
        AppVersion::class,
        CustomCategory::class,
        MapCategory::class
    ], version = 8,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4),
        AutoMigration (from = 4, to = 5),
        AutoMigration (from = 5, to = 6),
        AutoMigration (from = 6, to = 7),
        AutoMigration (from = 7, to = 8)


    ]

)
abstract class TapoDb: RoomDatabase() {
    abstract fun settingDb(): SettingDao
    abstract fun whatsNewsDb(): WhatsNewsDao
    abstract fun tariffDb(): TariffDao
    abstract fun  lastSearchDb(): LastSearchDao
    abstract fun assetListDb(): AssetListDao
    abstract fun appVersionDb(): AppVersionDao

    abstract fun mapCategory(): MapCategoryDao

    abstract fun customCategory(): CustomCategoryDao
}