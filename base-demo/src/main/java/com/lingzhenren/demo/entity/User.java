package com.lingzhenren.demo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/13
 */
@Data
@Accessors(chain = true)
public class User {

    private String name;

    private Date now;
}
