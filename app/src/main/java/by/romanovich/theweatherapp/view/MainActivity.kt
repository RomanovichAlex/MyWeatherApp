package by.romanovich.theweatherapp.view
//глянуть 2 лекция 2.28 мин !!! объяснение работы приложения
//глянуть 3 лекция 4.28 мин !!! объяснение работы приложения
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.ActivityMainBinding
import by.romanovich.theweatherapp.lesson10.MapsFragment
import by.romanovich.theweatherapp.lesson6.MyBroadcastReceiver
import by.romanovich.theweatherapp.lesson6.ThreadsFragment
import by.romanovich.theweatherapp.lesson9.ContentProviderFragment
import by.romanovich.theweatherapp.view.history.HistoryFragment
import by.romanovich.theweatherapp.view.main.MainFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    //вызываем класс олицитворяющий наш макет
    private lateinit var binding: ActivityMainBinding

    //для видимости
    private val receiver = MyBroadcastReceiver()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //надуваем контекстом
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Передаем корневой макет в метод setContentView()., Каждый биндинг-класс также имеет метод getRoot(), который возвращает корневой layout
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance()).commit()
        }
    }


        fun getFCMToken(){
        // получаем токен
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("mylogs_push", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("mylogs_push", " token $token")
            // Log and toast
            /*  val msg = getString(R.string.msg_token_fmt, token)
              Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()*/
        })

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
                        .add(R.id.container, ContentProviderFragment.newInstance())
                        .addToBackStack("")
                        .commit()
                    true
                }
                    R.id.menu_google_maps -> {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.container, MapsFragment()).addToBackStack("").commit()
                        true
                }else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }
    }
