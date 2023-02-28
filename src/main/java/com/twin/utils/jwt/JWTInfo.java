package com.twin.utils.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTInfo implements Serializable {

    private String username;

    private Long userId;

    private Long deptId;

    private String token;

    private Integer type;

    private Long auditId;

    private Integer isAdmin;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JWTInfo jwtInfo = (JWTInfo) o;

        if (!Objects.equals(username, jwtInfo.username)) {
            return false;
        }
        return Objects.equals(userId, jwtInfo.userId);

    }


    public static JWTInfo jwtInfoIgnored() {
        //某些调试需要免密的情况下使用,比如测试下载文件
        JWTInfo jwtInfoIgnored = new JWTInfo();
        jwtInfoIgnored.setUserId(12121L);
        jwtInfoIgnored.setUsername("admin");
        return jwtInfoIgnored;
    }

}
