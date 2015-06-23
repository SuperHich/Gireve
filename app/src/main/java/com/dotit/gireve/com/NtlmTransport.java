/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dotit.gireve.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.util.Base64;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.NTLMEngine;
import org.apache.http.impl.auth.NTLMEngineException;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.KeepAliveHttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 *
 */
public class NtlmTransport extends KeepAliveHttpsTransportSE {

    private static final String TAG = NtlmTransport.class.getSimpleName();
    private boolean isForCheck = false;
	private boolean isAlert = false;
	private String errorMsg;
	private boolean shouldBack = false;
	private boolean ignoreBack = false;

	public NtlmTransport(String host, int port, String file, int timeout, boolean isForCheck, boolean alert, SSLSocketFactory sslSocketFactory) {
		super(host, port, file, timeout);
		this.isForCheck = isForCheck;
		isAlert = alert;
		//		client = new DefaultHttpClient(getHttpParams(timeout));
        this.sslSocketFactory = sslSocketFactory;
		client = getThreadSafeClient(getHttpParams(timeout), sslSocketFactory);
	}

    public NtlmTransport(String host, int port, String file, SSLSocketFactory sslSocketFactory) {
        super(host, port, file, 45000);
//        this.isForCheck = isForCheck;
//        isAlert = alert;
        //		client = new DefaultHttpClient(getHttpParams(timeout));
        this.sslSocketFactory = sslSocketFactory;
        client = getThreadSafeClient(getHttpParams(timeout), sslSocketFactory);
    }

	static final String ENCODING = "utf-8";

	private final DefaultHttpClient client;
	private final HttpContext localContext = new BasicHttpContext();
	private String urlString;
	private String user;
	private String password;
	private String ntDomain;
	private String ntWorkstation;
    private SSLSocketFactory sslSocketFactory;

	public void setCredentials(String url, String user, String password, String domain, String workStation) {

		this.urlString = url;
		this.user = user;
		this.password = password;
		this.ntDomain = domain;
		this.ntWorkstation = workStation;
	}

	public void setIgnoreBack(boolean ignoreBack){
		this.ignoreBack = ignoreBack;
	}

