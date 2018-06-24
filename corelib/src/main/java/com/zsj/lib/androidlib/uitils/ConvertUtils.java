package com.zsj.lib.androidlib.uitils;

import android.util.Log;

/**
 * Created by shengjun.zhu1 on 2017/9/25.
 */

public class ConvertUtils {
    private static final String TAG = "ConvertUtils";

    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                Log.e(TAG, "Exception:" + e1);
                return defaultValue;
            }
        }
    }
}
