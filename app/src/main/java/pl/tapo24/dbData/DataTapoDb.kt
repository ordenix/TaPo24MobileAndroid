package pl.tapo24.dbData

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import pl.tapo24.dbData.dao.*
import pl.tapo24.dbData.entity.*

@Database(
    entities = [
        DataBaseVersion::class,
        CodeDrivingLicence::class,
        CountryDrivingLicence::class,
        LightsCodeCountry::class,
        LightsFront::class,
        LightsOthers::class,
        Status::class,
        Towing::class
    ], version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4)

    ]

)
abstract class DataTapoDb: RoomDatabase() {
    abstract fun dataBaseVersion(): DataBaseVersionDao

    abstract fun codeDrivingLicence(): CodeDrivingLicenceDao

    abstract fun countryDivingLicence(): CountryDrivingLicenceDao

    abstract fun lightsCodeCountry(): LightsCodeCountryDao

    abstract fun lightsFront(): LightsFrontDao

    abstract fun  lightsOthers(): LightsOthersDao
    abstract fun status(): StatusDao
    abstract fun towing(): TowingDao
}