package by.romanovich.theweatherapp.model

//интерфейс поведения
interface Repository {

    //функция для возврата погоды с сервера
    fun getWeatherFromServer(): Weather

    //функция для возврата погоды локально
    fun getWeatherFromLocalStorage(): Weather
}