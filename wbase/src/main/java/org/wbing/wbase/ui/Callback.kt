package org.wbing.wbase.ui

interface Callback<Param, Result> {
    fun call(param: Param): Result
}
