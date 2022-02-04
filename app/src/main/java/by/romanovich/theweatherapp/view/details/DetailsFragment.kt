package by.romanovich.theweatherapp.view.details

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.romanovich.theweatherapp.R
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
                    loadingFailed(appState.error)
                }
                is AppState.Loading -> {
                    loadingLayout.visibility = View.VISIBLE
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



    // отрисовываем погоду
    @SuppressLint("SetTextI18n")
    private fun setWeatherData(weather: Weather) {

        //добавить кнопку
        with(binding) {
           // weatherIcon.setOnClickListener {
            weather.city = localWeather.city
            viewModel.saveWeather(weather)
            //}

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

    @SuppressLint("SetTextI18n")
    private fun loadingFailed(code: String) {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater? = LayoutInflater.from(requireContext())
        val exitView: View = inflater!!.inflate(R.layout.dialog_error, null)
        dialog.setView(exitView)
        val dialog1: Dialog = dialog.create()
        val ok: Button = exitView.findViewById(R.id.ok)
        val codeTextView = exitView.findViewById<TextView>(R.id.codeTextView)
        codeTextView.text = codeTextView.text.toString() + " " + code
        dialog1.setCancelable(false)
        ok.setOnClickListener {
            dialog1.dismiss()
            requireActivity().onBackPressed()
        }
        dialog1.show()
    }

    companion object {
        //мы передаем бандл и помещаем его в аргументы
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
    }
}