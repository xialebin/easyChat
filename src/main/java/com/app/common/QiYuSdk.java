package com.app.common;

import com.app.util.DateTimeUtil;
import com.app.util.HttpUtil;
import com.app.util.MD5Util;
import com.app.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.util.Date;

@Component
public class QiYuSdk {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Autowired
    private Environment environment;

    private static String appsecret;
    private static String sendUrl;
    private static String appkey;


    //参数初始化
    @PostConstruct
    public void init(){
        appsecret = environment.getProperty("qiyu.appsecret");
        sendUrl = environment.getProperty("qiyu.sendUrl");
        appkey = environment.getProperty("qiyu.appkey");
    }



    private String encode(String appSecret, String nonce, String time) {
        String content = appSecret + nonce + time;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha1");
            messageDigest.update(content.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }


    public String sendMessage(String userId,String content) throws Exception {

        String md5 = MD5Util.MD5EncodeUtf8(content);
        String time = DateTimeUtil.dateToStr(new Date());

        String checkSum = this.encode(appsecret,md5,time);
        String url = sendUrl + "?appKey="+appkey+"&time="+time+"&checksum="+checkSum;

        String response = HttpUtil.HttpRestClient(url, HttpMethod.POST,content);
        if (!StringUtils.isEmpty(response)) {
            return response;
        }else {
            throw new Exception("接口无返回");
        }
    }


}
