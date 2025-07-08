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
public class DeleteSyncHandler implements SyncHandler{

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public SyncHandlerEnum getSyncHandlerEnum() {
        return SyncHandlerEnum.DELETE;
    }

    @DS(value = DataSourceSwitcher.CLOUD)
    @Override
    public void excute(CanalBinlogLog canalBinlogLog) {
        jdbcTemplate.execute(buildSql(canalBinlogLog));
    }

    @Override
    public String buildSql(CanalBinlogLog canalBinlogLog) {
        Map<String, Object> map = JSON.parseObject(canalBinlogLog.getAfterContent());
        if (!map.containsKey("id")) throw new RuntimeException("DELETE 必须包含主键 id");

        Object id = map.get("id");

        return String.format("DELETE FROM %s WHERE id='%s'", canalBinlogLog.getTableName(), id);
    }
}
