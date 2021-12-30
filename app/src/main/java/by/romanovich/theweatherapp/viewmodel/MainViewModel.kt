package by.romanovich.theweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.romanovich.theweatherapp.model.RepositoryImpl


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

//Вернуть погоду из..
    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalServer(true)

    fun getWeatherFromLocalSourceWorld() = getWeatherFromLocalServer(false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(true)//заглушка на 5 урок

    fun getWeatherFromLocalServer(isRussian: Boolean) {
        liveData.postValue(AppState.Loading(0))
        //из вспомогательного потока запускаем синхронно поствалю
        Thread {
            Thread.sleep(1000)
            //рандом до 40 градусов
            val rand = (1..40).random()
            if (true) {
                // В классе LiveData доступны методы setValue и postValue: первый метод для обновления
                //данных из основного потока, второй — из рабочего потока.
                //postValue - синхронный, с главным потоком запрос.Value - асинхронный(если в главном потоке то можно)
                liveData.postValue(
                    AppState.Success(
                        if (isRussian) {
                            repositoryImpl.getWeatherFromLocalStorageRus()
                        } else {
                            repositoryImpl.getWeatherFromLocalStorageWorld()
                        }
                    )
                )
            } else {
                // liveData.postValue(AppState.Error(IllegalStateException("")))
            }

        }.start()
    }
}