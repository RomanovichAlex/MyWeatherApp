package by.romanovich.theweatherapp.view.details

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.romanovich.theweatherapp.BuildConfig
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.API_KEY
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection



//Работаем в сервисе как в активити, т.е. у нас есть контекст, но нет вью, заточен на работу фоново
//IntentService отличается от сервис выполняется в новом потоке
class DetailsService(name:String= ""): IntentService(name) {

    //достали широту долготу
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = it.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
            loadWeather(lat, lon)
        }
    }


    fun loadWeather(lat: Double, lon: Double) {
//по ссылке яндекса
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")

        val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
            //гет запрос
            requestMethod = "GET"
            readTimeout = 2000
            //задали заголовок, передаем ключ апи и личный ключ
            addRequestProperty(API_KEY, BuildConfig.WEATHER_API_KEY)
        }
        //в буфере жсон данные, конвертируем в дто из строки. открываем на чтение с считываем в буфер
        val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
// преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
        val weatherDTO: WeatherDTO? =
            Gson().fromJson(convertBufferToResult(bufferedReader), WeatherDTO::class.java)

// ричим на весё устройство
       // sendBroadcast(Intent(BROADCAST_ACTION).apply {
            //внутрь интента по ключу добавили погоду
           // putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
        //})
        //кричим локально в рамках приложения
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(BROADCAST_ACTION).apply {
            putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
        })
    }


    // получили данные и собрали их в одну кучу где на выходе будет строка
    private fun convertBufferToResult(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
}