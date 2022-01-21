package by.romanovich.theweatherapp.utils

import android.os.Handler
import android.os.Looper
import by.romanovich.theweatherapp.BuildConfig
import by.romanovich.theweatherapp.model.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


//получаем реализацию интерфейса
class WeatherLoader(private val onWeatherLoaded: OnWeatherLoaded) {
    //загрузить погоду, запрос
    fun loadWeather(lat: Double, lon: Double) {

//по ссылке яндекса
        Thread {
            val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
            // открываем соединение
            val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"
                readTimeout = 2000
                //задали заголовок, передаем ключ апи и личный ключ
                addRequestProperty(API_KEY, BuildConfig.WEATHER_API_KEY)
            }
            //в буфере жсон данные, конвертируем в дто из строки. открываем на чтение с считываем в буфер
            val bufferedReader =
                BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
// преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
            val weatherDTO: WeatherDTO? =
                Gson().fromJson(convertBufferToResult(bufferedReader), WeatherDTO::class.java)
            //а внутри везер дто все инструменты что нам нужны
            Handler(Looper.getMainLooper()).post {
                onWeatherLoaded.onLoaded(weatherDTO)
            }
        }.start()
    }

    // получили данные и собрали их в одну кучу где на выходе будет строка
    private fun convertBufferToResult(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

    //для передачи везердто в фрагмент
    interface OnWeatherLoaded {
        //будет загружена погода
        fun onLoaded(weatherDTO: WeatherDTO?)
        fun onFailed(e: Throwable)
    }
}





