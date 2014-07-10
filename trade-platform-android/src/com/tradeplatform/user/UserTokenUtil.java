package com.tradeplatform.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.trade_platform.core.R;

public class UserTokenUtil {
	public static String getUserToken(Activity actvityHandler) {
		SharedPreferences settings = actvityHandler.getSharedPreferences(
				actvityHandler.getString(R.string.shared_preferences),
				Context.MODE_PRIVATE);

		return settings.getString(
				actvityHandler.getString(R.string.user_token), null);
	}
}
