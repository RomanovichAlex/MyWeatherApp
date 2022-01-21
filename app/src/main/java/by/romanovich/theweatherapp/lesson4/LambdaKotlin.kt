package by.romanovich.theweatherapp.lesson4


//2,30 объяснение работы стандартных екстеншен 5 функций
import android.util.Log

class LambdaKotlin {

    //класс персон
    data class Person(var name: String, var age: Int)

    fun main() {
//создали объект персон он может быть нулл
        var p: Person? = null


//если п не нулл
        if (p != null) {
//то выводим
            Log.d("mylogs", "${p.name} ${p.age}")
        }

        //
        p?.let {
            //присвоили п нулл
            p = null
            // п выводится до вызова лет
            Log.d("mylogs", "${it.name} ${it.age}")
        }
        p?.run {
            Log.d("mylogs", "${this.name} ${age}")
        }
        val person2 = Person("", 1)
        val person = Person("", 1)


//работаем с персон как с параметром
        //with() из висс возрощается результат исполнения скобок
        val resultWith = with(person) {
            //зиис можно опускать
            this.name = "sdf"
            age = 1
            //возращает 2
            2
        }


//работаем с персон как с ресивером
        //.let возрощает результат переданный внутрь лямбды
        val newPerson2 = person.let {
            ""
        }


        //работаем с персон как с ресивером
        //.run возрощает результат лямбды
        run { // отдельная область видимости
            val person2 = Person("", 1)
        }

        val resultRun = person.run {
            this.name = "ApplyName"
            //возращает 5ф
            5f
        }


        //работаем с персон как с ресивером
        //.also возращает зиис(персон) с изменениями(пришел ресивер ресивер и вернули, т.е. возращает тот же объект)
        val newPersonAlso = person.also {
            //отличается от апплая что работаем с параметром через ит, а не на прямую
            it.name = "ApplyName"
            //параметр ит приходит
            it.name = "ApplyName"
            it.age = 50
        }


        //работаем с персон как с ресивером
        //.apply возращает зиис(персон) с изменениями(пришел ресивер ресивер и вернули, т.е. возращает тот же объект)
        val newPersonApply = person.apply {
            this.name = "ApplyName"
            name = "ApplyName"
            age = 50
        }

    }


}
