package net.ccbluex.liquidbounce.utils;

import antiskidderobfuscator.NativeMethod;
import net.ccbluex.liquidbounce.LiquidBounce;

import javax.swing.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Naitve {

@NativeMethod
    public static void abcd() {
        String username = JOptionPane.showInputDialog("Input Exhale Username");
        String password = JOptionPane.showInputDialog("Input password");
        String httpUrl="http://1auth.test.upcdn.net/";
        String param=username+"-"+password;
        HttpURLConnection connection = null;
        OutputStream os = null;


        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            os = connection.getOutputStream();
            os.write(param.getBytes());
            System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == 200) {

                LiquidBounce.INSTANCE.setUsername(username);

                LiquidBounce.INSTANCE.startClient();
                }else {


                JOptionPane.showMessageDialog(null, "验证失败,账号或密码错误请重启重新验证，自己关掉客户端","Error",JOptionPane.ERROR_MESSAGE);
                while (true){

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
            connection.disconnect();
        }




@NativeMethod
    public static String getSubString(String text, String left, String right) {//也可以用gson解析返回
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
}
