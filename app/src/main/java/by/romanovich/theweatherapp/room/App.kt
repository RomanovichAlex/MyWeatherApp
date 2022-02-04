package by.romanovich.theweatherapp.room

import android.app.Application
import androidx.room.Room
import java.util.*

class App: Application() {
    //нужна статика
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }


    //раз статический значит нужен обжект
    companion object {
        //вначале пустой после создания приложения связывается с ресивером - appInstance = this
        private var appInstance:App? =null
        //корневой заголовок
        private const val DB_NAME = "History.db"
        // ссылка на базу данных
        private var db:HistoryDatabase?=null

        //делает возврат HistoryWeatherDao
        fun getHistoryWeatherDao():HistoryWeatherDao{
            //если база данных существует
            if(db==null){
                //если база данных пуста
                    if(appInstance==null){ throw  IllformedLocaleException("Все очень плохо") }
                //если не
                    else{
                        //то создаём базу данных на уровне апликатион!, ссылка на бд и строим нашу бд нужен контекст и имя дата класса и имя бд
                    db = Room.databaseBuilder(appInstance!!.applicationContext,HistoryDatabase::class.java,DB_NAME)
                        //мы уверены что ничего плохого не случится тогда
                        //.allowMainThreadQueries() // TODO нужно убрать эту строку
                            //и из нее получаем
                        .build()
                }
            }
            return db!!.historyWeatherDao()
        }
    }

}