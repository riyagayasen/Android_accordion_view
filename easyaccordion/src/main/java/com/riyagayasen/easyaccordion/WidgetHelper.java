package com.riyagayasen.easyaccordion;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Collection;

/**
 * Created by riyagayasen on 09/10/16.
 */

public class WidgetHelper {
    /***
     * A function to check if a string is null or blank
     *
     * @param string
     * @return
     */
    public static boolean isNullOrBlank(String string) {
        if (string == null)
            return true;
        if (string.equals(""))
            return true;
        if (string.length() == 0)
            return true;
        return false;
    }

    public static boolean isNullOrBlank(Object object) {
        if (object instanceof String)
            return isNullOrBlank((String) object);

        if (object == null)
            return true;
        if (object instanceof Collection) {
            if (((Collection) object).isEmpty())
                return true;

        }

        return false;
    }

    public static boolean isNullOrBlank(int object) {
        if (object == 0)
            return true;
        else
            return false;


    }

    public static int getFullHeight(ViewGroup layout) {
        layout.measure(0,0);
        int totalHeight = layout.getMeasuredHeight();
        return totalHeight;
    }



}
