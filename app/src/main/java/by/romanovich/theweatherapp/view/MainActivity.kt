package by.romanovich.theweatherapp.view
//глянуть 2 лекция 2.28 мин !!! объяснение работы приложения
//глянуть 3 лекция 4.28 мин !!! объяснение работы приложения
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.ActivityMainBinding
import by.romanovich.theweatherapp.lesson6.*
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.utils.BUNDLE_KEY
import by.romanovich.theweatherapp.view.details.BUNDLE_KEY_WEATHER
import by.romanovich.theweatherapp.view.details.DetailsFragment
import by.romanovich.theweatherapp.view.main.MainFragment
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {


    //вызываем класс олицитворяющий наш макет
    private lateinit var binding: ActivityMainBinding
    //для видимости
    val receiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //надуваем контекстом
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Передаем корневой макет в метод setContentView()., Каждый биндинг-класс также имеет метод getRoot(), который возвращает корневой layout
        setContentView(binding.root)

        if (intent.getParcelableExtra<WeatherDTO>(BUNDLE_KEY_WEATHER)!=null){
            supportFragmentManager.beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(
                    Bundle().apply { putParcelable(BUNDLE_KEY, intent.getParcelableExtra<WeatherDTO>(
                        BUNDLE_KEY_WEATHER)) }
                ))
                .addToBackStack("").commit()
        }

        //если нормальный запуск
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }


        startService(Intent(this,MyService::class.java).apply {
            putExtra(MAIN_SERVICE_KEY_EXTRAS,"hello")
        })

        val manager = WorkManager.getInstance(this)
        val worker = OneTimeWorkRequest.Builder(MyWorker::class.java)
                //задача будет выполняться с задержкой
            .setInitialDelay(5,TimeUnit.SECONDS)
            .build()
        manager.enqueue(worker)
        //умеет прекращать работу задачи
        //manager.cancelWorkById(worker.id)
        //manager.cancelAllWorkByTag()
        // посмотреть стаус задачи


        registerReceiver(MyBroadcastReceiver(), IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        //зарегистрировали приемник
        registerReceiver(MyBroadcastReceiver(), IntentFilter("myAction"))
//и сразу запустили сообщение в майАктион
        sendBroadcast(Intent("myAction").apply {
            putExtra(MAIN_SERVICE_KEY_EXTRAS, "HEllO")
        })
    }

    //закрываем слушателя
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }




    // можем в любые фрагменты добовлять свои меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {//общее меню
        menuInflater.inflate(R.menu.main_screen_menu, menu)//можно подгружать в разные фрагменты доп. меню
        return super.onCreateOptionsMenu(menu)
    }

    //
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return return when (item.itemId) {
            R.id.menu_threads -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, ThreadsFragment.newInstance()).addToBackStack("").commit()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

}