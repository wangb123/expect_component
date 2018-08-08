package org.wbing.wbase.ui

import android.app.Activity
import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Handler

/**
 * View层接口抽象，不同view需做不同实现
 */
interface WView<Binding : ViewDataBinding> {
    /**
     * @return IView的上下文
     */
    fun getContext(): Context?

    /**
     * @return IView所在的activity
     */
    fun getActivity(): Activity?

    /**
     * @return IView的ViewModel对象
     */
    fun getBinding(): Binding?

    /**
     * @return main线程的handler
     */
    fun getHandler(): Handler?
}