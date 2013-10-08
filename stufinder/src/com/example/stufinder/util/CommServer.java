package com.example.stufinder.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

public class CommServer {
	private static String serverUrl;
	public static String getServerUrl() {
		return serverUrl;
	}
	public static void setServerUrl(String serverUrl) {
		CommServer.serverUrl = serverUrl;
	}


	public List<NameValuePair> params;
	public void setParam(String query, String value){
		params.add(new BasicNameValuePair(query, value));
	}

	public void insertImage(final Bitmap image){
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] ba = bao.toByteArray();
		String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
		setParam("image", ba1);
		setParam("ext", "jpg");
	}

	//constructor  
	public CommServer(){
		params = new ArrayList<NameValuePair>();
		if(getServerUrl() == null){
			setServerUrl("http://skullacytest.cafe24.com");
		}
	}

	public String getData() throws UnsupportedEncodingException
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputStreamFromUrl(serverUrl, params), "utf-8"));

		try{
			String line = null;

			while((line = br.readLine()) != null){
				sb.append(line);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		return sb.toString();
	}

	public JSONObject getJSONData(){
		JSONObject jsonobj = null;
		try {
			String content = getData();
			jsonobj = new JSONObject(content);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonobj;

	}

	private static InputStream getInputStreamFromUrl(String url, List<NameValuePair> params){
		InputStream contentStream = null;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);
			HttpResponse responsePost = httpclient.execute(post);

			contentStream = responsePost.getEntity().getContent();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return contentStream;
	}








}

