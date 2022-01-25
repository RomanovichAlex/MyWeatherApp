package by.romanovich.theweatherapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.romanovich.theweatherapp.databinding.FragmentDetailsBinding
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.utils.BUNDLE_KEY
import by.romanovich.theweatherapp.viewmodel.AppState
import by.romanovich.theweatherapp.viewmodel.DetailsViewModel
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest


class DetailsFragment : Fragment() {


    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding !!
        }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private fun renderData(appState: AppState) {
        with(binding) {
            when (appState) {
                is AppState.Error -> {
                    /*mainFragmentLoadingLayout.visibility = View.GONE
                    root.showSnackBar("R.string.Error", "R.string.retryAgein",
                        { viewModel.getWeatherFromLocalSourceRus() }, Snackbar.LENGTH_INDEFINITE
                    )
                    /* Snackbar.make(root, getString(R.string.Error), Snackbar.LENGTH_LONG)
                         .setAction(R.string.retryAgein) {
                             sentRequest()
                         }.show()*/*/
                }
                is AppState.Loading -> {
                    /*mainFragmentLoadingLayout.visibility = View.VISIBLE*/
                }
                is AppState.Success -> {
                    val weather = appState.weatherData[0]
                    setWeatherData(weather)
                }
            }
        }
    }


    private lateinit var localWeather: Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //вешаем слушателя в инициализацию
        viewModel.getLiveData().observe(viewLifecycleOwner,{
            renderData(it)

        })
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                localWeather = it
                viewModel.getWeatherFromRemoteServer(localWeather.city.lat, localWeather.city.lon)

            }
        }
    }


    private fun setWeatherData(weather: Weather) {

        with(binding) {
            with(localWeather) {
                cityName.text = city.name
                cityCoordinates.text =
                    "${city.lat} ${city.lon}"
                temperatureValue.text = "${weather.temperature}"
                feelsLikeValue.text = "${weather.feelsLike}"


                /*Glide.with(headerIcon.context)
                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    .into( headerIcon)

                Picasso.get()
                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    .into( headerIcon)*/

                headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")

                weatherIcon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")

            }
        }
    }

    private fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
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
        //мы передаем бандл и помещаем его в аргументы
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
    }
}