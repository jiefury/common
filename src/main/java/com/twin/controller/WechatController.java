package com.twin.controller;


import com.alibaba.fastjson.JSONObject;
import com.twin.utils.Sign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/wechat")
public class WechatController {


    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.secret}")
    private String secret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    //1.获取token
    // https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&amp;appid=wxb03d4dfaef607b41&amp;secret=958761c9d70b49787eeeabf99d47ada1&nbsp;&nbsp;accessToken = jsonObject.getString(\"access_token\");2.获取JsapiTicket&nbsp;&nbsp;&nbsp;
    // String url = \"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=\" + token + \"&amp;type=jsapi\";resJson.getString(\"ticket\");",

    @RequestMapping("/getSign")
    public Map<String, String> getSign(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
        System.out.println("tokenUrl:" + tokenUrl);
        ResponseEntity<String> entity = restTemplate.getForEntity(tokenUrl, String.class);
        String body = entity.getBody();
        System.out.println(body);
        JSONObject js = JSONObject.parseObject(body);
        String accessToken = js.getString("access_token");
        String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
        System.out.println("ticketUrl:" + ticketUrl);
        ResponseEntity<String> entity1 = restTemplate.getForEntity(ticketUrl, String.class);
        String body1 = entity1.getBody();
        System.out.println(body1);
        JSONObject js1 = JSONObject.parseObject(body1);
        String ticket = js1.getString("ticket");
        Map<String, String> sign = Sign.sign(ticket, url);
        sign.put("appId", appId);
        return sign;
    }
}
