package by.romanovich.theweatherapp.view.main


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.FragmentMainBinding
import by.romanovich.theweatherapp.model.City
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.utils.BUNDLE_KEY
import by.romanovich.theweatherapp.view.details.DetailsFragment
import by.romanovich.theweatherapp.viewmodel.AppState
import by.romanovich.theweatherapp.viewmodel.MainViewModel


class MainFragment : Fragment(), OnMyItemClickListener {

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
    private val adapter: CitiesAdapter by lazy {
        CitiesAdapter(this)
    }


    //во вью делаем ссылку на вью модел
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    //инициализируем вью модел
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ViewModelProvider - хранилище вьюмодел, что бы создовались в одном экземпляре,
        // и возращаем вьюмодел поскольку ее не существуетViewModelProvider ее и создает
        initView()
        // обращаем внимание, просим вьюмодел вернуть лайфдату и устанавливаем слушателя на изменения лайфдаты (если они произошли -renderData)
        // с viewLifecycleOwner - жизненный путь
        //есть вью модел, в ней сидит лайфдата, на нее мы подписываемся, лайф дате надо знать viewLifecycleOwner жизненый цикл фрагмента,
        //сообщаем об изминениях в renderData
        //LifecycleOwner-встроенный во фрагмент(и в активити то же)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
    }

    private fun initView() {
        var isRussian =
            requireActivity().getPreferences(Activity.MODE_PRIVATE).getBoolean("isRussian", true)
        initLocation(isRussian)
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            //по нажатию на кнопку русские города в мировые
            mainFragmentFAB.setOnClickListener {
                isRussian = ! isRussian
                initLocation(isRussian)
            }
            mainFragmentFABLocation.setOnClickListener {
                checkPermission()
            }
        }
    }


    //спрашиваем есть ли у нас разрешение к контактам
    private fun checkPermission() {
        context?.let {
            when {
                //на вход идет контекст есть ли  разрешение
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                // нужна ли рацианализация(первый раз метод не сработает)
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialogRatio()
                }
                //если отказали
                else -> {
                    myRequestPermission()
                }
            }
        }
    }


//дистанция для обновления
    private val MIN_DISTANCE = 100f
    //время обновления == 1 мин
    private val REFRESH_PERIOD = 60000L


    private fun showAddressDialog(address:String,location: Location){
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_address_title))
            .setMessage(address)
            .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                toDetails(Weather(City(address,location.latitude,location.longitude)))
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }


    private fun getAddress(location: Location){
        Log.d(""," $location")
        //запускаем асинхронно
        Thread{
            //
            val geocoder = Geocoder(requireContext())
            val listAddress=geocoder.getFromLocation(location.latitude,location.longitude,1)
            requireActivity().runOnUiThread{
                showAddressDialog(listAddress[0].getAddressLine(0),location)
            }
        }.start()
    }


    private val locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            getAddress(location)

        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

//Если пользователь дал разрешение, получаем местоположение
    private fun getLocation() {
    activity?.let {
        if(ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )==PackageManager.PERMISSION_GRANTED){
            // Получить менеджер геолокаций
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//включен ли провайдер джпс
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                providerGPS?.let {
// Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        //вешаем на джпс лисенер который будет обновляться каждые 60 секунд или каждые 100 метров
                            REFRESH_PERIOD,
                            MIN_DISTANCE,
                            locationListener
                    )
                }
            }else {
                val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                lastLocation?.let {
                    getAddress(it)
                }
            }
        }else{

        }
    }
}



    private fun showDialog(){

    }

    val REQUEST_CODE = 999
    //делаем запрос на получение жпс координат
    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    //и проверяем результат
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {

            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogRatio()
                }
                else -> {
                    Log.d("", "КОНЕЦ")
                }
            }
        }
    }

    private fun showDialogRatio() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к геолокации") // TODO HW
            .setMessage(getString(R.string.dialog_message_no_gps))
            .setPositiveButton("Предоставить доступ") { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }








    private fun initLocation(isRussian:Boolean) {
        with(viewModel){
            if (isRussian) getWeatherFromLocalSourceRus()
            else getWeatherFromLocalSourceWorld()
        }
        if (isRussian) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            requireActivity().getPreferences(Activity.MODE_PRIVATE).edit().putBoolean("isRussian", true).apply()
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            requireActivity().getPreferences(Activity.MODE_PRIVATE).edit().putBoolean("isRussian", false).apply()
        }
    }

    private fun renderData(appState: AppState) {
        with(binding) {
            when (appState) {
                is AppState.Error -> {
                    Toast.makeText(requireContext(),appState.error, Toast.LENGTH_SHORT).show()
                    initView()
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
                    //root.showSnackBarWithoutAction(getString(R.string.Success_show), Snackbar.LENGTH_LONG)
                }
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
    ): View {
        //binding.tv // null pointer
        //присваеваем binding fragment заменяемый контейнер активити, который может быть нулл, возращаем который точно не нулл
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }



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

    //передаем погоду
    private fun toDetails(weather: Weather) {
        activity?.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.container,
                    DetailsFragment.newInstance(
                        Bundle().apply {
                            putParcelable(BUNDLE_KEY, weather)
                        }
                    ))
                .addToBackStack("").commit()
        }
    }
}
