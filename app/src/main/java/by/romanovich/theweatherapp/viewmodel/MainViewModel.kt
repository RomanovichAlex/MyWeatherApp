package by.romanovich.theweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.romanovich.theweatherapp.model.RepositoryImpl
import java.lang.IllegalStateException


//наследуется от ViewModel. Тип объекта, хранящий в себе LiveData, — <AppState (в качестве примера).
class MainViewModel(
    //создали контейнер liveData с состоянием приложения AppState и на этот контейнер подписалась фрагмент(Вьюшка)
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    //функция возврата LiveData типа <AppState>
    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherFromServer() {
        liveData.postValue(AppState.Loading(0))
        //из вспомогательного потока запускаем синхронно поствалю
        Thread {
            Thread.sleep(1000)
            //рандом до 40 градусов
            val rand = (1..40).random()
            if (rand > 20) {
                // В классе LiveData доступны методы setValue и postValue: первый метод для обновления
                //данных из основного потока, второй — из рабочего потока.
                //postValue - синхронный, с главным потоком запрос.Value - асинхронный(если в главном потоке то можно)
                liveData.postValue(AppState.Success(repositoryImpl.getWeatherFromServer()))
            } else {
                liveData.postValue(AppState.Error(IllegalStateException("")))
            }

        }.start()

        fun getWeather() {
            // скоро будет какой-то переключатель
            getWeatherFromServer()
        }
    }

    fun getWeather() {
        // скоро будет какой-то переключатель
        getWeatherFromServer()
    }

}