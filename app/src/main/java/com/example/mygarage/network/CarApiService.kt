package com.example.mygarage.network

import com.example.mygarage.model.CarInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL =
    "https://raw.githubusercontent.com/ElyesDer/Vehicule-data-DB/master/"

var gson: Gson = GsonBuilder()
    .setLenient()
    .create()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


/**
 * A public interface that exposes the [getMyCarInfo] method
 */
interface MyCarApiService {

    /**
     * Returns a [List] of [MyCarInfo] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "year" endpoint will be requested with the GET
     * HTTP method
     */
    @Headers("Content-Type: application/json")
    @GET("jsondata.json")
    suspend fun getMyCarInfo(): List<CarInfo>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MyCarApi {
    val retrofitService: MyCarApiService by lazy { retrofit.create(MyCarApiService::class.java) }
}