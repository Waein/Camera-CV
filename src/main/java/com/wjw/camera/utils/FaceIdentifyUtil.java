package com.wjw.camera.utils;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.FaceVerifyRequest;
import com.baidu.aip.face.MatchRequest;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ===================================
 * Created With IntelliJ IDEA
 *
 * @author Waein :)
 * @version method: FaceIdentifyUtil, v 0.1
 * @CreateDate 2018/11/6
 * @CreateTime 09:43
 * @GitHub https://github.com/Waein
 * ===================================
 */
public class FaceIdentifyUtil {

    //百度人脸识别应用id
    @Value("baidu.ai.app_id")
    private static String appId;
    //百度人脸识别应用apikey
    @Value("baidu.ai.api_key")
    private static String apiKey;
    //百度人脸识别应用sercetkey
    @Value("baidu.ai.sercet_key")
    private static String sercetKey;
    //百度人脸识别token 有效期一个月
    @Value("baidu.ai.token")
    private static String token;
    
    //百度人脸识别客户端
    static AipFace client = null;

    static {
        client = new AipFace(appId, apiKey, sercetKey);
        // 可选：设置网络连接参数
        // 1.设置http代理
        // client.setHttpProxy("proxy_host", proxy_port);
        // 2.设置socket代理
        // client.setSocketProxy("proxy_host", proxy_port);
        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "src/main/resources/log4j.properties");
        //设置链接超时时间
        client.setConnectionTimeoutInMillis(2000);
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        client.setSocketTimeoutInMillis(60000);
    }

    /**
     * 人脸检测
     *
     * @param file
     * @param max_face_num
     * @return
     */
    public static String detectFace(File file, String max_face_num) {
        try {
            return detectFace(FileToByte(file), max_face_num);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸检测
     *
     * @param arg0
     * @param max_face_num
     * @return
     */
    public static String detectFace(byte[] arg0, String max_face_num) {
        try {
            //传入可选参数调用接口
            HashMap<String, String> options = new HashMap<String, String>();
            options.put("face_field", "age,beauty,expression,faceshape,gender,glasses,race,qualities");
            options.put("max_face_num", "2");
            options.put("face_type", "LIVE");
            // Base64图片转换
            String imgStr = Base64Util.encode(arg0);
            String imageType = "BASE64";
            //人脸检测
            JSONObject res = client.detect(imgStr, imageType, options);
            System.out.println(res.toString(2));
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸比对
     *
     * @param file1
     * @param file2
     * @return
     */
    public static String matchFace(File file1, File file2) {
        try {
            return matchFace(FileToByte(file1), FileToByte(file2));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸比对
     *
     * @param arg0
     * @param arg1
     * @return
     */
    public static String matchFace(byte[] arg0, byte[] arg1) {
        // Base64图片转换
        String imgStr1 = Base64Util.encode(arg0);
        String imgStr2 = Base64Util.encode(arg1);
        MatchRequest req1 = new MatchRequest(imgStr1, "BASE64");
        MatchRequest req2 = new MatchRequest(imgStr2, "BASE64");
        ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
        requests.add(req1);
        requests.add(req2);
        JSONObject res = client.match(requests);
        return res.toString();
    }

    /**
     * 人脸搜索
     *
     * @param file
     * @param groupIdList
     * @param userId
     * @return
     */
    public static String searchFace(File file, String groupIdList, String userId) {
        try {
            return searchFace(FileToByte(file), groupIdList, userId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸搜索
     *
     * @param arg0
     * @param groupIdList
     * @param userId
     * @return
     */
    public static String searchFace(byte[] arg0, String groupIdList, String userId) {
        // Base64图片转换
        String imgStr = Base64Util.encode(arg0);
        String imageType = "BASE64";
        //传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        if (userId != null) {
            options.put("user_id", userId);
        }
        options.put("max_user_num", "1");
        //人脸搜索
        JSONObject res = client.search(imgStr, imageType, groupIdList, options);
        return res.toString(2);
    }

    /**
     * 注册用户
     *
     * @param file
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static String addUser(File file, String userInfo, String userId, String groupId) {
        try {
            return addUser(FileToByte(file), userInfo, userId, groupId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册用户
     *
     * @param arg0
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static String addUser(byte[] arg0, String userInfo, String userId, String groupId) {
        // Base64图片转换
        String imgStr = Base64Util.encode(arg0);
        String imageType = "BASE64";
        //传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", userInfo);
        options.put("quality_control", "NORMAL");
//        options.put("liveness_control", "LOW"); //默认是normal,暂时不开启低活率存储,注册的时候要求质量高一些

        JSONObject res = client.addUser(imgStr, imageType, groupId, userId, options);
        return res.toString(2);
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @param groupId
     * @return
     */
    public static String searchUserInfo(String userId, String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 用户信息查询
        JSONObject res = client.getUser(userId, groupId, options);
        return res.toString(2);
    }

    /**
     * 获取用户人脸列表
     *
     * @param userId
     * @param groupId
     * @return
     */
    public static String getUserFaceList(String userId, String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 获取用户人脸列表
        JSONObject res = client.faceGetlist(userId, groupId, options);
        return res.toString(2);
    }

    /**
     * 获取一组用户
     *
     * @param groupId
     * @param returnNum
     * @return
     */
    public static String getGroupUsers(String groupId, String returnNum) {
        //传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("start", "0");
        if (returnNum != null) {
            options.put("length", returnNum);
        }
        // 获取用户列表
        JSONObject res = client.getGroupUsers(groupId, options);
        return res.toString(2);
    }

    /**
     * 更新用户
     *
     * @param file
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static String updateUser(File file, String userInfo, String userId, String groupId) {
        try {
            return updateUser(FileToByte(file), userInfo, userId, groupId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户
     *
     * @param arg0
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static String updateUser(byte[] arg0, String userInfo, String userId, String groupId) {
        // Base64图片转换
        String imgStr = Base64Util.encode(arg0);
        String imageType = "BASE64";
        //传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        if (userInfo != null) {
            options.put("user_info", userInfo);
        }
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        //更新用户
        JSONObject res = client.updateUser(imgStr, imageType, groupId, userId, options);
        return res.toString(2);
    }

    /**
     * 删除用户 人脸删除
     *
     * @param userId
     * @param groupId
     * @param faceToken
     * @return
     */
    public static String deleteUserFace(String userId, String groupId, String faceToken) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 人脸删除
        JSONObject res = client.faceDelete(userId, groupId, faceToken, options);
        return res.toString();
    }

    /**
     * 组用户复制
     *
     * @param userId
     * @param srcGroupId
     * @param dstGroupId
     * @return
     */
    public static String userCopy(String userId, String srcGroupId, String dstGroupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("src_group_id", srcGroupId);
        options.put("dst_group_id", dstGroupId);
        // 复制用户
        JSONObject res = client.userCopy(userId, options);
        return res.toString(2);
    }

    /**
     * 删除用户
     *
     * @param userId
     * @param groupId
     * @return
     */
    public static String deleteUser(String userId, String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 人脸删除
        JSONObject res = client.deleteUser(groupId, userId, options);
        return res.toString();
    }

    /**
     * 增加组信息
     *
     * @param groupId
     * @return
     */
    public static String addGroup(String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 创建用户组
        JSONObject res = client.groupAdd(groupId, options);
        return res.toString();
    }

    /**
     * 删除用户组
     *
     * @param groupId
     * @return
     */
    public static String deleteGroup(String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 创建用户组
        JSONObject res = client.groupDelete(groupId, options);
        return res.toString();
    }

    /**
     * 获取组列表
     *
     * @param length
     * @return
     */
    public static String getGroupList(String length) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("start", "0");
        options.put("length", length);
        // 组列表查询
        JSONObject res = client.getGroupList(options);
        return res.toString();
    }

    /**
     * 活体检测
     *
     * @param arg0
     * @return
     */
    public static String faceverify(byte[] arg0) {
        String imgStr = Base64Util.encode(arg0);
        String imageType = "BASE64";
        FaceVerifyRequest req = new FaceVerifyRequest(imgStr, imageType);
        ArrayList<FaceVerifyRequest> list = new ArrayList<FaceVerifyRequest>();
        list.add(req);
        JSONObject res = client.faceverify(list);
        return res.toString();
    }

    /**
     * 身份验证
     *
     * @param arg0
     * @param idCardNumber
     * @param name
     * @return
     */
    public static String personVerify(byte[] arg0, String idCardNumber, String name) {
        // Base64图片转换
        String imgStr = Base64Util.encode(arg0);
        String imageType = "BASE64";
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        // 身份验证
        JSONObject res = client.personVerify(imgStr, imageType, idCardNumber, name, options);
        return res.toString(2);
    }


    /**
     * 将文件转为流
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] FileToByte(File file) throws IOException {
        // 将数据转为流
        InputStream content = new FileInputStream(file);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = content.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        // 获得二进制数组
        return swapStream.toByteArray();
    }

}
