package com.yaolan.expect

import android.content.Context
import android.content.Intent
import android.util.Log
import com.yaolan.expect.databinding.AppActMainBinding
import org.wbing.wbase.ui.impl.WAct


class Act : WAct<AppActMainBinding>() {

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, Act::class.java)
            context.startActivity(starter)
        }
    }

    override fun recycle() {
        Log.e("TAG", "loadData")
    }

    override fun layoutId(): Int {
        return R.layout.app_act_main
    }


    override fun loadData() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

}
