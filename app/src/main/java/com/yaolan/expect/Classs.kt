package com.yaolan.expect

import android.util.Log

class Classs : StaticClass() {

    fun function(){
        Log.e("tag", fun1())
        Log.e("tag", fun2())
        StaticClass.fun1()
        StaticClazz.fun2()
        StaticClass.str
    }
}