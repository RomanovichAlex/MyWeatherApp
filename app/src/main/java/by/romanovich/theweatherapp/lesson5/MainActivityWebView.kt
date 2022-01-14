package by.romanovich.theweatherapp.lesson5

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import by.romanovich.theweatherapp.databinding.ActivityMainWebviewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {

    private lateinit var binding: ActivityMainWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //надуваем контекстом
        binding = ActivityMainWebviewBinding.inflate(layoutInflater)
        //Передаем корневой макет в метод setContentView()., Каждый биндинг-класс также имеет метод getRoot(), который возвращает корневой layout
        setContentView(binding.root)
// по кнопке ок открывается строка с именем сайта
        binding.btnOk.setOnClickListener{
            request(binding.etUrl.text.toString())
        }
    }

    //имя сайта привели к виду урл
    private fun request(urlString: String) {
        /*try {
     // работаем здесь
            }catch (){
                // выводим ошибки
            } finally {
                httpsURLConnection.disconnect()
            }*/
        val handlerCurrent1 = Handler(Looper.myLooper()!!)
        //выносим все во вспомогательный поток
        Thread {
        val url = URL(urlString)
        //у урл открываем связь с сайтом и указываем какого типа должна быть связь
            val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
// устанавливаем настройки, метод запроса
            requestMethod = "GET"// установка метода получения данных -- GET
// устанавливаем настройки, устанавливаем таймаут ожидания ответа сервера
            readTimeout = 2000
        }
        // у нас есть буфер в который пришла информация из
        // нашего подключения HttpsURLConnection который подключен по урл к адресу нашего сайта
        val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))// читаемданные в поток
        //нажно обработать данные из буфера и привести к результату, конвертируем данные
        val result = convertBufferToResult(bufferedReader)
// загрузка данных в вебвью, вызвали главный поток
            runOnUiThread {
                binding.webView.loadDataWithBaseURL(
                    null,
                    result,
                    "text/html; charset=utf-8",
                    "utf-8",
                    null
                )
            }
            //из вспомогательного потока вызываем ссылкой главный
            val handlerMainUI1 = Handler(mainLooper)
            //== верхней записи
            val handlerMainUI2 = Handler(Looper.getMainLooper())
            //хэндлеру говорим сделай нам задачу(пост) отправь нам задачу выполни вебвью и загрузи туда данные
            handlerMainUI1.post {
                binding.webView.loadDataWithBaseURL(
                    null,
                    result,
                    "text/html; charset=utf-8",
                    "utf-8",
                    null
                )
            }
            //ссылается на главный поток майн
            handlerCurrent1.post {
                binding.webView.loadDataWithBaseURL(
                    null,
                    result,
                    "text/html; charset=utf-8",
                    "utf-8",
                    null
                )
            }
            handlerCurrent1.post {
            //binding.webView.loadUrl(url.path)
            }

            httpsURLConnection.disconnect()
        }.start()
    }



//склеиваем данные из буферридера
private fun convertBufferToResult(bufferedReader: BufferedReader): String {
    return bufferedReader.lines().collect(Collectors.joining("\n"))

    }
}