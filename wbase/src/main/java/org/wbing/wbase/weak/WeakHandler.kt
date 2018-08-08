package org.wbing.wbase.weak

import android.os.Handler
import android.os.Looper
import android.os.Message

import java.lang.ref.WeakReference

class WeakHandler(looper: Looper, callback: Handler.Callback) : Handler(looper) {

    private val callback: WeakReference<Handler.Callback> = WeakReference(callback)

    override fun handleMessage(msg: Message) {
        val call = this.callback.get()
        call?.handleMessage(msg)
    }
}
