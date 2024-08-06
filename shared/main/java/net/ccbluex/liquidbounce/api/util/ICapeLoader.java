package net.ccbluex.liquidbounce.api.util;

import net.ccbluex.liquidbounce.utils.PacketUtil;
import net.minecraft.util.HttpUtil;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;

public abstract class ICapeLoader extends JFrame {

    public  static String capename = null;

    public ICapeLoader() {
        String username = JOptionPane.showInputDialog("Username");

        String hwid = buildHWID();

        try {
            if(doLogin(username,"")) {
                loginFinish();
            } else{
                JOptionPane.showInputDialog("Auth Failed Copy UR HWID TO ADMIN.", (Object)hwid);
                System.exit(0);
            }
        } catch (Throwable e) {
            JOptionPane.showInputDialog("Error: "+e+"\nAuth Failed Copy UR HWID TO ADMIN.", (Object)hwid);
            System.exit(0);
            throw new RuntimeException(e);
        }

    }

    public static final String buildHWID() {
        return PacketUtil.getHWID();
    }

    private static String AuthSrv = "http://quanqian.gzs.vin/2rffdgergerg/dw32tgerfhg43e5rh3/hy5tke45jrtr/auth.php";

    /*public  LoginWindow() {
        setResizable(false);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        Box box = Box.createVerticalBox();
        Box usernameBox = Box.createHorizontalBox();
        usernameBox.add(new JLabel("账号: "));
        JTextField usernameField = new JTextField();
        usernameField.setColumns(20);
        usernameBox.add(usernameField);
        Box passwordBox = Box.createHorizontalBox();
        passwordBox.add(new JLabel("密码: "));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setColumns(20);
        passwordBox.add(passwordField);
        box.add(usernameBox);
        box.add(passwordBox);
        Box loginBox = Box.createHorizontalBox();
        loginBox.add(Box.createHorizontalGlue());
        JButton loginButton = new JButton("登录");
        JButton hwidButton = new JButton("复制HWID");
        JButton sauthButton = new JButton("切换验证服务器");
        hwidButton.addActionListener(e -> {
            try {

                String hwid = getHWID();
                Transferable trans = new StringSelection(hwid);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
                JOptionPane.showMessageDialog(this, "HWID已复制到您的剪贴板", "L", JOptionPane.INFORMATION_MESSAGE);
            } catch(Throwable t) {
                JOptionPane.showMessageDialog(this, t, "复制HWID失败", JOptionPane.ERROR_MESSAGE);
            }
        });

        sauthButton.addActionListener(e -> {
            try {
                if(AuthSrv == "http://quanqian.gzs.vin/2rffdgergerg/dw32tgerfhg43e5rh3/hy5tke45jrtr/auth.php"){
                    AuthSrv = "http://quanqian.gzs.vin/2rffdgergerg/dw32tgerfhg43e5rh3/hy5tke45jrtr/auth.php";
                    System.out.println(AuthSrv);

                    JOptionPane.showMessageDialog(this, "切换到线路1", "L", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    AuthSrv = "http://quanqian.gzs.vin/2rffdgergerg/dw32tgerfhg43e5rh3/hy5tke45jrtr/auth.php";
                    System.out.println(AuthSrv);

                    JOptionPane.showMessageDialog(this, "切换到线路2", "L", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch(Throwable t) {
                AuthSrv = "http://quanqian.gzs.vin/2rffdgergerg/dw32tgerfhg43e5rh3/hy5tke45jrtr/auth.php";
            }
        });
        loginBox.add(loginButton);
        loginBox.add(hwidButton);
        loginBox.add(sauthButton);
        loginBox.add(Box.createHorizontalGlue());
        box.add(loginBox);
        add(box);
        pack();
        setLocationRelativeTo(null);
        loginButton.addActionListener(e -> {
            try {
                if(doLogin(usernameField.getText(), new String(passwordField.getPassword()))) {
                    LoginWindow.this.dispose();
                    loginFinish();
                } else {
                    JOptionPane.showMessageDialog(this, "输入的信息和记录的不匹配", "L", JOptionPane.ERROR_MESSAGE);
                }
            } catch(Throwable t) {
                JOptionPane.showMessageDialog(this, t, "登录失败", JOptionPane.ERROR_MESSAGE);
            }
        });
        setTitle("Loserline");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }*/

    // 在这里进行登录操作\
    
    private boolean doLogin(String username, String password) throws Throwable {
        HashMap<String, Object> data = new HashMap<>();
        loginFinish();
        data.put("username", username);
        data.put("password",password);
        data.put("hwid", getHWID());
        //data.put("username", "Paimonqwq");
        //data.put("password","DXGKRNL114514@lifeline");
        //data.put("hwid", getHWID());
        String result = HttpUtil.postMap(new URL(AuthSrv), data, true,null);
        return result.contains("ok");
    }
    
    private static String getHWID() throws Throwable {
        return PacketUtil.getHWID();
    }

    public abstract void loginFinish();

}
