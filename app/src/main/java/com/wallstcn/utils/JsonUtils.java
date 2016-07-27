package com.wallstcn.utils;

import android.text.TextUtils;
import android.util.Log;

import com.wallstcn.bean.ChannalIemBean;
import com.wallstcn.entity.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by _GZL on 2016/2/29.
 * Json的解析类
 * 异常时  均有默认的返回值 并打印错误Log
 *
 * 关于性能
 */
public class JsonUtils {
    private final static String CATEGORY = "category";
    private final static String ID = "id";
    private final static String CATEGORY_NAME = "categoryName";
    private final static String AUTHORS = "authors";
    private final static String FOLLOWSTATUS = "followStatus";
    private final static String SUBSCRIPTION_NUM = "subscriptionNum";
    private final static String INTRO = "intro";
    private final static String AVATAR = "avatar";
    private final static String NICK_NAME = "nickname";

    //获取单个Item中的Id  异常时  返回 -1
    public static int getChannelItemID(JSONObject obj) {
        if (null == obj) {
            return -1;
        }
        int id = -1;
        try {
            id = obj.getInt(ID);
        } catch (Exception e) {
            showErrLog(e);
            return -1;
        }
        return id;
    }


    //从服务器返回的JSONArray 中解析 顶部导航栏中所需的ItemName集合
    public static ArrayList<String> getChannalItemNames(JSONArray jarry) {
//        long stratTime = System.currentTimeMillis();  0-1毫秒级
        if (null == jarry) {
            return null;
        }
        int len = jarry.length();
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            try {
                JSONObject obj = jarry.getJSONObject(i);
                String jstr = obj.getString(CATEGORY);
                JSONObject jobj = new JSONObject(jstr);
                String result = jobj.getString(CATEGORY_NAME);
                if (TextUtils.isEmpty(result)) {
                    items.add("");
                } else {
                    items.add(result.trim());//这里一定要加 trim()方法  不然会出现 导航栏 排版问题
                }
            } catch (Exception e) {
                showErrLog(e);
                return null;
            }
        }
//        long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getChannalItemNames used: " + (endTime - stratTime));
        return items;
    }

    //获取当个Item中的SubscriptNum 异常时 返回-1
    public static int getChannelItemSubscriptNum(JSONObject obj) {
//        long startTime = System.currentTimeMillis();  0-1毫秒级
        if (null == obj) {
            return -1;
        }
        int id = -1;
        try {
            id = obj.getInt(SUBSCRIPTION_NUM);
        } catch (Exception e) {
            showErrLog(e);
            return -1;
        }
//        long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getChannelItemSubscriptNum used: " + (endTime - startTime));
        return id;
    }

    //回去单个Item中的avatar 异常时 返回null
    public static String getChannelItemAvatar(JSONObject obj) {
//        long startTime = System.currentTimeMillis();  0-1毫秒级
        if (null == obj) {
            return null;
        }
        String str = null;
        try {
            str = obj.getString(AVATAR);
        } catch (Exception e) {
            showErrLog(e);
            return null;
        }
//        long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getChannelItemAvatar used: " + (endTime - startTime));
        return str;
    }

    //获取单个Item中的Intro 异常时 返回null
    public static String getChannelItemIntro(JSONObject obj) {
//        long startTime = System.currentTimeMillis();  0-1毫秒级
        if (null == obj) {
            return null;
        }
        String str = null;
        try {
            str = obj.getString(INTRO);
        } catch (Exception e) {
            showErrLog(e);
            return null;
        }
//          long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getChannelItemIntro used: " + (endTime - startTime));
        return str;
    }

    //
    public static boolean getChannelItemFollosStatus(JSONObject obj) {
//        long startTime = System.currentTimeMillis();  0-1毫秒级
        if (null == obj) {
            return false;
        }
        boolean flag = false;
        try {
            flag = obj.getBoolean(FOLLOWSTATUS);
        } catch (Exception e) {
            showErrLog(e);
            return false;
        }
//        long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getChannelItemFollosStatus used: " + (endTime - startTime));
        return flag;
    }

    public static String getChannelItemNickName(JSONObject obj) {
//        long startTime = System.currentTimeMillis();  0-1毫秒级
        if (null == obj) {
            return null;
        }
        String nickName = null;
        try {
            nickName = obj.getString(NICK_NAME);
        } catch (Exception e) {
            showErrLog(e);
            return null;
        }
//        long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getChannelItemNickName used: " + (endTime - startTime));
        return nickName;
    }

    public static String getAuthors(JSONObject obj) {
//        long startTime = System.currentTimeMillis();  0-1毫秒级
        if (null == obj) {
            return null;
        }
        String str = null;
        try {
            str = obj.getString(AUTHORS);
        } catch (Exception e) {
            showErrLog(e);
            return null;
        }
//        long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getAuthors used: " + (endTime - startTime));
        return str;
    }

    public static void showErrLog(Exception e) {
        Log.e(Config.LOG_TAG, e.toString());
        e.printStackTrace();
    }

    public static ArrayList<ChannalIemBean>[] getItems(JSONArray jarry) {
//        long startTime = System.currentTimeMillis();  耗时约31毫秒
        if (null == jarry) {
            return null;
        }
        int len = jarry.length();
        ArrayList<ChannalIemBean>[] items = new ArrayList[len];
        for (int i = 0; i < len; i++) {
            try {
                JSONObject jobj = (JSONObject) jarry.get(i);
                String str = getAuthors(jobj);
                JSONArray array = new JSONArray(str);
                items[i] = new ArrayList<>();
                int length = array.length();
                for (int j = 0; j < length; j++) {
                    JSONObject tmpobj = (JSONObject) array.get(j);
                    ChannalIemBean bean = new ChannalIemBean(tmpobj);
                    items[i].add(bean);
                }
            } catch (Exception e) {
                showErrLog(e);
                return null;
            }
        }
//        long endTime = System.currentTimeMillis();
//        Log.e("TAG", "getItems used: " + (endTime - startTime));
        return items;
    }
}
