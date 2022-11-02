package com.app.common;

import com.app.service.Lock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ZkLockWithCuratorTemplate implements Lock {

    private InterProcessMutex lock;

    public ZkLockWithCuratorTemplate() {
        lock = new InterProcessMutex(CuratorBuild.build(), "/bin");
    }

    @Override
    public boolean getLock() throws Exception {
        return lock.acquire(5, TimeUnit.SECONDS);
    }

    @Override
    public void unLock() throws Exception {
        lock.release();
    }
}
