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

	// private HttpRequest request;

	@Override
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) {

		if (action.equals("execute")) {

			cordova.getThreadPool().execute(new Runnable() {

				public void run() {
					try {

						HttpRequest request;

						String url = args.getString(0);
						String method = args.getString(1);

						JSONObject params = args.getJSONObject(2);
						JSONObject options = args.getJSONObject(3);

						// optional params
						boolean trust = options.getBoolean("trustAll");
						

						// iterate over the supplied params, depending on
						// request method params are either directly attached to
						// url(GET) or as a form(POST)

						Map<String, String> inputParams = new HashMap<String, String>();

						@SuppressWarnings("unchecked")
						Iterator<String> keys = params.keys();

						while (keys.hasNext()) {

							// get the key and corresponding value
							String keyName = (String) keys.next();
							String keyValue = params.getString(keyName);

							inputParams.put(keyName, keyValue);

						}

						if (method.equalsIgnoreCase("post")) {
							request = HttpRequest.post(url);
							request.form(inputParams);
						}
						else {
							request = HttpRequest.get(url, inputParams, true);
						}

						if (trust == true) {
							request.trustAllCerts();
							request.trustAllHosts();
						}

						int code = request.code();
						String body = request.body();
						String msg = request.message();

						JSONObject response = new JSONObject();

						response.put("code", code);
						response.put("body", body);
						response.put("message", msg);

//						Log.d(TAG, "Response code " + url + " " + code);
//					    Log.d(TAG, "Response body " + url + " " + body);

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
						Log.e(TAG, ex.getStackTrace().toString());
						Log.e(TAG, ex.getMessage());
					}

				}
			});
			return true;
		}
		return false;

	}

}