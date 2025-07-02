package com.lingzhenren.demo.enums;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
public enum SyncHandlerEnum {

    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");
    private String desc;
    SyncHandlerEnum(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public static SyncHandlerEnum getByDesc(String value) {
        for (SyncHandlerEnum item : SyncHandlerEnum.values()) {
            if (item.getDesc().equals(value)) {
                return item;
            }
        }
        return null;
    }

}
