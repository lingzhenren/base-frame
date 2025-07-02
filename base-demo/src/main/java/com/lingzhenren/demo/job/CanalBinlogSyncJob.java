package com.lingzhenren.demo.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;
import com.lingzhenren.demo.factory.SyncHandlerFactory;
import com.lingzhenren.demo.handler.SyncHandler;
import com.lingzhenren.demo.mapper.CanalBinlogLogMapper;
import com.lingzhenren.demo.utils.NetworkUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private JdbcTemplate cloudJdbcTemplate; // 云端目标库 JdbcTemplate

    @XxlJob("canalBinlogSyncJob")
    public void execute() {
        logger.info("XXL-JOB 同步开始");

        if(!NetworkUtil.isNetworkAvailable()){
            return;
        }
        List<CanalBinlogLog> logs = canalBinlogLogMapper.selectList(
                new LambdaQueryWrapper<CanalBinlogLog>()
                        .eq(CanalBinlogLog::getSyncStatus, "0")
                        .orderByAsc(CanalBinlogLog::getBinlogTs)
                        .last("LIMIT 100")
        );

        for (CanalBinlogLog logEntry : logs) {
            try {
                logger.info("开始同步 id={} opType={} table={}", logEntry.getId(), logEntry.getOpType(), logEntry.getTableName());
                SyncHandler syncHandler = syncHandlerFactory.getSyncHandler(SyncHandlerEnum.getByDesc(logEntry.getOpType()));
                syncHandler.excute(logEntry);
//                String opType = logEntry.getOpType();
//                String table = logEntry.getTableName();
//                String afterJson = logEntry.getAfterContent();
//                String beforeJson = logEntry.getBeforeContent();
//
//                if ("INSERT".equalsIgnoreCase(opType)) {
//                    syncInsert(table, afterJson);
//                } else if ("UPDATE".equalsIgnoreCase(opType)) {
//                    syncUpdate(table, afterJson);
//                } else if ("DELETE".equalsIgnoreCase(opType)) {
//                    syncDelete(table, beforeJson);
//                }

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


    // 插入 SQL 构造
    private void syncInsert(String table, String json) {
        Map<String, Object> map = JSON.parseObject(json);
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        map.forEach((k, v) -> {
            fields.append(k).append(",");
            values.append("'").append(v).append("',");
        });

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                table,
                fields.substring(0, fields.length() - 1),
                values.substring(0, values.length() - 1)
        );

        cloudJdbcTemplate.execute(sql);
    }

    // 更新 SQL 构造（要求存在 id 主键）
    private void syncUpdate(String table, String json) {
        Map<String, Object> map = JSON.parseObject(json);
        if (!map.containsKey("id")) throw new RuntimeException("UPDATE 必须包含主键 id");

        StringBuilder setClause = new StringBuilder();
        Object id = map.remove("id"); // remove id from SET

        map.forEach((k, v) -> {
            setClause.append(k).append("='").append(v).append("',");
        });

        String sql = String.format("UPDATE %s SET %s WHERE id='%s'",
                table,
                setClause.substring(0, setClause.length() - 1),
                id
        );

        cloudJdbcTemplate.execute(sql);
    }

    // 删除 SQL 构造（要求存在 id 主键）
    private void syncDelete(String table, String json) {
        Map<String, Object> map = JSON.parseObject(json);
        if (!map.containsKey("id")) throw new RuntimeException("DELETE 必须包含主键 id");

        Object id = map.get("id");

        String sql = String.format("DELETE FROM %s WHERE id='%s'", table, id);

        cloudJdbcTemplate.execute(sql);
    }
}
