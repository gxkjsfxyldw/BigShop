package com.ldw.shop.common.constant;

public abstract class AbstractCache {

    /**
     * 缓存
     */
    public abstract void init();

    /**
     * 获取缓存
     *
     * @param <T>
     * @return
     */
    public abstract <T> T get();

    /**
     * 清理缓存
     */
    public abstract void clear();

    /**
     * 重新加载
     */
    public void reload() {
        clear();
        init();
    }
}
