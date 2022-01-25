package by.romanovich.theweatherapp.repository

import by.romanovich.theweatherapp.BuildConfig
import by.romanovich.theweatherapp.model.getRussianCities
import by.romanovich.theweatherapp.model.getWorldCities
import by.romanovich.theweatherapp.utils.YANDEX_API_KEY
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request


//реализация
class RepositoryImpl : RepositoryCitiesList, RepositoryDetails {

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()


    override fun getWeatherFromServer(url: String, callback: Callback) {
        //настраиваем билдер
        val builder = Request.Builder().apply {
            //заголовок
            header(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            //адрес
            // url(YANDEX_API_URL + YANDEX_API_URL_END_POINT +"?lat=${localWeather.city.lat}&lon=${localWeather.city.lon}")}
            url(url)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }
}