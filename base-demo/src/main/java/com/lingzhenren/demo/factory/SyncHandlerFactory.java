package com.lingzhenren.demo.factory;

import com.lingzhenren.demo.enums.SyncHandlerEnum;
import com.lingzhenren.demo.handler.SyncHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
@Component
public class SyncHandlerFactory implements InitializingBean {

    @Resource
    private List<SyncHandler> syncHandlerList;

    private final Map<SyncHandlerEnum, SyncHandler> syncHandlerMap = new HashMap<>();

    public SyncHandler getSyncHandler(SyncHandlerEnum syncHandlerEnum) {
        return syncHandlerMap.get(syncHandlerEnum);
    }
    @Override
    public void afterPropertiesSet() throws Exception {

        for (SyncHandler syncHandler : syncHandlerList) {
            syncHandlerMap.put(syncHandler.getSyncHandlerEnum(), syncHandler);
        }
    }
}
