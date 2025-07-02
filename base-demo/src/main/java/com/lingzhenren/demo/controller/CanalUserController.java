package com.lingzhenren.demo.controller;

import com.lingzhenren.demo.entity.CanalUser;
import com.lingzhenren.demo.service.CanalUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/7/1
 */
@RestController
@RequestMapping("/canalUser")
public class CanalUserController {

    @Resource
    private CanalUserService canalUserService;

    @PostMapping("/save")
    public boolean save(@RequestBody CanalUser canalUser) {
        return canalUserService.save(canalUser);
    }
}
