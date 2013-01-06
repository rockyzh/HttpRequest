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
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

		try {

			String url = args.getString(0);
			String method = args.getString(1);

			JSONObject params = args.getJSONObject(2);
			JSONObject options = args.getJSONObject(3);

			if (action.equals("execute")) {

				if (method.equals("post")) {
					this.setPostUrl(url);
				}
				else if (method.equals("get")) {
					this.setGetUrl(url, params);
				}

				if (options.getBoolean("acceptGzip") == true) {
					this.acceptGzip();
				}
				if (params.length() > 0 && method.equals("post")) {
					this.setPostParams(params);
				}
				if (options.getBoolean("trustAll") == true) {
					this.acceptAll();
				}

				int code = this.getCode();
				String body = this.getBody();
				String msg = this.getMessage();

				JSONObject response = new JSONObject();
				response.put("code", code);
				response.put("body", body);
				response.put("message", msg);

				Log.d(TAG, "Response from " + url + " " + code);

				if (code == 200) {
					callbackContext.success(response);
					return true;
				}
				else {
					callbackContext.error(response);
					return false;
				}

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

		return false;
	}

	private void setPostUrl(String url) throws HttpRequestException {
		request = HttpRequest.post(url);
		return;
	}

	private void setGetUrl(String url, JSONObject params) throws HttpRequestException, JSONException {

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

		request = HttpRequest.get(url,inputParams,true);
		return;
	}

	private void setPostParams(JSONObject params) throws JSONException, HttpRequestException {

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

	private void acceptAll() throws HttpRequestException {
		// Accept all certificates
		request.trustAllCerts();
		// Accept all hostnames
		request.trustAllHosts();
		return;
	}

	private void acceptGzip() {
		// Tell server to gzip response and automatically uncompress
		request.acceptGzipEncoding().uncompress(true);
		return;
	}

	private int getCode() throws HttpRequestException {
		return request.code();
	}

	private String getMessage() throws HttpRequestException {
		return request.message();
	}

	private String getBody() throws HttpRequestException {
		return request.body();
	}

}