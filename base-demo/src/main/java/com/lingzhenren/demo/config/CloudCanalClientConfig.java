package com.lingzhenren.demo.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.enums.SyncHandlerEnum;
import com.lingzhenren.demo.mapper.CanalBinlogLogMapper;
import com.lingzhenren.demo.utils.CanalJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Configuration
public class CloudCanalClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(CloudCanalClientConfig.class);
//    @Autowired
//    private CanalBinlogLogMapper canalBinlogLogMapper;

    @PostConstruct
    public void startCanalClient() {
        new Thread(() -> {
            CanalConnector connector = CanalConnectors.newSingleConnector(
                    new InetSocketAddress("127.0.0.1", 11111),
                    "cloud", "", ""
            );

            connector.connect();
            connector.subscribe(); // 订阅目标库表（需与 instance 配置匹配）
            connector.rollback();

            while (true) {
                try {
                    Message message = connector.getWithoutAck(100);
                    long batchId = message.getId();
                    List<Entry> entries = message.getEntries();

                    if (batchId != -1 && !entries.isEmpty()) {
                        for (Entry entry : entries) {
                            if (entry.getEntryType() != EntryType.ROWDATA) continue;

                            RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
                            String tableName = entry.getHeader().getTableName();
                            if ("canal_binlog_log".equals(tableName)){
                                continue;
                            }
                            String opType = rowChange.getEventType().toString();
                            long binlogTs = entry.getHeader().getExecuteTime();

                            for (RowData rowData : rowChange.getRowDatasList()) {
                                String beforeJson = CanalJsonUtil.columnsToJson(rowData.getBeforeColumnsList());
                                String afterJson = CanalJsonUtil.columnsToJson(rowData.getAfterColumnsList());

                                CanalBinlogLog log = new CanalBinlogLog();
                                log.setTableName(tableName);
                                log.setOpType(opType);
                                log.setBeforeContent(SyncHandlerEnum.DELETE.getDesc().equals(opType) || SyncHandlerEnum.UPDATE.getDesc().equals(opType) ? beforeJson : null);
                                log.setAfterContent(SyncHandlerEnum.INSERT.getDesc().equals(opType) || SyncHandlerEnum.UPDATE.getDesc().equals(opType) ? afterJson : null);
                                log.setBinlogTs(binlogTs);
                                log.setSyncStatus("0");
                                logger.info("canal CanalBinlogLog: {}", log);
//                                canalBinlogLogMapper.insert(log);
                            }
                        }
                    }

                    connector.ack(batchId);
                } catch (Exception e) {
                    log.error("Canal 消费异常", e);
                }
            }
        }, "canal-client-thread").start();
    }
}
