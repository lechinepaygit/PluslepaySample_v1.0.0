package com.lepay.pluslepaydemo.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class HttpsUtils {
	/** 可复制 */
	private static OkHttpClient sClient;

	/**
	 * 初始化HTTPS,添加信任证书
	 */
	public static void init(Context context) {
		if (sClient != null) return;
		try{
			InputStream is = null;
			try {
				// 证书认证 ------------------------------------------------------------------------
//				is = context.getAssets().open("testclient.cer"); // 得到证书的输入流
//				X509TrustManager trustManager = trustManagerForCertificates(is);//以流的方式读入证书
//				SSLContext sslContext = SSLContext.getInstance("TLS");
//				sslContext.init(null, new TrustManager[]{trustManager}, null);
//				SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

				// 无证书认证 ----------------------------------------------------------------------
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, new TrustManager[]{getX509TrustManager()}, new SecureRandom());
				SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
				HostnameVerifier noVerifier = new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				// ------------------------------------------------------------------------------------

				sClient = new OkHttpClient.Builder()
						.sslSocketFactory(sslSocketFactory)
						.hostnameVerifier(noVerifier)
						.build();
			} finally {
				if (is != null) is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static X509TrustManager getX509TrustManager() {
		return new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[]{};
			}
		};
	}

	public static OkHttpClient getInstance() {
		if (sClient == null) throw new IllegalStateException("HttpsUtils 未初始化");
		return sClient;
	}

	/**
	 * 以流的方式添加信任证书
	 */
	/**
	 * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
	 * certificates have not been signed by these certificates will fail with a {@code
	 * SSLHandshakeException}.
	 * <p/>
	 * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
	 * set. This is useful in development where certificate authority-trusted certificates aren't
	 * available. Or in production, to avoid reliance on third-party certificate authorities.
	 * <p/>
	 * <p/>
	 * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
	 * <p/>
	 * <p>Relying on your own trusted certificates limits your server team's ability to update their
	 * TLS certificates. By installing a specific set of trusted certificates, you take on additional
	 * operational complexity and limit your ability to migrate between certificate authorities. Do
	 * not use custom trusted certificates in production without the blessing of your server's TLS
	 * administrator.
	 */
	private static X509TrustManager trustManagerForCertificates(InputStream in)
			throws GeneralSecurityException {
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
		if (certificates.isEmpty()) {
			throw new IllegalArgumentException("expected non-empty set of trusted certificates");
		}

		// Put the certificates a key store.
		char[] password = "password".toCharArray(); // Any password will work.
		KeyStore keyStore = newEmptyKeyStore(password);
		int index = 0;
		for (Certificate certificate : certificates) {
			String certificateAlias = Integer.toString(index++);
			keyStore.setCertificateEntry(certificateAlias, certificate);
		}

		// Use it to build an X509 trust manager.
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, password);
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
		if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
			throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
		}
		return (X509TrustManager) trustManagers[0];
	}


	/**
	 * 添加password
	 *
	 * @param password
	 * @return
	 * @throws GeneralSecurityException
	 */
	private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
			InputStream in = null; // By convention, 'null' creates an empty key store.
			keyStore.load(in, password);
			return keyStore;
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}
}
