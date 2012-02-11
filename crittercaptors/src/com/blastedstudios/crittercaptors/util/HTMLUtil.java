package com.blastedstudios.crittercaptors.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTMLUtil {
	private static String getString(InputStream stream){
		String line, result = "";
		BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
		try {
			while ((line = rd.readLine()) != null) 
				result += line;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getHTML(String urlToRead) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(urlToRead).openConnection();
			conn.setRequestMethod("GET");
			return getString(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Apaches method, handles redirects etc.
	 * @param urlToRead 
	 * "http://gisdata.usgs.gov/"
	            + "xmlwebservices2/elevation_service.asmx/"
	            + "getElevation?X_Value=" + String.valueOf(longitude)
	            + "&Y_Value=" + String.valueOf(latitude)
	            + "&Elevation_Units=METERS&Source_Layer=-1&Elevation_Only=true"
	 * @return http content of urlToRead
	 */
	/*
	public static String getHTML(String urlToRead) {
		String result = "";
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    HttpGet httpGet = new HttpGet(urlToRead);
	    try {
	        HttpResponse response = httpClient.execute(httpGet, localContext);
	        HttpEntity entity = response.getEntity();
	        if (entity != null)
	            return getString(entity.getContent());
	    } catch (ClientProtocolException e) {} 
	    catch (IOException e) {}
	    return result;
	}*/
}
