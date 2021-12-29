package by.romanovich.theweatherapp.lesson3;

import java.util.ArrayList;
import java.util.List;

public class LessonJava3 extends Object {
    void main(){
        List<Object> strs = new ArrayList<Object>();

        List<? super String> objs = strs;
        objs.add("");

        //String s = strs.get(0);
        //s.toLowerCase();
    }

    boolean isNull(LessonJava3 str){
        /*if(str!=null){
            return false;
        }else{
            return true;
        }*/
        return str!=null;
    }
}
