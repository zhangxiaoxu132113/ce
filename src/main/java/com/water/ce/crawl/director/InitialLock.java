package com.water.ce.crawl.director;

/**
 * Created by zhangmiaojie on 2017/2/14.
 */
public class InitialLock {
    private boolean isInit = false;
    private Object obj = new Object();

    public InitialLock() {

    }

    public void doInit(Runnable runnable) {
        if (!this.isInit) {
            Object synch = this.obj;
            synchronized (InitialLock.class) {
                if (!this.isInit) {
                    this.isInit = true;
                    runnable.run();
                }
            }
        }
    }
}
