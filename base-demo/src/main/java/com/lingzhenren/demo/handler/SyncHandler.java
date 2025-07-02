package com.lingzhenren.demo.handler;

import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
public interface SyncHandler {


    SyncHandlerEnum getSyncHandlerEnum();

    void excute(CanalBinlogLog canalBinlogLog);
}
