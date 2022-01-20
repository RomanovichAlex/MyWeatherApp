package by.romanovich.theweatherapp.lesson6

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.romanovich.theweatherapp.databinding.FragmentThreadsBinding


class ThreadsFragment : Fragment() {

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
        binding.button.setOnClickListener {
            myThread.handler?.post {
                //обработка задач
                val result = startCalculations(2)
                activity?.let{ activity->
                    //
                    Handler(Looper.getMainLooper()).post {
                        //создаем новые вьюхи на каждый из наших потоков, добавляя их в mainContainer
                        binding.mainContainer.addView(TextView(activity).apply {
                            text = result
                        })
                    }
                }
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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