	public void shutDownRequest(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try{
					isAlert = false;
					client.getConnectionManager().shutdown();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	private String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		//is.close();
		return sb.toString();
	} 

	public static String getResultString(InputStream is){
		String res = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			//	            is.close();
			res = sb.toString();
			Log.e("JSON", "getResultString " + res);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	private String showResultOnly(String soapAction, SoapEnvelope envelope, List headers){

		try {
			HttpPost httppost = new HttpPost(urlString);
			setHeaders(soapAction, envelope, httppost, headers);
			HttpResponse resp = client.execute(httppost, localContext);
			HttpEntity respEentity = resp.getEntity();
			InputStream is = respEentity.getContent();

//			SAXParserFactory fact = SAXParserFactory.newInstance();
//            SAXParser pars = fact.newSAXParser();
//            SoapErrorHandler handler = new SoapErrorHandler();
//            pars.parse(is, handler);
//
//			String result = handler.getResult();
            String result = getResultString(is);

			if(result == null)
				result = resp.getStatusLine().getReasonPhrase();

			return result;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	@Override
	protected void parseResponse(SoapEnvelope envelope, InputStream is)
			throws XmlPullParserException, IOException {

		super.parseResponse(envelope, is);
	}

	@Override
	public List call(String soapAction, SoapEnvelope envelope, List headers)
			throws IOException, XmlPullParserException {

		HttpResponse resp = null;
		InputStream is = null;

		if(setupNtlm(urlString, user, password)) {

            try {
                // URL url = new URL(urlString);
                HttpPost httppost = new HttpPost(urlString);
                setHeaders(soapAction, envelope, httppost, headers);
                Log.e("", "HTTP Entiry : " + convertStreamToString(httppost.getEntity().getContent()));
                resp = client.execute(httppost, localContext);
                System.out.println("Response Code : "
                        + resp.getStatusLine().getStatusCode());
                System.out.println("Reason Phrase : "
                        + resp.getStatusLine().getReasonPhrase());

                if (isForCheck) {
                    int code = resp.getStatusLine().getStatusCode();
                    ArrayList<String> result = new ArrayList<>();
                    //result.add(resp.getStatusLine().getStatusCode());
                    result.add(String.valueOf(code));
                    return result;
                } else {
                    HttpEntity respEentity = resp.getEntity();
                    is = respEentity.getContent();
                    parseResponse(envelope, is);

//                showResultOnly(soapAction, envelope, headers);
                }
            } catch (Exception ex) {
                Log.e("**********", "Failed **************to parse soap message: " + ex.getMessage());

//			errorMsg = showResultOnly(soapAction, envelope, headers);
//			if(errorMsg == null)
//				errorMsg = ApplicationHelper.getInstance().getString(R.string.connection_problem);
//			else
//				shouldBack = true;
//			if(isAlert)
//				((FragmentActivity) ApplicationHelper.getInstance()).runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if(shouldBack && !ignoreBack)
//							ViewHelper.displayMsgInDialogWithBack(errorMsg, (FragmentActivity) ApplicationHelper.getInstance());
//						else
//							ViewHelper.displayMsgInDialog(errorMsg, ApplicationHelper.getInstance());
//					}
//				});

                ex.printStackTrace();
                return null;
            }

            if (resp != null) {
                return Arrays.asList(resp.getAllHeaders());
            }
        }

		return null;
	}

	private void setHeaders(String soapAction, SoapEnvelope envelope, HttpPost httppost, List headers) {
		byte[] requestData = null;
		try {
			requestData = createRequestData(envelope);
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}

		httppost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		//httppost.getParams().setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, org.ksoap2.transport.Transport.CONTENT_TYPE_XML_CHARSET_UTF_8);
		//httppost.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, org.ksoap2.transport.Transport.CONTENT_TYPE_XML_CHARSET_UTF_8);

		ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestData);
		byteArrayEntity.setContentType("text/xml");
		byteArrayEntity.setContentEncoding(org.ksoap2.transport.Transport.CONTENT_TYPE_XML_CHARSET_UTF_8);
		//		httppost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
		httppost.setEntity(byteArrayEntity);

		httppost.addHeader("USER-AGENT", org.ksoap2.transport.Transport.CONTENT_TYPE_XML_CHARSET_UTF_8);
		// SOAPAction is not a valid header for VER12 so do not add
		// it
		// @see "http://code.google.com/p/ksoap2-android/issues/detail?id=67
		//        if (envelope.version != SoapSerializationEnvelope.VER12) {
		//            httppost.addHeader("SOAPAction", soapAction);
		//        }

		if (envelope.version == SoapSerializationEnvelope.VER12) {
			httppost.addHeader("Content-Type", org.ksoap2.transport.Transport.CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8);
			httppost.setHeader("Accept", org.ksoap2.transport.Transport.CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8+";odata=verbose");
		} else {
			httppost.addHeader("Content-Type", org.ksoap2.transport.Transport.CONTENT_TYPE_XML_CHARSET_UTF_8);
			httppost.setHeader("Accept", org.ksoap2.transport.Transport.CONTENT_TYPE_XML_CHARSET_UTF_8+";odata=verbose");
		}

		// httppost.addHeader("Connection", "close");
		// httppost.addHeader("Accept-Encoding", "gzip");
		// httppost.addHeader("Content-Length", "" + requestData.length);
		// For best performance, call setFixedLengthStreamingMode(int) when the body length is known in advance
		// @see "http://developer.android.com/reference/java/net/HttpURLConnection.html" Posting Content
		// httppost.setFixedLengthStreamingMode(requestData.length);

		// Pass the headers provided by the user along with the call
		if (headers != null) {
			for (int i = 0; i < headers.size(); i++) {
				HeaderProperty hp = (HeaderProperty) headers.get(i);
				Log.i("", "*********: "+hp.getKey());
				Log.e("", "+++++++++"+hp.getValue());

				httppost.addHeader(hp.getKey().trim(), hp.getValue());
				//httppost.addHeader("Cookie", hp.getValue());
			}
		}

	}

	// Try to execute a cheap method first. This will trigger NTLM authentication
	public boolean setupNtlm(String dummyUrl, String userId, String password) {
		try {

			((AbstractHttpClient) client).getAuthSchemes().register("ntlm", new NTLMSchemeFactory());

//			NTCredentials creds = new NTCredentials(userId, password, ntWorkstation, ntDomain);
//			client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);

			HttpGet httpget = new HttpGet(dummyUrl);

//			httpget.addHeader("Persistent-Auth", "true");
			httpget.addHeader("Connection", "Keep-Alive");
			HttpResponse response1 = client.execute(httpget, localContext);
			HttpEntity entity1 = response1.getEntity();
			Log.d("**********","status Line " + response1.getStatusLine());

			Header[] hArray = response1.getAllHeaders();
			for (Header h: hArray ) {
//				Log.d(TAG, "Setup " + h.getName() + ":" + h.getValue());
				if (h.getName().equals("WWW-Authenticate")) {
					entity1.consumeContent();
					throw new Exception("Failed Authentication");
				}
			}

			// EntityUtils.getContentCharSet(entity1);
			//Log.d("**********","Got credentials from " + dummyUrl + " len " + entity1.getContentLength());

			//            InputStream is = entity1.getContent();
			//            Log.d(MainActivity.TAG,"[" + getByteOutputStream(is) + "]");

			//Log.i("", "entity........."+convertStreamToString(entity1.getContent().));
			entity1.consumeContent();
            return true;
		} catch (Exception ex) {
			Log.e("**********","Fail to get credentials from " + dummyUrl + ":" + ex.getMessage());
            return false;
		}

	}

	//NTLM Scheme factory
	public class NTLMSchemeFactory implements AuthSchemeFactory {

		public AuthScheme newInstance(final HttpParams params) {
			//  see http://www.robertkuzma.com/2011/07/manipulating-sharepoint-list-items-with-android-java-and-ntlm-authentication/
			return new NTLMScheme(new JCIFSEngine());
		}
	}

	@Override
	public String getHost() {

		String retVal = null;

		try {
			retVal = new URL(url).getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	@Override
	public int getPort() {

		int retVal = -1;

		try {
			retVal = new URL(url).getPort();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	@Override
	public String getPath() {

		String retVal = null;

		try {
			retVal = new URL(url).getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	// JCIFSEngine 
	public static class JCIFSEngine implements NTLMEngine {

		public String generateType1Msg(
				String domain, 
				String workstation) throws NTLMEngineException {

			Type1Message t1m = new Type1Message(
					Type1Message.getDefaultFlags(),
					domain,
					workstation);
			return Base64.encode(t1m.toByteArray());
		}

		public String generateType3Msg(
				String username, 
				String password, 
				String domain,
				String workstation, 
				String challenge) throws NTLMEngineException {
			Type2Message t2m;
			try {
				t2m = new Type2Message(Base64.decode(challenge));
			} catch (IOException ex) {
				throw new NTLMEngineException("Invalid Type2 message", ex);
			}
			Type3Message t3m = new Type3Message(
					t2m, 
					password, 
					domain, 
					username, 
					workstation, 0);
			return Base64.encode(t3m.toByteArray());
		}

	}

	public static DefaultHttpClient getThreadSafeClient(HttpParams params, SSLSocketFactory sslSocketFactory)  {

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, schemeRegistry);

		DefaultHttpClient client = new DefaultHttpClient(ccm, params);
//		ClientConnectionManager mgr = client.getConnectionManager();
//		//	    HttpParams params = client.getParams();
//		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
//				mgr.getSchemeRegistry()), params);
		return client;
	}

	public static HttpParams getHttpParams(int timeout){
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		//    	int timeoutConnection = timeout/2;
		int timeoutConnection = 30000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = timeout;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		return httpParameters;
	}


}
