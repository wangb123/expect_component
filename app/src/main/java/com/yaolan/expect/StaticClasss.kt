package com.yaolan.expect

open class StaticClass {
    companion object {
        const val str:String = "fdsa"

        fun fun1(): String {
            return str
        }

        fun fun2(): String {
            return str
        }
    }
}

open class StaticClazz {
    companion object {
      private  const val str:String = "fdsa"

        fun fun1(): String {
            return str
        }

        fun fun2(): String {
            return str
        }
    }
}
