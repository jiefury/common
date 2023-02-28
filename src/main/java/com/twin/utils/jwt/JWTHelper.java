package com.twin.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTHelper {


    public static JWTInfo parserToken(String token) throws Exception {
        DecodedJWT decode = JWT.decode(token);
        String username = decode.getClaim("username").asString();
        Long userId = decode.getClaim("id").asLong();
        Integer type = decode.getClaim("type").asInt();
        Long deptId = decode.getClaim("deptId").asLong();
        Long auditId = decode.getClaim("auditId").asLong();
        Integer isAdmin = decode.getClaim("isAdmin").asInt();
        JWTInfo jwtInfo = new JWTInfo();
        jwtInfo.setToken(token);
        jwtInfo.setUserId(userId);
        jwtInfo.setDeptId(deptId);
        jwtInfo.setType(type);
        jwtInfo.setUsername(username);
        jwtInfo.setAuditId(auditId);
        jwtInfo.setIsAdmin(isAdmin);
        return jwtInfo;
    }


}