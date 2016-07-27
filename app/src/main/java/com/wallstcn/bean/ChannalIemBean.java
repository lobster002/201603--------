package com.wallstcn.bean;

import com.wallstcn.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by _GZL on 2016/3/3.
 */
public class ChannalIemBean {

    private int id;
    private boolean followStatus;
    private int subscriptionNum;
    private String avatar;
    private String nickname;
    private String intro;

    public void setId(int id) {
        this.id = id;
    }

    public void setFollowStatus(boolean followStatus) {
        this.followStatus = followStatus;
    }

    public void setSubscriptionNum(int subscriptionNum) {
        this.subscriptionNum = subscriptionNum;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public ChannalIemBean(JSONObject obj) {

        id = JsonUtils.getChannelItemID(obj);
        followStatus = JsonUtils.getChannelItemFollosStatus(obj);
        subscriptionNum = JsonUtils.getChannelItemSubscriptNum(obj);
        avatar = JsonUtils.getChannelItemAvatar(obj);
        nickname = JsonUtils.getChannelItemNickName(obj);
        intro = JsonUtils.getChannelItemIntro(obj);
    }

    public ChannalIemBean(int key) {

        id = 0;
        followStatus = false;
        subscriptionNum = 0;
        avatar = "null";
        nickname = "上拉出来的新数据:" + key;
        intro = "上拉出来的新数据: " + key;
    }

    public String getId() {
        return "ID: " + String.valueOf(id);
    }

    public boolean isFollowStatus() {
        return followStatus;
    }

    public int getSubscriptionNum() {
        return subscriptionNum;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIntro() {
        return intro;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id: ").append(id).append("\n").append("followStatus: ").append(followStatus).append("\n").append("subscriptionNum: ")
                .append(subscriptionNum).append("\n").append("avatar: ").append(avatar).append("\n").append("nickname: ").append(nickname)
                .append("\n").append("intro: ").append(intro).append("\n------------------------------------------------------------------------------>\n");
        return sb.toString();
    }
}
