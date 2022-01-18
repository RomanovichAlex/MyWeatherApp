package by.romanovich.theweatherapp.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.FragmentMainBinding
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.utils.BUNDLE_KEY
import by.romanovich.theweatherapp.view.details.DetailsFragment
import by.romanovich.theweatherapp.viewmodel.AppState
import by.romanovich.theweatherapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment(),OnMyItemClickListener {

    //вызываем класс олицитворяющий наш макет, нулевой биндинг
    private var _binding: FragmentMainBinding? = null

    // ссылка типа Фрагмент биндинг
    private val binding: FragmentMainBinding
        //указываем геттер который возращает биндинг что он точно не нулл
        get() {
            //!!- это точно не нулл
            return _binding !!
        }

    /*//было
// в мфа передаем
    private val adapter = MainFragmentAdapter(this)*/
    //Стало by lazy— ленивая инициализация
    private val adapter: MainFragmentAdapter by lazy {
        MainFragmentAdapter(this)
    }


    // для отображения по умолчанию русских городов
    private var isRussian = true

    //во вью делаем ссылку на вью модел
    private lateinit var viewModel: MainViewModel


    //инициализируем вью модел
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ViewModelProvider - хранилище вьюмодел, что бы создовались в одном экземпляре,
        // и возращаем вьюмодел поскольку ее не существуетViewModelProvider ее и создает
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initView()
        // обращаем внимание, просим вьюмодел вернуть лайфдату и устанавливаем слушателя на изменения лайфдаты (если они произошли -renderData)
        // с viewLifecycleOwner - жизненный путь
        //есть вью модел, в ней сидит лайфдата, на нее мы подписываемся, лайф дате надо знать viewLifecycleOwner жизненый цикл фрагмента,
        //сообщаем об изминениях в renderData
        //LifecycleOwner-встроенный во фрагмент(и в активити то же)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun initView() {
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            //по нажатию на кнопку русские города в мировые
            mainFragmentFAB.setOnClickListener {
                sentRequest()
            }
        }
    }

    /*private fun sentRequest() {
        isRussian = !isRussian
        if (isRussian) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
    }*/
    private fun sentRequest() {
        isRussian = ! isRussian
        with(binding) {
            if (isRussian) {
                viewModel.getWeatherFromLocalSourceRus()
                mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            } else {
                viewModel.getWeatherFromLocalSourceWorld()
                mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            }
        }
    }

    /*//просматриваем изминения лайфдаты
    private fun renderData(appState: AppState) {
        //пробегаемся по трем состояниям сервера
        when (appState) {
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, getString(R.string.Error), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retryAgein)) { sentRequest() }.show() }
            is AppState.Loading -> {
                //видимый
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                //в случае удачи, подгружаем адаптер
                adapter.setWeather(appState.weatherData)
                Snackbar.make(binding.root, getString(R.string.Succes), Snackbar.LENGTH_LONG).show()
            }
        }
    }*/

    private fun renderData(appState: AppState) {
        with(binding) {
            when (appState) {
                is AppState.Error -> {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    root.showSnackBar("R.string.Error","R.string.retryAgein",
                        { viewModel.getWeatherFromLocalSourceRus() },Snackbar.LENGTH_INDEFINITE)
                   /* Snackbar.make(root, getString(R.string.Error), Snackbar.LENGTH_LONG)
                        .setAction(R.string.retryAgein) {
                            sentRequest()
                        }.show()*/
                }
                is AppState.Loading -> {
                    mainFragmentLoadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    adapter.setWeather(appState.weatherData)
                    root.showSnackBarWithoutAction(
                        getString(R.string.Success_show),
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        }
    }

    //View-как санстрейн лайаут как ресивер,
    private fun View.showSnackBarWithoutAction(text:String,length:Int){
        Snackbar.make(this,text,length).show()
    }

    private fun View.showSnackBar(text: String, actionText: String, action: (View) -> Unit, length: Int = Snackbar.LENGTH_INDEFINITE) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
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
    ): View {
        //binding.tv // null pointer
        //присваеваем binding fragment заменяемый контейнер активити, который может быть нулл, возращаем который точно не нулл
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    //Было
    /*override fun onItemClick(weather: Weather) {
        //контейнер в котором передаются данные
        val bundle=Bundle()
        // в контейнер, по ключу поместили погоду
        bundle.putParcelable(BUNDLE_KEY,weather)
        //переходим в транзакцию, в ней переходим в supportFragmentManager и в ней говорим
        requireActivity().supportFragmentManager.beginTransaction()
                //добавить в контейнер, созданный нами бандл
            .add(R.id.container, DetailsFragment.newInstance(bundle))
                //что бы не закрывалось приложение по нажатию назад
            .addToBackStack("").commit()
    }
}*/
    //Стало
    override fun onItemClick(weather: Weather) {
//если активити не нулл, я с ней работаю как с ресивером
        activity?.run {
            //у нее, переходим в транзакцию, в ней переходим в supportFragmentManager и в ней говорим
            supportFragmentManager.beginTransaction()
                //где передаю в детаилс фрагмент
                .add(R.id.container,
                    //и в фабричный метод передаю
                    DetailsFragment.newInstance(
                        //новый бандл, с которым сейчас буду работать
                        Bundle().apply {
                            //код для работы с бандлом, в котором принимаются изменения
                            putParcelable(BUNDLE_KEY, weather)
                        }
                    ))
                .addToBackStack("").commit()
        }
    }
}