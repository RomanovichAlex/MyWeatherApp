package by.romanovich.theweatherapp.model

//город температура и как чувствуется с данными поумолчанию
data class Weather(val city:City= getDefaultCity(), val temperature:Int=20, val feelsLike:Int=20)

//название города, долгота и широта
data class City(val name:String,val lon:Double,val lat:Double)

//функция генерирующая случайный город
fun getDefaultCity() = City("Москва",37.5,55.5)
