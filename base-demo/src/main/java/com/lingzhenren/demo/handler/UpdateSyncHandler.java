package com.lingzhenren.demo.handler;

import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;
import org.springframework.stereotype.Component;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
@Component
public class UpdateSyncHandler implements SyncHandler{
    @Override
    public SyncHandlerEnum getSyncHandlerEnum() {
        return SyncHandlerEnum.UPDATE;
    }

    @Override
    public void excute(CanalBinlogLog canalBinlogLog) {

    }
}
