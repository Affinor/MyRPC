package com.jin.myconfig.center.spirngboot.datasource;

import lombok.Data;

/**
 * @author wangjin
 */
@Data
public class DataSourceInfo {
    private String connUrl;
    private String userName;
    private String password;
}
