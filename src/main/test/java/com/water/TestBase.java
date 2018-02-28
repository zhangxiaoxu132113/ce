package com.water;

import com.water.ce.web.service.impl.InfoQServiceImpl;

/**
 * Created by admin on 2017/11/20.
 */
public class TestBase {
    public static void main(String[] args) {
        InfoQServiceImpl service = new InfoQServiceImpl();
        service.fetchToutiao();
    }
}
