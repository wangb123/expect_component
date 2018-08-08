package org.wbing.wbase.ui

interface WCallback<Data> {
    /**
     * 数据请求成功
     *
     * @param data 请求到的数据
     */
    fun onSuccess(data: Data)

    /**
     * 使用网络API接口请求方式时，虽然已经请求成功但是由
     * 于`msg`的原因无法正常返回数据。
     * @param code 错误码，用于定位查找错误位置或原因，
     * @param msg 错误原因
     */
    fun onFailure(code: Int, msg: String)

    /**
     * 请求数据失败，指在请求网络API接口请求方式时，出现无法联网、
     * 缺少权限，内存泄露等原因导致无法连接到请求数据源。
     * @param throwable 异常信息
     */
    fun onError(throwable: Throwable)


    /**
     * 当请求数据结束时，无论请求结果是成功，失败或是抛出异常都会执行此方法给用户做处理，通常做网络
     * 请求时可以在此处隐藏“正在加载”的等待控件
     */
    fun onComplete()
}
