package org.wbing.wbase.ui.impl

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import org.wbing.wbase.ui.Callback
import org.wbing.wbase.weak.WeakHandler


internal class ViewHolder<Binding : ViewDataBinding> {
    var binding: Binding? = null
        private set
    private var handler: Handler? = null
    var callback: Callback<Message, Unit>? = null

    constructor(activity: Activity, layout: Int) {
        binding = DataBindingUtil.setContentView(activity, layout)
    }

    constructor(inflater: LayoutInflater, layout: Int, parent: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layout, parent, false)
    }

    fun getHandler(): Handler? {
        if (handler == null) {
            synchronized(WeakHandler::class.java) {
                handler = WeakHandler(Looper.getMainLooper(), Handler.Callback { this.handleMessage(it) })
            }
        }
        return handler
    }

    fun recycle() {
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    private fun handleMessage(msg: Message): Boolean {
        callback?.call(msg)
        return true
    }
}
