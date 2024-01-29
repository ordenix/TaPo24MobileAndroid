package pl.tapo24.twa.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
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
        MapCategory::class,
        Mourning::class,
        CheckListDictionary::class,
        CheckListMap::class,
        CheckListType::class,
    ], version = 13,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4),
        AutoMigration (from = 4, to = 5),
        AutoMigration (from = 5, to = 6),
        AutoMigration (from = 6, to = 7),
        AutoMigration (from = 7, to = 8),
        AutoMigration (from = 8, to = 9),
        AutoMigration (from = 9, to = 10),
        AutoMigration (from = 10, to = 11, spec = TapoDb.Migration11::class),
        AutoMigration (from = 11, to = 12),
        AutoMigration (from = 12, to = 13),


    ]

)
abstract class TapoDb: RoomDatabase() {
    @RenameColumn(tableName = "CustomCategory", fromColumnName ="synchronized", toColumnName = "dataSynchronized")
    @RenameColumn(tableName = "MapCategory", fromColumnName ="synchronized", toColumnName = "dataSynchronized")
    class Migration11: AutoMigrationSpec
    abstract fun settingDb(): SettingDao
    abstract fun whatsNewsDb(): WhatsNewsDao
    abstract fun tariffDb(): TariffDao
    abstract fun  lastSearchDb(): LastSearchDao
    abstract fun assetListDb(): AssetListDao
    abstract fun appVersionDb(): AppVersionDao

    abstract fun mapCategory(): MapCategoryDao

    abstract fun customCategory(): CustomCategoryDao

    abstract fun mourning(): MourningDao

    abstract fun checkListDictionary(): CheckListDictionaryDao

    abstract fun checkListMap(): CheckListMapDao

    abstract fun checkListType(): CheckListTypeDao
}