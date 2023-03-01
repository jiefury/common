package com.twin.quarz;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/quartz")
public class QuartzController {

    @Autowired
    SchedulerService schedulerService;

    @PostMapping("/add")
    public String add(@RequestBody QuartzBean quartzBean) throws Exception {
        schedulerService.addJob(quartzBean);
        JSONObject json = new JSONObject();
        json.put("code", "success");
        return json.toJSONString();
    }

    @PostMapping("/list")
    public String list() throws Exception {
        List<QuartzBean> jobs = schedulerService.getJobs();
        JSONObject json = new JSONObject();
        json.put("code", "success");
        json.put("data", jobs);
        return json.toJSONString();
    }

}
