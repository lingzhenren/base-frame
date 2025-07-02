package com.lingzhenren.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName("canal_binlog_log")
public class CanalBinlogLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("table_name")
    private String tableName;

    @TableField("op_type")
    private String opType;

    @TableField("before_content")
    private String beforeContent;

    @TableField("after_content")
    private String afterContent;

    @TableField("binlog_ts")
    private Long binlogTs;

    @TableField("sync_status")
    private String syncStatus;

    @TableField("error_msg")
    private String errorMsg;

    @TableField("create_time")
    private Date createTime;  // 可由数据库自动填充，无需 insert
}
