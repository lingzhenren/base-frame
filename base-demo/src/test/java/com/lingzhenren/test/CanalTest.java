package com.lingzhenren.test;

import com.alibaba.excel.write.metadata.RowData;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.lingzhenren.demo.DemoApplication;
import javafx.scene.web.WebHistory;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/26
 */
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class CanalTest {

    public static void main(String[] args) {
        // 连接 Canal Server（默认本地部署）
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("127.0.0.1", 11111), // host, port
                "example",  // instanceName
                "canal",         // username
                ""          // password
        );

        try {
            connector.connect();
            connector.subscribe("canal_test\\..*"); // 订阅目标库表（需与 instance 配置匹配）
            connector.rollback(); // 回滚到未 ack 的位置

            while (true) {
                Message message = connector.getWithoutAck(100); // 每次最多获取100条
                long batchId = message.getId();
                int size = message.getEntries().size();

                if (batchId != -1 && size > 0) {
                    printEntries(message.getEntries());
                }

                connector.ack(batchId); // 提交确认
                // 如果出错可以使用 connector.rollback(batchId);
            }
        } finally {
            connector.disconnect();
        }
    }

    private static void printEntries(List<Entry> entries) {
        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.ROWDATA) {
                RowChange rowChange;
                try {
                    rowChange = RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    continue;
                }

                EventType eventType = rowChange.getEventType();
                System.out.printf("\n=== %s %s ===\n", eventType, entry.getHeader().getTableName());

                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    if (eventType == EventType.DELETE) {
                        printColumns(rowData.getBeforeColumnsList());
                    } else if (eventType == EventType.INSERT) {
                        printColumns(rowData.getAfterColumnsList());
                    } else { // UPDATE
                        System.out.println("Before:");
                        printColumns(rowData.getBeforeColumnsList());
                        System.out.println("After:");
                        printColumns(rowData.getAfterColumnsList());
                    }
                }
            }
        }
    }

    private static void printColumns(List<Column> columns) {
        for (Column column : columns) {
            System.out.printf("%s = %s (updated=%s)\n",
                    column.getName(), column.getValue(), column.getUpdated());
        }
    }
}
