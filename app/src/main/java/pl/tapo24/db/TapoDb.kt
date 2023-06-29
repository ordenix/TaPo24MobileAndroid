package pl.tapo24.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import pl.tapo24.db.dao.SettingDao
import pl.tapo24.db.dao.WhatsNewsDao
import pl.tapo24.db.entity.Setting
import pl.tapo24.db.entity.WhatsNews

@Database(
    entities = [
        Setting::class,
        WhatsNews::class
    ], version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4)
    ]

)
abstract class TapoDb: RoomDatabase() {
    abstract fun settingDb(): SettingDao
    abstract fun whatsNewsDb(): WhatsNewsDao
}