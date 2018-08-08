package org.wbing.wbase.ui.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import org.wbing.wbase.ui.Callback
import org.wbing.wbase.ui.WView
import org.wbing.wbase.util.StatusBarUtils

/**
 * Activity的基础类
 */
abstract class WAct<Binding : ViewDataBinding> : AppCompatActivity(), WView<Binding>, Callback<Message, Unit> {
    companion object {

        /**
         * 双击关闭程序的间隔
         */
        private const val BACK_DOUBLE_CLICK_INTERVAL = 2500

        //支持使用Vector（svg）
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    /**
     * 记录点击返回按钮的时间，用于双击返回标识
     */
    private var lastBackPressTime: Long = 0
    /**
     * 标识资源是否被释放
     */
    private var isRecycle: Boolean = false

    private var holder: ViewHolder<Binding>? = null

    /**
     * 生命周期函数，activity创建，获取参数，填充布局
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setTranslucent(this, true)
        window.decorView.setBackgroundColor(Color.WHITE)
        //获取参数
        getParams(intent)
        //设置布局
        val layout = layoutId()
        if (layout > 0) {
            holder = ViewHolder(this, layout)
            holder?.callback = this
        }
    }

    /**
     * 生命周期函数，创建完成，获取数据，并做对应的操作
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //获取数据
        postLoadData()
    }

    /**
     * 当Activity被重用时，回调该方法，需获取最新的参数，并重新获取数据
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        //重新获取参数
        getParams(intent)
        //获取数据
        postLoadData()
    }

    /**
     *activity获取返回值时调用，将获取到的数据下发至所有的fragment，不允许在fragment中直接调用startActivityForResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取所有的fragment并将数据下发
        for (frag in supportFragmentManager.fragments) {
            if (frag is WFrag<*>) {
                frag.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    /**
     * 提前释放资源，因为onDestroy方法会延迟执行
     */
    override fun onPause() {
        super.onPause()
        if (isFinishing && !isRecycle) {
            postRecycle()
        }
    }

    /**
     * 提前释放资源，因为onPause很有可能未执行释放操作
     */
    override fun onStop() {
        super.onStop()
        if (isFinishing && !isRecycle) {
            postRecycle()
        }
    }

    /**
     * 释放一下资源，在这里也做释放的原因是因为activity很大可能不是用户去删除的，导致上方onPause、onStop释放操作未执行
     */
    override fun onDestroy() {
        super.onDestroy()
        if (!isRecycle) {
            postRecycle()
        }
    }

    /**
     * 用户点击返回，回调该方法
     * 这里实现双击返回
     */
    override fun onBackPressed() {
        if (isTaskRoot) {
            val millis = SystemClock.uptimeMillis()
            if (millis - lastBackPressTime > BACK_DOUBLE_CLICK_INTERVAL) {
                //todo 显示一个Toast提示要退出啦
                lastBackPressTime = millis
                return
            }

        }
        super.onBackPressed()
    }

    override fun getContext(): Context {
        return applicationContext
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun getBinding(): Binding? {
        return holder?.binding
    }

    override fun getHandler(): Handler? {
        return holder?.getHandler()
    }

    fun setStatusBarTranslucent(translucent: Boolean) {
        StatusBarUtils.setTranslucent(this, translucent)
    }

    fun setStatusBarBackgroundColor(color: Int, animated: Boolean) {
        StatusBarUtils.setBackgroundColor(this, color, animated)
    }

    fun setStatusBarStyle(dark: Boolean) {
        StatusBarUtils.setStyle(this, dark)
    }


    /**
     * 处理IView中handle的消息
     */
    override fun call(msg: Message): Unit {

    }

    /**
     * 获取各种参数
     */
    fun getParams(intent: Intent?) {

    }

    /**
     * 加载资源
     */
    abstract fun loadData()

    /**
     * 释放资源
     */
    abstract fun recycle()

    /**
     * 布局文件
     */
    abstract fun layoutId(): Int

    /**
     * 加载资源操作
     */
    private fun postLoadData() {
        loadData()
        isRecycle = false
    }

    /**
     * 释放操作
     */
    private fun postRecycle() {
        recycle()
        holder?.recycle()
        isRecycle = true
    }

}