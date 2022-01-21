package by.romanovich.theweatherapp.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.romanovich.theweatherapp.databinding.FragmentDetailsBinding
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.BUNDLE_KEY


const val BUNDLE_KEY_WEATHER = "key_weather_dto"
const val BROADCAST_ACTION = "BROADCAST_KEY"
const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"



class DetailsFragment : Fragment() {



    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    //наш ресивер является BroadcastReceiver
    private val receiver: BroadcastReceiver = object :BroadcastReceiver(){
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

    lateinit var localWeather :Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                localWeather = it
                //поместили широту долготу
                requireActivity().startService(Intent(requireActivity(), DetailsService::class.java).apply {
                    putExtra(LATITUDE_EXTRA,it.city.lat)
                    putExtra(LONGITUDE_EXTRA,it.city.lon)
                })
            }
        }
        //регистрируем локальный приемник
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter(BROADCAST_ACTION) )
    //регистрируем глобальный приемник
    //requireActivity().registerReceiver(receiver, IntentFilter(BROADCAST_ACTION) )
    }

    private fun setWeatherData(weatherDTO: WeatherDTO) {

        with(binding){
            with(localWeather){
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
        fun newInstance(bundle:Bundle)=DetailsFragment().apply { arguments = bundle }
    }
}