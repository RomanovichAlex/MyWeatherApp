package by.romanovich.theweatherapp.repository

import by.romanovich.theweatherapp.BuildConfig
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.YANDEX_API_URL
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//реализация
class RepositoryRemoteImpl : RepositoryDetails {

    private val retrofit = Retrofit.Builder()
        .baseUrl(YANDEX_API_URL)
        //к которому добавляются наши эндпоинты
        .addConverterFactory(GsonConverterFactory.create(
            //setLenient- предусматривает косяки с gson
            GsonBuilder().setLenient().create()
        ))
        //бостроили и связали его с нашим интерфейсом запросов
        .build().create(WeatherApi::class.java)

    override fun getWeatherFromServer(lat:Double,lon:Double, callback: Callback<WeatherDTO>) {
        retrofit.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).enqueue(callback)
    }
}