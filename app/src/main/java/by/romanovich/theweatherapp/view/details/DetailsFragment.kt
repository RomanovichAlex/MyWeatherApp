package by.romanovich.theweatherapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.romanovich.theweatherapp.databinding.FragmentDetailsBinding
import by.romanovich.theweatherapp.model.Weather

const val BUNDLE_KEY = "key"

class DetailsFragment : Fragment() {


    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding !!
        }


    // отображает данные которые в него передали
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // было
        // super.onViewCreated(view, savedInstanceState)
        //достаем веазер из аргументов, досаем парсеализированную погоду по ключу
        //val weather = arguments?.getParcelable<Weather>(BUNDLE_KEY)
        //if(weather!=null){ setWeatherData(weather) }
        //Стало:
        //работа с аргументами и в случае если они есть, из того что я получил извликаю веазер
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.run {
                //и сразу с ней работаю если она не нулл
                setWeatherData(this)
            }
        }
    }


    //Было
    /*private fun setWeatherData(weather: Weather) {
        binding.cityName.text = weather.city.name
        binding.cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
        binding.temperatureValue.text = "${weather.temperature}"
        binding.feelsLikeValue.text = "${weather.feelsLike}"
    }*/
    //Стало:
    private fun setWeatherData(weather: Weather) {
        with(binding) {

            cityName.text = weather.city.name
            cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
            temperatureValue.text = "${weather.temperature}"
            feelsLikeValue.text = "${weather.feelsLike}"
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

    //Было
    // контейнер в котором сидит наша погода
    /*companion object {
        fun newInstance(bundle: Bundle):DetailsFragment {
            val fragment  = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }*/
    //Стало
    companion object {
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
//можно вместо apply, also { it.arguments = bundle }, но может пойти что то не так с многопоточностью
    }
}