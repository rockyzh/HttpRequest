package com.phonegap.plugins.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class HttpRequestPlugin extends CordovaPlugin {

	private final String TAG = "HTTP";

	private HttpRequest request;

	@Override
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) {

		if (action.equals("execute")) {

			cordova.getThreadPool().execute(new Runnable() {

				public void run() {
					try {

						String url = args.getString(0);
						String method = args.getString(1);

						JSONObject params = args.getJSONObject(2);
						JSONObject options = args.getJSONObject(3);

						if (method.equals("post")) {
							setPostUrl(url);
						}
						else if (method.equals("get")) {
							setGetUrl(url, params);
						}

						if (options.getBoolean("acceptGzip") == true) {
							acceptGzip();
						}
						if (params.length() > 0 && method.equals("post")) {
							setPostParams(params);
						}
						if (options.getBoolean("trustAll") == true) {
							acceptAll();
						}

						int code = getCode();
						String body = getBody();
						String msg = getMessage();
						disconnect();

						JSONObject response = new JSONObject();
						
						response.put("code", code);
						response.put("body", body);
						response.put("message", msg);
						

//						Log.d(TAG, "Response from " + url + " " + code);
//						Log.d(TAG, "Response body " + url + " " + body);

						if (code == 200) {
							callbackContext.success(response);
							return;
						}
						else {
							callbackContext.error(response);
							return;
						}

					}
					catch (JSONException ex) {
						Log.e(TAG, ex.getMessage());
					}
					catch (HttpRequestException ex) {
						Log.e(TAG, ex.getMessage());
					}
					catch (Exception ex) {
						Log.e(TAG, ex.getMessage());
					}

				}
			});
			return true;
		}
		return false;

	}

	public void disconnect(){
		request.disconnect();
	}
	public void setPostUrl(String url) throws HttpRequestException {
		request = HttpRequest.post(url);
		return;
	}

	public void setGetUrl(String url, JSONObject params) throws HttpRequestException, JSONException {

		Map<String, String> inputParams = new HashMap<String, String>();

		if (params.length() > 0) {

			@SuppressWarnings("unchecked")
			Iterator<String> keys = params.keys();

			while (keys.hasNext()) {

				// get the key and corresponding value
				String keyName = (String) keys.next();
				String keyValue = params.getString(keyName);

				inputParams.put(keyName, keyValue);

			}
		}

		request = HttpRequest.get(url, inputParams, true);
		return;
	}

	public void setPostParams(JSONObject params) throws JSONException, HttpRequestException {

		Map<String, String> inputParams = new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		Iterator<String> keys = params.keys();

		while (keys.hasNext()) {

			// get the key and corresponding value
			String keyName = (String) keys.next();
			String keyValue = params.getString(keyName);

			inputParams.put(keyName, keyValue);

		}
		request.form(inputParams);
		return;
	}

	public void acceptAll() throws HttpRequestException {
		// Accept all certificates
		request.trustAllCerts();
		// Accept all hostnames
		request.trustAllHosts();
		return;
	}

	public void acceptGzip() {
		// Tell server to gzip response and automatically uncompress
		request.acceptGzipEncoding().uncompress(true);
		return;
	}

	public int getCode() throws HttpRequestException {
		return request.code();
	}

	public String getMessage() throws HttpRequestException {
		return request.message();
	}

	public String getBody() throws HttpRequestException {
		return request.body();
	}

}