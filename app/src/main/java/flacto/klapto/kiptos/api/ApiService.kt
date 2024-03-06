package flacto.klapto.kiptos.api

import flacto.klapto.kiptos.model.NumberDataRaw
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("{number}?json")
    suspend fun getByNumberTrivia(
        @Path("number") string: String,
    ): Response<NumberDataRaw>

    @GET("{number}/math?json")
    suspend fun getByNumberMath(
        @Path("number") string: String,
    ): Response<NumberDataRaw>

    @GET("{number}/date?json")
    suspend fun getByNumberDate(
        @Path("number") string: String,
    ): Response<NumberDataRaw>



}