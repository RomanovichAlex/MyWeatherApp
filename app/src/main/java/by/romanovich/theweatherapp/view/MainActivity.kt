package by.romanovich.theweatherapp.view
//глянуть 2.28 мин лекции!!! объяснение работы приложения

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    //вызываем класс олицитворяющий наш макет
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //надуваем контекстом
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Передаем корневой макет в метод setContentView()., Каждый биндинг-класс также имеет метод getRoot(), который возвращает корневой layout
        setContentView(binding.root)
        //если нормальный запуск
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container,MainFragment.newInstance()).commit()
        }
    }

}