package com.lingzhenren.demo.handler;

import com.alibaba.fastjson.JSON;
import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
@Component
public class InsertSyncHandler implements SyncHandler{

    @Override
    public SyncHandlerEnum getSyncHandlerEnum() {
        return SyncHandlerEnum.INSERT;
    }


    @Override
    public void excute(CanalBinlogLog canalBinlogLog) {
        //1、获取表名、构建插入SQL语句

    }


    private String syncInsert(String table, String json) {
        Map<String, Object> map = JSON.parseObject(json);
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        map.forEach((k, v) -> {
            fields.append(k).append(",");
            values.append("'").append(v).append("',");
        });

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                table,
                fields.substring(0, fields.length() - 1),
                values.substring(0, values.length() - 1)
        );
    }
}
