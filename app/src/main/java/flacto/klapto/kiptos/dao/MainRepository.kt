package flacto.klapto.kiptos.dao

import android.content.Context
import androidx.lifecycle.LiveData
import flacto.klapto.kiptos.api.RetrofitInstance
import flacto.klapto.kiptos.db.NumbersDatabase
import flacto.klapto.kiptos.model.NumberDataFullEntity
import flacto.klapto.kiptos.model.NumberDataRaw
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

class MainRepository(
    val context: Context
) {


    private val db by lazy { NumbersDatabase(context) }


    fun getAllNumbersDataLiveData(): LiveData<List<NumberDataFullEntity>> =
        db.getNumbersDao().getAllNumbersDataLiveData()


    suspend fun upsert(item: NumberDataFullEntity) {
        db.getNumbersDao().upsert(item)
    }

    private suspend fun getByNumberTrivia(string: String): Response<NumberDataRaw> {
        return RetrofitInstance.api.getByNumberTrivia(string)
    }

    private suspend fun getByNumberMath(string: String): Response<NumberDataRaw> {
        return RetrofitInstance.api.getByNumberMath(string)
    }

    private suspend fun getByNumberDate(string: String): Response<NumberDataRaw> {
        return RetrofitInstance.api.getByNumberDate(string)
    }


    suspend fun getAllNumberFacts(number: String): Triple<Response<NumberDataRaw>, Response<NumberDataRaw>, Response<NumberDataRaw>> {
        return coroutineScope {
            val triviaDeferred = async { getByNumberTrivia(number) }
            val mathDeferred = async { getByNumberMath(number) }
            val dateDeferred = async { getByNumberDate(number) }

            val triviaResponse = triviaDeferred.await()
            val mathResponse = mathDeferred.await()
            val dateResponse = dateDeferred.await()

            Triple(triviaResponse, mathResponse, dateResponse)
        }
    }


    suspend fun clearAllData() {
        db.getNumbersDao().clearDatabase()
    }

}