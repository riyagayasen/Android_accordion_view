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

    /***
     * Function to check if an object is null or blank
     * @param object
     * @return
     */
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

    /***
     * Function to check if an integer is zero or non zero
     * @param object
     * @return
     */
    public static boolean isNullOrBlank(int object) {
        if (object == 0)
            return true;
        else
            return false;


    }

    /***
     * This function returns the actual height the layout. The getHeight() function returns the current height which might be zero if
     * the layout's visibility is GONE
     * @param layout
     * @return
     */
    public static int getFullHeight(ViewGroup layout) {
        layout.measure(0,0);
        int totalHeight = layout.getMeasuredHeight();
        return totalHeight;
    }



}
