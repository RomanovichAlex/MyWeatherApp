package by.romanovich.theweatherapp.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log


const val MAIN_SERVICE_KEY_EXTRAS = "key_"

//Работаем в сервисе как в активити, т.е. у нас есть контекст, но нет вью, заточен на работу фоново
//IntentService отличается от сервис выполняется в новом потоке
class MyService(name:String= ""): IntentService(name) {

    private val TAG = "mylogs"

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }

    //во фрагмент приходит какой то интент
    override fun onHandleIntent(intent: Intent?) {
        //  в интенте будет сидеть экстра по ключу, если экстра нет он верне нулл
        createLogMessage("onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_KEY_EXTRAS)}")
    }


    override fun onCreate() {
        super.onCreate()
        createLogMessage("onCreate ")
    }

    override fun onDestroy() {
        super.onDestroy()
        createLogMessage("onDestroy ")
    }

    //
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createLogMessage("onStartCommand ${flags} ")
        IntentService.START_STICKY_COMPATIBILITY
        IntentService.START_STICKY
        //  по умолчанию флаг IntentService.START_STICKY_COMPATIBILITY
        return super.onStartCommand(intent, flags, startId)
    }

}