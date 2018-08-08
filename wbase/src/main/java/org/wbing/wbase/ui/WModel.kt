package org.wbing.wbase.ui

/**
 * 定义数据模型层，每个数据模型都有int形id，int占用32位4个字节，long64位8字节，节省内存
 */
interface WModel {
    fun _id(): Int
}
