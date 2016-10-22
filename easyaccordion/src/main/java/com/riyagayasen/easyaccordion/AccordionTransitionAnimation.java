package com.riyagayasen.easyaccordion;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by riyagayasen on 16/10/16.
 */

public class AccordionTransitionAnimation extends Animation{
    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View view;
    private int endHeight;
    private int endBottomMargin;
    private int endTopMargin;
    private int type;
    private LinearLayout.LayoutParams layoutParams;

    public AccordionTransitionAnimation(View view, int duration, int type) {

        setDuration(duration);
        this.view = view;
        endHeight = this.view.getMeasuredHeight();

        layoutParams = ((LinearLayout.LayoutParams) view.getLayoutParams());
        endBottomMargin = layoutParams.bottomMargin;
        endTopMargin = layoutParams.topMargin;
        this.type = type;
        if(this.type == EXPAND) {
            layoutParams.height = 0;
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = 0;
        } else {
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        view.setVisibility(View.VISIBLE);
    }

    public int getHeight(){
        return view.getHeight();
    }

    public void setHeight(int height){
        endHeight = height;
    }

    public int getEndTopMargin() {
        return endTopMargin;
    }

    public void setEndTopMargin(int endTopMargin) {
        this.endTopMargin = endTopMargin;
    }

    public int getEndBottomMargin() {
        return endBottomMargin;
    }

    public void setEndBottomMargin(int endBottomMargin) {
        this.endBottomMargin = endBottomMargin;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if(type == EXPAND) {
                layoutParams.height =  (int)(endHeight * interpolatedTime);
                layoutParams.topMargin = (int)(endTopMargin * interpolatedTime);
                layoutParams.bottomMargin = (int)(endBottomMargin * interpolatedTime);
                view.invalidate();
            } else {
                layoutParams.height = (int) (endHeight * (1 - interpolatedTime));
                layoutParams.topMargin = (int) (endTopMargin * (1 - interpolatedTime));
                layoutParams.bottomMargin = (int) (endBottomMargin * (1 - interpolatedTime));
            }

            view.requestLayout();
        } else {
            if(type == EXPAND) {
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                view.requestLayout();
            }else{
                view.setVisibility(View.GONE);
            }
        }
    }


}
