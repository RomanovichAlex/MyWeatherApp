package by.romanovich.theweatherapp.repository

import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.YANDEX_API_KEY
import by.romanovich.theweatherapp.utils.YANDEX_API_URL_END_POINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {
    //перед функцией указываем
    @GET(YANDEX_API_URL_END_POINT)
    //шаблон запроса
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    //ответ с типом везер дто
    ): Call<WeatherDTO>
}