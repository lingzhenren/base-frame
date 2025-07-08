package com.lingzhenren.demo.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;
import com.lingzhenren.demo.factory.SyncHandlerFactory;
import com.lingzhenren.demo.handler.SyncHandler;
import com.lingzhenren.demo.mapper.CanalBinlogLogMapper;
import com.lingzhenren.demo.utils.NetworkUtil;
import com.lingzhenren.demo.utils.PingUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class CanalBinlogSyncJob {

    private static final Logger logger = LoggerFactory.getLogger(CanalBinlogSyncJob.class);
    @Autowired
    private CanalBinlogLogMapper canalBinlogLogMapper;
    @Autowired
    private SyncHandlerFactory syncHandlerFactory;

    @XxlJob("canalBinlogSyncJob")
    public void execute() {
        logger.info("XXL-JOB 同步开始");

        if(!PingUtil.isReachable()){
            return;
        }
        List<CanalBinlogLog> logs = canalBinlogLogMapper.selectList(
                new LambdaQueryWrapper<CanalBinlogLog>()
                        .eq(CanalBinlogLog::getSyncStatus, "0")
                        .orderByAsc(CanalBinlogLog::getBinlogTs)
                        .last("LIMIT 500")
        );

        for (CanalBinlogLog logEntry : logs) {
            try {
                logger.info("开始同步 id={} opType={} table={}", logEntry.getId(), logEntry.getOpType(), logEntry.getTableName());
                SyncHandler syncHandler = syncHandlerFactory.getSyncHandler(SyncHandlerEnum.getByDesc(logEntry.getOpType()));
                syncHandler.excute(logEntry);
                logEntry.setSyncStatus("1");
                logEntry.setErrorMsg(null);
            } catch (Exception e) {
                log.error("同步失败，id={} error={}", logEntry.getId(), e.getMessage());
                logEntry.setSyncStatus("2");
                logEntry.setErrorMsg(e.getMessage());
            }

            canalBinlogLogMapper.updateById(logEntry);
        }

        log.info("XXL-JOB 同步完成，共处理 {} 条记录", logs.size());
    }
}
