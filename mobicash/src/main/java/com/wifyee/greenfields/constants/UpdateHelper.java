package com.wifyee.greenfields.constants;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by amit on 3/28/2018.
 */

public class UpdateHelper {
    private static final String TAG = UpdateHelper.class.getSimpleName();
    public static final String KEY_UPDATE_REQUIRED = "is_updated";
    public static final String KEY_CURRENT_VERSION = "version";
    public static final String KEY_UPDATE_URL = "update_url";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public UpdateHelper(@NonNull Context context,
                        OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    /*public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        try {
            if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
                String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
                String appVersion = getAppVersion(context);
                String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);

                if (!TextUtils.equals(currentVersion, appVersion)
                        && onUpdateNeededListener != null) {
                    onUpdateNeededListener.onUpdateNeeded(updateUrl);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }*/

    private String getAppVersion(Context context) {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public UpdateHelper build() {
            return new UpdateHelper(context, onUpdateNeededListener);
        }

       /* public UpdateHelper check() {
            UpdateHelper forceUpdateChecker = build();
            forceUpdateChecker.check();
            return forceUpdateChecker;
        }*/
    }
}
