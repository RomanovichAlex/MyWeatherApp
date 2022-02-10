package by.romanovich.theweatherapp.view
//глянуть 2 лекция 2.28 мин !!! объяснение работы приложения
//глянуть 3 лекция 4.28 мин !!! объяснение работы приложения
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.ActivityMainBinding
import by.romanovich.theweatherapp.lesson6.MyBroadcastReceiver
import by.romanovich.theweatherapp.lesson6.ThreadsFragment
import by.romanovich.theweatherapp.lesson9.ContentProviderFragment
import by.romanovich.theweatherapp.view.history.HistoryFragment
import by.romanovich.theweatherapp.view.main.MainFragment


class MainActivity : AppCompatActivity() {
    //вызываем класс олицитворяющий наш макет
    private lateinit var binding: ActivityMainBinding

    //для видимости
    private val receiver = MyBroadcastReceiver()

    companion object {
        //2 нотификации
        private const val NOTIFICATION_ID_1 = 1
        private const val NOTIFICATION_ID_2 = 2
        //2 канала
        private const val CHANNEL_ID_1 = "channel_id_1"
        private const val CHANNEL_ID_2 = "channel_id_2"
    }


    private fun pushNotification() {
        //вызываем системный сервис и приводим к менеджеру нотификаций
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder_1 = NotificationCompat.Builder(this,CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setContentTitle("Заголовок для $CHANNEL_ID_1")
            setContentText("Сообщение для $CHANNEL_ID_1")
            //задаём приоритет
            priority = NotificationCompat.PRIORITY_MAX
        }
        val notificationBuilder_2 = NotificationCompat.Builder(this,CHANNEL_ID_2).apply {
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setContentTitle("Заголовок для $CHANNEL_ID_2")
            setContentText("Сообщение для $CHANNEL_ID_2")
            priority = NotificationCompat.PRIORITY_LOW
        }

        //передаем нотификации в канал если текущая версия больше 26 то работаем с каналами если нет то в кучу наваливается
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val channelName_1 = "Name $CHANNEL_ID_1"
            val channelDescription_1 = "Description for $CHANNEL_ID_1"
            //и у каналов есть приоритет
            val channelPriority_1 = NotificationManager.IMPORTANCE_HIGH
            //создаем канал
            val channel_1 = NotificationChannel(CHANNEL_ID_1,channelName_1,channelPriority_1).apply {
                description = channelDescription_1
            }
            //если версия после 25 то создаем канал
            notificationManager.createNotificationChannel(channel_1)
        }
        //отправляем нотификацию в первый канал
        notificationManager.notify(NOTIFICATION_ID_1,notificationBuilder_1.build())


        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val channelName_2 = "Name $CHANNEL_ID_2"
            val channelDescription_2 = "Description for $CHANNEL_ID_2"
            val channelPriority_2 = NotificationManager.IMPORTANCE_DEFAULT

            val channel_2 = NotificationChannel(CHANNEL_ID_2,channelName_2,channelPriority_2).apply {
                description = channelDescription_2
            }
            notificationManager.createNotificationChannel(channel_2)
        }
        //отправляем нотификацию во второй канал
        notificationManager.notify(NOTIFICATION_ID_2,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+1,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+2,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+3,notificationBuilder_2.build())
        notificationManager.notify(NOTIFICATION_ID_2+4,notificationBuilder_2.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //надуваем контекстом
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Передаем корневой макет в метод setContentView()., Каждый биндинг-класс также имеет метод getRoot(), который возвращает корневой layout
        setContentView(binding.root)

        pushNotification()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance()).commit()
        }


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
