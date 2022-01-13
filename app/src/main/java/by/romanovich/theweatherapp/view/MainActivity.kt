package by.romanovich.theweatherapp.view
//глянуть 2 лекция 2.28 мин !!! объяснение работы приложения
//глянуть 3 лекция 4.28 мин !!! объяснение работы приложения
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.ActivityMainBinding
import by.romanovich.theweatherapp.view.main.MainFragment


class MainActivity : AppCompatActivity() {


    //вызываем класс олицитворяющий наш макет
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //надуваем контекстом
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Передаем корневой макет в метод setContentView()., Каждый биндинг-класс также имеет метод getRoot(), который возвращает корневой layout
        setContentView(binding.root)
        //если нормальный запуск
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }
    }

}