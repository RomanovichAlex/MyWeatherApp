package by.romanovich.theweatherapp.repository

import by.romanovich.theweatherapp.model.Weather

//интерфейс поведения, тот кто имплементирует этот интерфейс должен вернуть города
interface RepositoryCitiesList {


    //функция для возврата погоды локально
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}