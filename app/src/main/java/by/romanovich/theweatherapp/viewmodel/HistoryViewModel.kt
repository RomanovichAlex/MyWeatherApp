package by.romanovich.theweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.romanovich.theweatherapp.repository.RepositoryLocalImpl

class HistoryViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
) : ViewModel() {

    private val repositoryLocalImpl: RepositoryLocalImpl by lazy {
        RepositoryLocalImpl()
    }

    fun getLiveData() = liveData


    //вернуть всю историю
    fun getAllHistory() {
        //Thread {
            //liveData.postValue(AppState.Loading(0))
            // из локального реп получаем все записи
            val listWeather = repositoryLocalImpl.getAllHistoryWeather()
            //и помещаем в liveData
            liveData.postValue(AppState.Success(listWeather))
       // }.start()

    }


}