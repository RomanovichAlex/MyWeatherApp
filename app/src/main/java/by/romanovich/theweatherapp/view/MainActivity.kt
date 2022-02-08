package by.romanovich.theweatherapp.view
//глянуть 2 лекция 2.28 мин !!! объяснение работы приложения
//глянуть 3 лекция 4.28 мин !!! объяснение работы приложения
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.ActivityMainBinding
import by.romanovich.theweatherapp.lesson6.MyBroadcastReceiver
import by.romanovich.theweatherapp.lesson6.ThreadsFragment
import by.romanovich.theweatherapp.lesson9.ContentProviderFragment
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.BUNDLE_KEY
import by.romanovich.theweatherapp.utils.BUNDLE_KEY_WEATHER
import by.romanovich.theweatherapp.view.details.DetailsFragment
import by.romanovich.theweatherapp.view.history.HistoryFragment
import by.romanovich.theweatherapp.view.main.MainFragment


class MainActivity : AppCompatActivity() {
    //вызываем класс олицитворяющий наш макет
    private lateinit var binding: ActivityMainBinding
    private lateinit var number: String

    //для видимости
    val receiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //надуваем контекстом
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Передаем корневой макет в метод setContentView()., Каждый биндинг-класс также имеет метод getRoot(), который возвращает корневой layout
        setContentView(binding.root)

        //если нормальный запуск
        if (intent.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER) != null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    DetailsFragment.newInstance(
                        Bundle().apply {
                            putParcelable(
                                BUNDLE_KEY,
                                intent.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)
                            )
                        }
                    ))
                .addToBackStack("").commit()
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance()).commit()
        }
//обозначает что доступно только в рамках приложения придумываем таг
        val sp = getSharedPreferences("TAG", Context.MODE_PRIVATE)
// работает только на уровне активити таг - activityP
        val activityP = getPreferences(Context.MODE_PRIVATE)
        //на уровне приложения - вместо тэга пэкэдж найм имя приложения
        val appP = getDefaultSharedPreferences(this)

        //чтобы прочитать по ключу, получаем
appP.getString("key","")

        //чтобы записать строку по ключу
        val editor = appP.edit()
            editor.putString("key","value").apply()
        editor.putString("key2","value2").apply()
        editor.putBoolean("key3",true).apply()
editor.apply()


    }






        //закрываем слушателя
        override fun onDestroy() {
            super.onDestroy()
            unregisterReceiver(receiver)
        }


        // можем в любые фрагменты добовлять свои меню
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {//общее меню
            menuInflater.inflate(
                R.menu.main_screen_menu,
                menu
            )//можно подгружать в разные фрагменты доп. меню
            return super.onCreateOptionsMenu(menu)
        }

        //
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menu_threads -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, ThreadsFragment.newInstance()).addToBackStack("")
                        .commit()
                    true
                }R.id.menu_history -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, HistoryFragment.newInstance()).addToBackStack("")
                        .commit()
                    true
                }R.id.menu_content -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, ContentProviderFragment.newInstance()).addToBackStack("")
                        .commit()
                    true
                }else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }
    }
