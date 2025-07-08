package com.lingzhenren.demo.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;
import com.lingzhenren.demo.utils.DataSourceSwitcher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
@Component
public class UpdateSyncHandler implements SyncHandler{
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public SyncHandlerEnum getSyncHandlerEnum() {
        return SyncHandlerEnum.UPDATE;
    }

    @DS(value = DataSourceSwitcher.CLOUD)
    @Override
    public void excute(CanalBinlogLog canalBinlogLog) {
        jdbcTemplate.execute(buildSql(canalBinlogLog));
    }

    @Override
    public String buildSql(CanalBinlogLog canalBinlogLog) {
        Map<String, Object> map = JSON.parseObject(canalBinlogLog.getAfterContent());
        if (!map.containsKey("id")) throw new RuntimeException("UPDATE 必须包含主键 id");

        StringBuilder setClause = new StringBuilder();
        Object id = map.remove("id"); // remove id from SET

        map.forEach((k, v) -> {
            setClause.append(k).append("='").append(v).append("',");
        });

        return String.format("UPDATE %s SET %s WHERE id='%s'",
                canalBinlogLog.getTableName(),
                setClause.substring(0, setClause.length() - 1),
                id
        );
    }
}
