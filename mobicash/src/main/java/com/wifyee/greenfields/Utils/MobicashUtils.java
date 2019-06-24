package com.wifyee.greenfields.Utils;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.wifyee.greenfields.R;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by sumanta on 12/13/16.
 */

public class MobicashUtils {


    public static final String ZERO_BALANCE = "0";
    public static final String AGE_ZERO = "0";
    public static final String AGE_ZERO_DIGITS = "00";
    public static final String BLANK_STRING = "";

    public static final String TRANSFER_TYPE_NEFT = "NEFT";
    public static final String TRANSFER_TYPE_IMPS = "IMPS";

    /**
     * return sh1 string pattern.
     *
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


 //Generating Md5 code
    public static String md5(String pass)  {
            String password = null;
            MessageDigest mdEnc;
            try {
                mdEnc = MessageDigest.getInstance("MD5");
                mdEnc.update(pass.getBytes(), 0, pass.length());
                pass = new BigInteger(1, mdEnc.digest()).toString(16);
                while (pass.length() < 32) {
                    pass = "0" + pass;
                }
                password = pass;
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            return password;
    }
    /**
     * get color int
     *
     * @param context
     * @param id
     * @return
     */
    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    /**
     * check network status.
     *
     * @return network status
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * generate unique transaction id
     */

    public static int generateTransactionId(){
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }


   /* //Below android marshmallow
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }*/
    /**
     * get mac address
     * @param context
     * @return
     */

   /* @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String getMACAddress(@NotNull final String interfaceName) {
        String macaddress = "";
        try {
            final List&lt;NetworkInterface&gt; interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (final NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                final byte[] mac = intf.getHardwareAddress();
                if (mac==null) continue;
                final StringBuilder buf = new StringBuilder();
                for (int idx=0; idx &lt;= mac.length; idx++)
                buf.append(String.format("%02X:", mac[idx]));
                if (buf.length()&gt;=0) buf.deleteCharAt(buf.length()-1);
                macaddress = buf.toString();
                if(!macaddress.equals("")) break;
            }
        } catch (final Exception ex) { } // for now eat exceptions
        return macaddress ;
    }
*/

   //Getting Mac Address
    public static String getMacAddress(Context context) {
      /*  WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();*/
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

    public static String getIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager)context.getApplicationContext(). getSystemService(Context.WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        return ipAddress;
    }

    public static void showSuccessDialog(final Context activity)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_success);
        // Set dialog title
        layout.setTitle("Success");

        Button yes = (Button) layout.findViewById(R.id.yes);
        layout.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });


    }

    public static String getCurrentDateAndTime(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c.getTime());
    }

}
