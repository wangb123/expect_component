package org.wbing.wbase.ui

import android.databinding.ViewDataBinding

/**
 * presenter抽象
 */
interface WPresenter<Binding : ViewDataBinding, View : WView<Binding>> {
    /**
     * 当view添加时调用
     */
    fun attachView(view: View)

    /**
     * 当View移除时调用
     */
    fun detachView()
}