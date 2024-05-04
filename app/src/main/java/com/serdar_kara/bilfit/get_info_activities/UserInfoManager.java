package com.serdar_kara.bilfit.get_info_activities;

import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;

public class UserInfoManager {
    private static UserInfoManager instance;
    private UserInfoHolder userInfo;

    private UserInfoManager() { }

    public static synchronized UserInfoManager getInstance() {
        if (instance == null) {
            instance = new UserInfoManager();
        }
        return instance;
    }

    public UserInfoHolder getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoHolder userInfo) {
        this.userInfo = userInfo;
    }
}
