package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.TelephoneNumbers
@Dao
interface TelephoneNumbersDao {

    @Query("SELECT * FROM telephoneNumbers")
    fun getAll(): List<TelephoneNumbers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<TelephoneNumbers>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: TelephoneNumbers)

    @Query("DELETE FROM telephoneNumbers")
    fun deleteAll()

}

//fun getAll(): List<>
//fun insertList(items: List<>)
//
//fun insert(items:)
//fun deleteAll()