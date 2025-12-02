/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.utiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Service;

/**
 *
 * @author USER01
 */
@Service
public class WebServices {

    private URL url;
    private int Cnx;
//    private HttpURLConnection server;
    private HttpURLConnection server;

    private String reponse;
    private InputStream reponses;
    private String[] retour;
    private SSLContext ctx;

    public WebServices() {
    }

    public int connect(String method) throws Exception {
        try {
//            System.out.println("url "+url.toURI().toString());
//            ctx = SSLContext.getInstance("TLS");

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            server = (HttpURLConnection) url.openConnection();
//            server.setDoInput(true);
//            server.setDoOutput(true);
            server.setRequestMethod(method);

//            server.setRequestProperty("Content-type",
//                    "application/x-www-form-urlencoded");
            server.connect();

            return 1;
        } catch (IOException e) {
            //throw new Exception("Connection to the http server failed");
            System.err.println(e.toString());
            System.err.println("erreur de connection au service biometrie");

            return -1;
        }
    }

    public String displayResponse() throws Exception {
        String line;
        String b = "";

        try {
            BufferedReader s = new BufferedReader(
                    new InputStreamReader(
                            server.getInputStream()));
            String url = server.getURL().toString();
            b = s.readLine();

//            while (line != null) {
//                System.out.println(line);
//                b = line;
////                line = s.readLine();
//                
//
//            }
            //System.out.println(Integer.parseInt(line.substring(0, 1)));
            s.close();
            return b;

        } catch (Exception e) {
            //throw new Exception("Unable to read input stream");
            System.err.println(e.toString());
            System.err.println("Code " + e.getLocalizedMessage());
            System.err.println("Impossible de lire la reponse de la passerelle 1");

            return "NOK";

        }
    }

    public InputStream displayResponses() throws Exception {
        String line;
        String b = "";

        try {

//            String url = server.getURL().toString();
//            b = s.readLine();
//           
////            while (line != null) {
////                System.out.println(line);
////                b = line;
//////                line = s.readLine();
////                
////
////            }
//            //System.out.println(Integer.parseInt(line.substring(0, 1)));
//
//            s.close();
            return server.getInputStream();

        } catch (Exception e) {
            //throw new Exception("Unable to read input stream");
            System.err.println(e.toString());
            System.err.println("Impossible de lire la reponse de la passerelle 1");

            return null;
        }
    }

    public String urlBiometrie(String hostname) {

        try {

            url = new URL(hostname);
            //server3.setConnectTimeout(0);
            Cnx = this.connect("GET");
            if (Cnx == 1) {
                reponse = this.displayResponse();
                return reponse;
            } else {
                return "echec";
            }

        } catch (Exception ex) {

        }
        return reponse;
    }

    public InputStream urlBiometries(String hostname) {

        try {

            url = new URL(hostname);
//            url = new URL(null, hostname, new sun.net.www.protocol.https.Handler());
            //server3.setConnectTimeout(0);
            Cnx = this.connect("GET");
            if (Cnx == 1) {
                reponses = this.displayResponses();
                return reponses;
            } else {
                return null;
            }

        } catch (Exception ex) {

        }
        return reponses;
    }

}
