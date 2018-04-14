package de.cn.chartserver.ssl;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSLSetup {
	public static final String KEYSTORE_FACTORY_TYPE = "SunX509";
	public static final String KEYSTORE_TYPE = "JKS";
	public static final String SSLCONTEXT_INSTANCE_TYPE="TLS";

	/**
	 * @param inputStreamOfKeyStoreFile
	 * @param keyStoreFilePassword
	 * @return
	 * @throws Exception
	 */
	public static SSLContext createSSLContext(InputStream inputStreamOfKeyStoreFile, String keyStoreFilePassword) throws Exception {

		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
		ks.load(inputStreamOfKeyStoreFile, keyStoreFilePassword.toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KEYSTORE_FACTORY_TYPE);
		kmf.init(ks, keyStoreFilePassword.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(KEYSTORE_FACTORY_TYPE);
		tmf.init(ks);

		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance(SSLCONTEXT_INSTANCE_TYPE);
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return sslContext;
	}
}
