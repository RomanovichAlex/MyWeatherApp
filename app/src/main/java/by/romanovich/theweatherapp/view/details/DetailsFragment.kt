package by.romanovich.theweatherapp.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.romanovich.theweatherapp.BuildConfig
import by.romanovich.theweatherapp.databinding.FragmentDetailsBinding
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class DetailsFragment : Fragment() {


    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding !!
        }

    //наш ресивер является BroadcastReceiver
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        //и в этом методе мы получчаем интент в котором сидит ВезерДТО
        override fun onReceive(context: Context?, intent: Intent?) {
            //если интент не нулл, если в нем вернем погоду по ключу и отправили в сетВезер...
            intent?.let {
                it.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)?.let {
                    setWeatherData(it)
                }
            }
        }
    }
//создаем клиент
    private var client: OkHttpClient? = null

    private fun getWeather(){
        if(client==null)
           client = OkHttpClient()

        //настраиваем билдер
        val builder=Request.Builder().apply {
            //заголовок
            header(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            //адрес
            url(YANDEX_API_URL+YANDEX_API_URL_END_POINT+"?lat=${localWeather.city.lat}&lon=${localWeather
                .city.lon}")}
            //создаем настроеный запрос и переводим в реквест
        val request = builder.build()
//если клиент не нулл новый вызов происходит на базе реквеста
        val call = client?.newCall(request)
        /*Thread {
            TASK 1
            val response = call?.execute()//ждем пока выполнится задача1,эксекют это блокирующий поток
            Задача 2 - потом выполняем вторую задачу
        }.start()
        val response = call?.execute() // ошибка с networkOnMainThreadEx
*/

        call?.enqueue(object : Callback{
            //когда не достучался до сервера
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
//ответ сервера
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    //если ответ бади не нулл то мы его переводим в строку и парсим в везер дто
                    response.body()?.let{
                        //выполняем сет везер в главном потоке
                        val json = it.string()
                        requireActivity().runOnUiThread {
                            setWeatherData(Gson().fromJson(json, WeatherDTO::class.java))
                        }
                    }
                }else{
                        //TODO HW
                }
            }
        })
    }

    private lateinit var localWeather: Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                localWeather = it
                //поместили широту долготу
                getWeather()
                    }
            }
        }


    private fun setWeatherData(weatherDTO: WeatherDTO) {

        with(binding) {
            with(localWeather) {
                cityName.text = city.name
                cityCoordinates.text =
                    "${city.lat} ${city.lon}"
                temperatureValue.text = "${weatherDTO.fact.temp}"
                feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //requireActivity().unregisterReceiver(receiver)
        context?.let {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        //мы передаем бандл и помещаем его в аргументы
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
    }
}