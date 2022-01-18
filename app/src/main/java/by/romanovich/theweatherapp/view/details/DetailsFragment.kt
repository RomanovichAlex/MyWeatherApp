package by.romanovich.theweatherapp.view.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.romanovich.theweatherapp.databinding.FragmentDetailsBinding
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.BUNDLE_KEY
import by.romanovich.theweatherapp.utils.WeatherLoader


class DetailsFragment : Fragment(),WeatherLoader.OnWeatherLoaded {



    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }



    private val weatherLoader =WeatherLoader(this)
    lateinit var locaWeather:Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                locaWeather = it
                weatherLoader.loadWeather(it.city.lat,it.city.lon)
            }
        }

    }

    private fun setWeatherData(weatherDTO: WeatherDTO) {

        with(binding){
            with(locaWeather){
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(bundle:Bundle)=DetailsFragment().apply { arguments = bundle }
    }

    override fun onLoaded(weatherDTO: WeatherDTO?) {
        weatherDTO?.let {
            setWeatherData(weatherDTO)
        }
        Log.d("","")
    }

    override fun onFailed(e: Throwable) {
        Toast.makeText(context,"e", Toast.LENGTH_SHORT).show()

    }
}