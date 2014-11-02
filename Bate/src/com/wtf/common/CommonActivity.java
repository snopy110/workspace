package com.wtf.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;

import com.wtf.R;

public class CommonActivity extends Activity {
	
	
	public String getIpAddress() {
		try {
		            for (Enumeration<NetworkInterface> en = NetworkInterface
		                    .getNetworkInterfaces(); en.hasMoreElements();) {
		                NetworkInterface intf = en.nextElement();
		                for (Enumeration<InetAddress> enumIpAddr = intf
		                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
		                    InetAddress inetAddress = enumIpAddr.nextElement();
		                    if (!inetAddress.isLoopbackAddress()
		                            && inetAddress instanceof Inet4Address) {
		                        // if (!inetAddress.isLoopbackAddress() && inetAddress
		                        // instanceof Inet6Address) {
		                        return inetAddress.getHostAddress().toString();
		                    }
		                }
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		
		        return getString(R.string.ip_info_null);
		    }

}
