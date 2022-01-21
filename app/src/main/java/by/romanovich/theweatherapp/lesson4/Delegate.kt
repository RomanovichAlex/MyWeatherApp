package by.romanovich.theweatherapp.lesson4

import android.util.Log


//интерфейс - это поведение, базовое поведение1, подразумевает функцию(поведение)
interface Base1 {
    fun someFun1(print: String): Int
}

interface Base2 {
    fun someFun2()
}

//классы которые реализует у себя интерфейс задачи базе1 и 2, с пустым конструктором()
class BaseImpl1() : Base1 {
    override fun someFun1(print: String): Int {
        return Log.d("mylogs", " someFun1 $print")
    }
}

class BaseImpl2() : Base2 {
    override fun someFun2() {
        Log.d("mylogs", " someFun2")
    }
}


//ленивому боссу передаем 2-х работников, (босс умеет работать и с базе1 и 2 но вызывае на работу базе1 и базе2)
class LazyImpl(base1: Base1, base2: Base2) : Base1 by base1, Base2 by base2 {

}

// функция верхнего уровня
fun main() {
    //даем выполнить задания 1 и 2 базе1 и 2
    val base1 = BaseImpl1()
    val base2 = BaseImpl2()
    //ленивый босс
    val boss = LazyImpl(base1, base2)

    Log.d("mylogs", " ${boss.someFun1("boss")}")
    boss.someFun2()
}