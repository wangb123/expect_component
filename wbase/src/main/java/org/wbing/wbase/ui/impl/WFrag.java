package org.wbing.wbase.ui.impl;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.jetbrains.annotations.Nullable;
import org.wbing.wbase.ui.Callback;
import org.wbing.wbase.ui.WView;

import kotlin.Unit;

public abstract class WFrag<Binding extends ViewDataBinding> extends Fragment implements WView<Binding>, Callback<Message, Unit> {

    /**
     * 持有对象
     */
    private ViewHolder<Binding> holder;
    /**
     * 标识是否释放数据
     */
    private boolean isRecycle = true;
    /**
     * 是否第一次加载
     */
    private boolean isFirst;
    /**
     * 是否忽略第一次加载
     */
    private boolean ignoreFirst;
    /**
     * view是否已经创建完成
     */
    private boolean isPrepared;
    /**
     * 是否显示
     */
    private boolean isInvisible = true;


    /**
     * 生命周期函数，这里主要分三种情况
     * 1、不加载任何View
     * 2、重复使用已经加载过的view
     * 3、创建新的view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirst = true;
        int layout = layoutId();
        //情景1
        if (layout <= 0) {
            postViewCreate();
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        //情景2
        if (holder != null) {
            Binding binding = holder.getBinding();
            if (binding != null) {
                View root = binding.getRoot();
                ViewParent parent = root.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(root);
                    postViewCreate();
                    return root;
                }
            }
        }
        //情景3
        holder = new ViewHolder<>(inflater, layout, container);
        holder.setCallback(this);
        postViewCreate();
        Binding binding = holder.getBinding();
        return binding == null ? super.onCreateView(inflater, container, savedInstanceState) : binding.getRoot();
    }


    /**
     * ViewPager中使用该方法控制显示和隐藏
     *
     * @param isVisibleToUser 是否显示
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * FragmentTransaction的show()和hide()方法最终回调这里
     *
     * @param hidden 是否隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onInvisible();
        } else {
            onVisible();
        }
    }

    /**
     * 生命周期方法，销毁view
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!isRecycle) {
            postRecycle();
        }
    }

    /**
     * 生命周期方法，销毁fragment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isRecycle) {
            postRecycle();
        }
    }

    @Nullable
    @Override
    public Binding getBinding() {
        return holder == null ? null : holder.getBinding();
    }

    @Nullable
    @Override
    public Handler getHandler() {
        return holder == null ? null : holder.getHandler();
    }

    @Override
    public Unit call(Message message) {
        return null;
    }

    public void onPostViewCreate() {
    }


    /**
     * 布局文件
     */
    abstract int layoutId();

    /**
     * 加载资源
     */
    abstract void loadData();

    /**
     * 释放资源
     */
    abstract void recycle();

    private void lazyLoad() {
        if (isPrepared && !isInvisible) {
            if (ignoreFirst || isFirst) {
                ignoreFirst = false;
                isFirst = false;
                postLoadData();
            }
        }
    }

    private void onVisible() {
        isInvisible = false;
        lazyLoad();
    }

    private void onInvisible() {
        isInvisible = true;
    }


    /**
     * 加载资源操作
     */
    private void postLoadData() {
        loadData();
        isRecycle = false;
    }

    /**
     * 释放操作
     */
    private void postRecycle() {
        recycle();
        if (holder != null) {
            holder.recycle();
        }
        isRecycle = true;
        isPrepared = false;
    }

    /**
     * 加载view
     */
    private void postViewCreate() {
        isPrepared = true;
        lazyLoad();
        onPostViewCreate();
    }
}
