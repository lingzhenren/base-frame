package com.lingzhenren.demo.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;
import com.lingzhenren.demo.utils.DataSourceSwitcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
@Component
public class InsertSyncHandler implements SyncHandler{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public SyncHandlerEnum getSyncHandlerEnum() {
        return SyncHandlerEnum.INSERT;
    }


    @DS(value = DataSourceSwitcher.CLOUD)
    @Override
    public void excute(CanalBinlogLog canalBinlogLog) {
        //1、获取表名、构建插入SQL语句
        jdbcTemplate.execute(buildSql(canalBinlogLog));
    }

    @Override
    public String buildSql(CanalBinlogLog canalBinlogLog) {
        Map<String, Object> map = JSON.parseObject(canalBinlogLog.getAfterContent());
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        map.forEach((k, v) -> {
            fields.append(k).append(",");
            values.append("'").append(v).append("',");
        });

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                canalBinlogLog.getTableName(),
                fields.substring(0, fields.length() - 1),
                values.substring(0, values.length() - 1)
        );
    }
}
