package com.vlxtrcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class VlxtrCoreApplication {

	public static void main(String[] args) {
		disableSslVerification();
		SpringApplication.run(VlxtrCoreApplication.class, args);
	}

	private static void disableSslVerification() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[]{
					new X509TrustManager() {
						public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
						public void checkClientTrusted(X509Certificate[] certs, String authType) { }
						public void checkServerTrusted(X509Certificate[] certs, String authType) { }
					}
			};
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
