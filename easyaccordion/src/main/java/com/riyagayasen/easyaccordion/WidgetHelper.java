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
        int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
        int specHeight = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);


        layout.measure(specWidth,specHeight);
        int totalHeight = 0;//layout.getMeasuredHeight();
        int initialVisibility = layout.getVisibility();
        layout.setVisibility(View.VISIBLE);
        int numberOfChildren = layout.getChildCount();
        for(int i = 0;i<numberOfChildren;i++) {
            View child = layout.getChildAt(i);
            if(child instanceof ViewGroup) {
                totalHeight+=getFullHeight((ViewGroup)child);
            }else {
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(layout.getWidth(),
                        View.MeasureSpec.AT_MOST);
                child.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight+=child.getMeasuredHeight();
            }

        }
        layout.setVisibility(initialVisibility);
        return totalHeight;
    }



}
