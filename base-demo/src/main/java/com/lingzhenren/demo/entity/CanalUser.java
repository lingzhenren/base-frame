package com.lingzhenren.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
@TableName("canal_user")
@Data
public class CanalUser {

    @TableId(type = IdType.NONE)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("sex")
    private String sex;

}
