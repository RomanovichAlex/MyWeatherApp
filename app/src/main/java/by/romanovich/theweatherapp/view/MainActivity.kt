package by.romanovich.theweatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.lesson1.Weather

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Добавить кнопку в разметку и повесить на неё clickListener в Activity.
        //. Потренироваться в создании классов и функций, описанных в уроке, и убедиться, что всё работает. Например, создать тестовое приложение:
        //a. Сформировать data class с двумя свойствами и вывести их на экран приложения.
        //b. Создать Object. В Object вызвать copy и вывести значения скопированного класса на экран.
        //c. Вывести значения из разных циклов в консоль, используя примеры из методических материалов.

        val weather = Weather("Minsk", 5)
        val weatherMoscow = weather.copy("Moscow", 10)


        val button = findViewById<Button>(R.id.button)
        val cityV = findViewById<TextView>(R.id.city)
        if (weather != null) cityV.text = weather.city else cityV.text = "Нет города"
        val temperature = findViewById<TextView>(R.id.temp)
        temperature.text = weather.temperature.toString()
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cityV.text = weatherMoscow.city
                temperature.text = weatherMoscow.temperature.toString()
            }
        })

        var result = if (true) 1 else 2
        Log.d("if", "$result")


        result = when (WeatherType.SNOWY) {
            WeatherType.SUNNY -> 1
            WeatherType.RAINY -> 2
            WeatherType.CLOUDY -> 3
            WeatherType.MISTY -> 4
            WeatherType.SNOWY -> 5
            WeatherType.HAILY -> 6
            else -> {
                7
            }
        }
        Log.d("When", "$result")


        for (i in 1..20 step 2) {
            Log.d("for-in", "$i Hello Kotlin!")
        }

        for (i in 20 downTo 0) {
            Log.d("for-in downTo", "$i")
        }

        for (i in 0 until 20) {
            Log.d("for-in until", "$i")
        }

        repeat(20) {
            Log.d("repeat", "${it+1}")
        }


        val daysOfWeek =
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        daysOfWeek.forEach {
            Log.d("forEach days", "$it")
        }

        for (day in daysOfWeek) {
            Log.d("for in days", "$day")
        }

    }

    enum class WeatherType {
        SUNNY,
        RAINY,
        CLOUDY,
        MISTY,
        SNOWY,
        HAILY
    }
}