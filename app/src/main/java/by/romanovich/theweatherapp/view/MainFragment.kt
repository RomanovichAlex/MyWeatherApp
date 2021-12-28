package by.romanovich.theweatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.ActivityMainBinding
import by.romanovich.theweatherapp.databinding.FragmentMainBinding
import by.romanovich.theweatherapp.viewmodel.AppState
import by.romanovich.theweatherapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment() {

    //вызываем класс олицитворяющий наш макет, нулевой биндинг
    var _binding: FragmentMainBinding? = null
    // ссылка типа Фрагмент биндинг
    private val binding: FragmentMainBinding
    //указываем геттер который возращает биндинг что он точно не нулл
        get() {
        //!!- это точно не нулл
            return _binding!!
        }

    //во вью делаем ссылку на вью модел
    private lateinit var viewModel: MainViewModel


    //инициализируем вью модел
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ViewModelProvider - хранилище вьюмодел, что бы создовались в одном экземпляре,
        // и возращаем вьюмодел поскольку ее не существуетViewModelProvider ее и создает
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // обращаем внимание, просим вьюмодел вернуть лайфдату и устанавливаем слушателя на изменения лайфдаты (если они произошли -renderData)
        // с viewLifecycleOwner - жизненный путь
        //есть вью модел, в ней сидит лайфдата, на нее мы подписываемся, лайф дате надо знать viewLifecycleOwner жизненый цикл фрагмента,
        //сообщаем об изминениях в renderData
        //LifecycleOwner-встроенный во фрагмент(и в активити то же)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
        //вернуть погоду
        viewModel.getWeather()
    }

    //просматриваем изминения лайфдаты
    private fun renderData(appState: AppState) {
        //пробегаемся по трем состояниям сервера
        when (appState) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainView, "Error", Snackbar.LENGTH_LONG)
                    .setAction("Попробовать ещё раз") {
                        viewModel.getWeatherFromServer()
                    }.show()
            }
            //видимый
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                //выводим город
                binding.cityName.text = appState.weatherData.city.name
                //координаты
                binding.cityCoordinates.text = "${appState.weatherData.city.lat} ${appState.weatherData.city.lon}"
                //температуру
                binding.temperatureValue.text =  "${appState.weatherData.temperature}"
                //как ощущается
                binding.feelsLikeValue.text =  "${appState.weatherData.feelsLike}"

                Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    // Важно! Обязательно обнуляем _binding в onDestroyView, чтобы избежать утечек и не желаемого
    //поведения. В Activity ничего похожего делать не требуется.
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding.tv // null pointer
        //присваеваем binding fragment заменяемый контейнер активити, который может быть нулл, возращаем который точно не нулл
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}