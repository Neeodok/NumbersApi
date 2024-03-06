package flacto.klapto.kiptos.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import flacto.klapto.kiptos.model.NumberDataFullEntity

@Dao
interface NumbersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(numberDataFullEntity: NumberDataFullEntity): Long


    @Query("SELECT DISTINCT * FROM numbers ORDER BY id DESC")
    fun getAllNumbersDataLiveData(): LiveData<List<NumberDataFullEntity>>

    @Query("DELETE FROM numbers")
    suspend fun clearDatabase()


}
