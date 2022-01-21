package by.romanovich.theweatherapp.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import by.romanovich.theweatherapp.BuildConfig
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.API_KEY
import by.romanovich.theweatherapp.view.MainActivity
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


const val DETAILS_SERVICE_KEY_EXTRAS = "key_"

//Работаем в сервисе как в активити, т.е. у нас есть контекст, но нет вью, заточен на работу фоново
//IntentService отличается от сервис выполняется в новом потоке
class DetailsService(name:String= ""): IntentService(name) {

    private val TAG = "mylogs"

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }

    override fun onHandleIntent(intent: Intent?) {
        loadWeather(lat, lon)
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
                    val intent = Intent(DETAILS_INTENT_FILTER).apply {
                        //в нутрь интента по ключу добавили погоду
                        putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
                    }
        //в главном потоке
        Handler(Looper.getMainLooper()).post{
            //из DetailsService открываем новую активити
            startActivity(Intent(applicationContext,MainActivity::class.java).apply {
                putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
            })
        }
        }


    // получили данные и собрали их в одну кучу где на выходе будет строка
    private fun convertBufferToResult(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

}