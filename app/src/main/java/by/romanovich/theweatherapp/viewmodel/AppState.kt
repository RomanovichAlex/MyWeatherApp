package by.romanovich.theweatherapp.viewmodel

import by.romanovich.theweatherapp.model.Weather


// запечатанный класс в котором 3 состояния
sealed class AppState {
    //передаём переменную progress в дата класс наследованный от AppState
    data class Loading(val progress:Int):AppState()
    //ответ сервера в случае успеха получим погоду
    data class Success(val weatherData: Weather):AppState()

    data class Error( val error:Throwable):AppState()
}