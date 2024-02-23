package pl.tapo24.twa.dbData

import androidx.room.Database
import androidx.room.AutoMigration
import androidx.room.RoomDatabase
import pl.tapo24.twa.dbData.dao.*
import pl.tapo24.twa.dbData.entity.*

@Database(
    entities = [
        DataBaseVersion::class,
        CodeDrivingLicence::class,
        CountryDrivingLicence::class,
        LightsCodeCountry::class,
        LightsFront::class,
        LightsOthers::class,
        Status::class,
        Towing::class,
        Story::class,
        ControlList::class,
        CodePointsNew::class,
        CodePointsOld::class,
        CodeLimitsDrivingLicence::class,
        HoldingDocuments::class,
        Uto::class,
        Sign::class,
        Law::class,
        Spb::class,
        TelephoneNumbers::class,
        CodeColors::class

    ], version = 7,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4),
        AutoMigration (from = 4, to = 5),
        AutoMigration (from = 5, to = 6),
        AutoMigration (from = 6, to = 7),
//        AutoMigration (from = 7, to = 8),
//        AutoMigration (from = 8, to = 9),
//        AutoMigration (from = 9, to = 10),
//        AutoMigration (from = 10, to = 11),
//        AutoMigration (from = 11, to = 12),
//        AutoMigration (from = 12, to = 13)



    ],

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

    abstract fun story(): StoryDao
    abstract fun holdingDocuments(): HoldingDocumentsDao
    abstract fun controlList(): ControlListDao
    abstract fun codePointsNew(): CodePointsNewDao
    abstract fun codePointsOld(): CodePointsOldDao
    abstract fun codeLimitsDrivingLicence(): CodeLimitsDrivingLicenceDao

    abstract fun uto(): UtoDao

    abstract fun sign(): SignDao

    abstract fun law(): LawDao

    abstract fun spb(): SpbDao

    abstract fun telephoneNumbers(): TelephoneNumbersDao

    abstract fun codeColors(): CodeColorsDao

}