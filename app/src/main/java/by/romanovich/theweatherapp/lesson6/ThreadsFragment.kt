package by.romanovich.theweatherapp.lesson6

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.romanovich.theweatherapp.databinding.FragmentThreadsBinding


class ThreadsFragment : Fragment() {
// методы фрагмента
    //контекст активити открывается
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
//контекст удаляется
    override fun onDetach() {
        super.onDetach()
    }


    private var _binding: FragmentThreadsBinding? = null
    private val binding: FragmentThreadsBinding
        get() {
            return _binding!!
        }


    //создали один поток для этого фрагмента и выполнения задач
    val myThread = MyThread()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myThread.start()
        //по клику на кнопку передаем потоку наши задачи
        binding.button.setOnClickListener {           //в лямбду приходит параметр ит
            myThread.handler?.post {
                //обработка задач
                val result = startCalculations(2)
                // после онДетач активити занулена(ее не существует), фрагмент повис в воздухе
                //activity?.let{ activity->//-контекст приходит в параметр
                    //передаем задачу в поток
                    Handler(Looper.getMainLooper()).post {
                        Log.d("My Logs", "Происходит утечка в fragment c binding $binding")
                        //создаем новые вьюхи на каждый из наших потоков, добавляя их в mainContainer
                        binding.mainContainer.addView(TextView(activity).apply {
                            text = result
                        })
                    }
                //}
            }
        }
    }


    //что бы не создавать кучу потоков, будем работать в одном
    class MyThread:Thread(){
        var handler: Handler?= null
        override fun run() {
            //зацикливаем, поток(бесконечные поток, чтобы не закрывался по сле выполнения задачи)
            Looper.prepare()
            handler = Handler(Looper.myLooper()!!)
            Looper.loop()
        }
    }


    private fun startCalculations(seconds: Int): String {
        Thread.sleep(seconds*1000L)
        return "${seconds.toString()} ${Thread.currentThread().name}"
    }


    //поток закрывается
    override fun onDestroy() {
        super.onDestroy()
        //удаляем все задачи из потока
        //myThread.handler?.removeCallbacksAndMessages(null)
        //зануляем байндинг
        //_binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = ThreadsFragment()
    }
}