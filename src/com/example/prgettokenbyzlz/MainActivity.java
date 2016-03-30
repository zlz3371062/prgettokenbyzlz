package com.example.prgettokenbyzlz;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("zlz", "1");
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							gettoken();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}

	private void gettoken() throws MalformedURLException, IOException {
		String nonce = String.valueOf(Math.random() * 1000000);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder toSign = new StringBuilder("aLckLWdduu2zUi").append(nonce).append(timestamp);
		String sign = SHA1(toSign.toString());
		HttpURLConnection conn = (HttpURLConnection) (new URL("https://api.cn.ronghub.com/user/getToken.json"))
				.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
//		conn.setInstanceFollowRedirects(true);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);

		conn.setRequestProperty("APPKEY", "n19jmcy59z3n9");
		conn.setRequestProperty("NONCE", nonce);
		conn.setRequestProperty("TIMESTAMP", timestamp);
		conn.setRequestProperty("SIGNATURE", sign);
//		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes("userId=1&name=zlz&portraitUri=http://aa.com/a.png");
		out.flush();
		out.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
			Log.d("zlz", sb.toString());

		}
	}

	public static String SHA1(String decript) {

		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
