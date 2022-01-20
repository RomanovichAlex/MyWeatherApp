package by.romanovich.theweatherapp.lesson6

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


//Если нужно выполнять задачи в фоне с переодичностью до часа
class MyWorker(context:Context,workerParameters: WorkerParameters):Worker(context,workerParameters) {
    override fun doWork(): Result {
        Log.d("mylogs", "doWork()")
        return Result.success()
    }
}