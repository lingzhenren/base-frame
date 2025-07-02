package com.lingzhenren.demo.utils;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class CanalJsonUtil {
    public static String columnsToJson(List<CanalEntry.Column> columns) {
        JSONObject json = new JSONObject();
        for (CanalEntry.Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        return json.toJSONString();
    }
}
