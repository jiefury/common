package com.twin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
//@RefreshScope
public class ProviderController {
    @GetMapping("invoke")
    public String invoke() {
        return LocalTime.now() + " invoke";
    }

    //    @NacosValue(value = "${test.data}", autoRefreshed = true)
    private String data;
    @Value(value = "${test.data}")
    private String datas;

    @GetMapping("test")
    public String test() {
        return "data ：" + data + ",datas=" + datas;
    }

}
