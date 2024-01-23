package com.github.warren_bank.https_url_connection_tester;

import android.app.Application;

import javax.net.ssl.HttpsURLConnection;

public class AppBase extends Application {

  protected void upgradeSSLSocketFactory(boolean upgradeProtocols, boolean upgradeCipherSuites) {
    try {
      SSLSocketFactoryCompat socketFactory = new SSLSocketFactoryCompat(upgradeProtocols, upgradeCipherSuites);

      HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
    }
    catch(Exception e) {}
  }

}
