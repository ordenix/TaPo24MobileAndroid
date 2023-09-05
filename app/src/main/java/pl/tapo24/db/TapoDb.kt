package pl.tapo24.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import pl.tapo24.db.dao.LastSearchDao
import pl.tapo24.db.dao.SettingDao
import pl.tapo24.db.dao.TariffDao
import pl.tapo24.db.dao.WhatsNewsDao
import pl.tapo24.db.entity.LastSearch
import pl.tapo24.db.entity.Setting
import pl.tapo24.db.entity.Tariff
import pl.tapo24.db.entity.WhatsNews

@Database(
    entities = [
        Setting::class,
        WhatsNews::class,
        Tariff::class,
        LastSearch::class
    ], version = 1,
    exportSchema = true,
    autoMigrations = [
//        AutoMigration (from = 1, to = 2),
//        AutoMigration (from = 2, to = 3)

    ]

)
abstract class TapoDb: RoomDatabase() {
    abstract fun settingDb(): SettingDao
    abstract fun whatsNewsDb(): WhatsNewsDao
    abstract fun tariffDb(): TariffDao
    abstract fun  lastSearchDb(): LastSearchDao
}