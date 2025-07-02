package com.lingzhenren.demo.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingzhenren.demo.entity.CanalBinlogLog;
import com.lingzhenren.demo.utils.DataSourceSwitcher;

@DS(value = DataSourceSwitcher.LOCAL)
public interface CanalBinlogLogMapper extends BaseMapper<CanalBinlogLog> {
}
