package com.riyagayasen.easyaccordion;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by riyagayasen on 08/10/16.
 */


public class AccordionView extends RelativeLayout {

    View[] children;

    Boolean isExpanded = false;

    Boolean isAnimated = false;

    Boolean isPartitioned = false;

    String headingString;

    View partition;

    TextView heading;

    RelativeLayout paragraph;

    int headingTextSize;

    ImageView dropdownImage;

    ImageView dropupImage;

    private LayoutInflater inflater;

    LinearLayout headingLayout;

    int paragraphTopMargin;

    int paragraphBottomMargin;

   // int paragraphHeight;

    int headingBackgroundColor = Color.WHITE;

    int paragraphBackgroundColor = Color.WHITE;

    Drawable headingBackground;

    Drawable paragraphBackground;

    AccordionExpansionCollapseListener listener;

    /***
     * Constructor taking only the context. This is useful in case
     * the developer wants to programatically create an accordion view.
     *
     * @param context
     */
    public AccordionView(Context context) {
        super(context);
        prepareLayoutWithoutChildren(context);
    }

    /***
     * The constructor taking an attribute set. This is called by the android OS itself,
     * in case this accordion component was included in the layout XML itself.
     * @param context
     * @param attrs
     */
    public AccordionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributeSet(context, attrs);
    }

    /***
     * Same as the constructor AccordionView(Context context, AttributeSet attrs)
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public AccordionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributeSet(context, attrs);
    }

    /***
     * A function that takes the various attributes defined for this accordion. This accordion extends
     * a relative layout. There are certain custom attributes that I have defined for this accordion whose values need
     * to be retrieved.
     * @param context
     * @param attts
     */
    private void handleAttributeSet(Context context, AttributeSet attts) {
        TypedArray a = context.obtainStyledAttributes(attts, R.styleable.accordion);
        isAnimated = a.getBoolean(R.styleable.accordion_isAnimated, false);
        isPartitioned = a.getBoolean(R.styleable.accordion_isPartitioned, false);
        headingString = a.getString(R.styleable.accordion_heading);
        isExpanded = a.getBoolean(R.styleable.accordion_isExpanded, false);
        headingTextSize = a.getDimensionPixelSize(R.styleable.accordion_headingTextSize, 20);
        if (WidgetHelper.isNullOrBlank(headingString))
            throw new IllegalStateException("Please specify a heading for the accordion");

        headingBackgroundColor = a.getColor(R.styleable.accordion_headingBackgroundColor,0);
        paragraphBackgroundColor = a.getColor(R.styleable.accordion_bodyBackgroundColor,0);

        headingBackground = a.getDrawable(R.styleable.accordion_headingBackground);
        paragraphBackground = a.getDrawable(R.styleable.accordion_bodyBackground);


    }

    /***
     * This creates an accordion layout. This is called when the user programatically creates an accordion. 'Without Children' signifies that no UI elements
     * have been added to the body of the accordion yet.
     * @param context
     */
    private void initializeViewWithoutChildren(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout accordionLayout = (LinearLayout) inflater.inflate(R.layout.accordion, null);
        partition = accordionLayout.findViewById(R.id.partition);
        heading = (TextView) accordionLayout.findViewById(R.id.heading);
        paragraph = (RelativeLayout) accordionLayout.findViewById(R.id.paragraph_layout);
        dropdownImage = (ImageView) accordionLayout.findViewById(R.id.dropdown_image);
        dropupImage = (ImageView) accordionLayout.findViewById(R.id.dropup_image);
        headingLayout = (LinearLayout) accordionLayout.findViewById(R.id.heading_layout);
        paragraph.removeAllViews();
        removeAllViews();
        paragraphBottomMargin = ((LinearLayout.LayoutParams) paragraph.getLayoutParams()).bottomMargin;
        paragraphTopMargin = ((LinearLayout.LayoutParams) paragraph.getLayoutParams()).topMargin;
        addView(accordionLayout);

    }


    /***
     * This function is called when the accordion is added in the XML itself and is used to initialize the various components
     * of the accordion
     * @param context
     */
    private void initializeViews(Context context) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout accordionLayout = (LinearLayout) inflater.inflate(R.layout.accordion, null);
        partition = accordionLayout.findViewById(R.id.partition);
        heading = (TextView) accordionLayout.findViewById(R.id.heading);
        paragraph = (RelativeLayout) accordionLayout.findViewById(R.id.paragraph_layout);
        dropdownImage = (ImageView) accordionLayout.findViewById(R.id.dropdown_image);
        dropupImage = (ImageView) accordionLayout.findViewById(R.id.dropup_image);
        headingLayout = (LinearLayout) accordionLayout.findViewById(R.id.heading_layout);
        paragraph.removeAllViews();

        int i;
        children = new View[getChildCount()];
        for (i = 0; i < getChildCount(); i++) {
            children[i] = getChildAt(i);
        }
        removeAllViews();
        for (i = 0; i < children.length; i++) {
            paragraph.addView(children[i]);
        }


        paragraphBottomMargin = ((LinearLayout.LayoutParams) paragraph.getLayoutParams()).bottomMargin;
        paragraphTopMargin = ((LinearLayout.LayoutParams) paragraph.getLayoutParams()).topMargin;

        addView(accordionLayout);


    }

    /***
     * This function; after initializing the accordion, performs necessary UI operations like setting the partition or adding animation or
     * expanding or collapsing the accordion
     * @param context
     */
    private void prepareLayout(Context context) {
        initializeViews(context);
        partition.setVisibility(isPartitioned ? VISIBLE : INVISIBLE);
        heading.setText(headingString);
        heading.setTextSize(headingTextSize);

        //Set the background on the heading...if the background drawable has been set by the user, use that. Else, set the background color
        if(!WidgetHelper.isNullOrBlank(headingBackground) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            headingLayout.setBackground(headingBackground);
        else if(!WidgetHelper.isNullOrBlank(headingBackgroundColor))
            headingLayout.setBackgroundColor(headingBackgroundColor);

        //Set the background on the paragraph...if the background drawable has been set by the user, use that. Else, set the background color
        if(!WidgetHelper.isNullOrBlank(paragraphBackground) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            paragraph.setBackground(paragraphBackground);
        else if(!WidgetHelper.isNullOrBlank(paragraphBackgroundColor))
            paragraph.setBackgroundColor(paragraphBackgroundColor);

        paragraph.setVisibility(VISIBLE);
        //paragraph.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        if (isAnimated) {
            headingLayout.setLayoutTransition(new LayoutTransition());
        } else {
            headingLayout.setLayoutTransition(null);

        }

        if (isExpanded)
            expand();
        else
            collapse();

        setOnClickListenerOnHeading();

    }

    /***
     * This function is used to prepare the layout after the initialize funciton but is called when the developer PROGRAMATICALLY adds
     * the accordion from the class. Hence, the accordion does not have the UI elements (children) yet
     * @param context
     */
    private void prepareLayoutWithoutChildren(Context context) {
        initializeViewWithoutChildren(context);
        partition.setVisibility(isPartitioned ? VISIBLE : INVISIBLE);
        heading.setText(headingString);
        paragraph.setVisibility(VISIBLE);
        //paragraph.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        if (isAnimated) {
            headingLayout.setLayoutTransition(new LayoutTransition());
        } else {
            headingLayout.setLayoutTransition(null);

        }

        if (isExpanded)
            expand();
        else
            collapse();

        setOnClickListenerOnHeading();

    }



    @Override
    protected void onFinishInflate() {
        prepareLayout(getContext());
        super.onFinishInflate();

    }

    /***
     * This function expands the accordion
     */
    private void expand() {
        if (isAnimated) {

            AccordionTransitionAnimation expandAnimation = new AccordionTransitionAnimation(paragraph, 300, AccordionTransitionAnimation.EXPAND);
            expandAnimation.setHeight(WidgetHelper.getFullHeight(paragraph));
            expandAnimation.setEndBottomMargin(paragraphBottomMargin);
            expandAnimation.setEndTopMargin(paragraphTopMargin);
            paragraph.startAnimation(expandAnimation);

        } else {
            paragraph.setVisibility(VISIBLE);
        }


        partition.setVisibility(isPartitioned ? VISIBLE : INVISIBLE);

        dropupImage.setVisibility(VISIBLE);
        dropdownImage.setVisibility(GONE);
        if(!WidgetHelper.isNullOrBlank(listener)) {
            listener.onExpanded(this);
        }


    }

    /***
     * This function collapses the accordion
     */
    private void collapse() {

        if (isAnimated) {

            AccordionTransitionAnimation collapseAnimation = new AccordionTransitionAnimation(paragraph, 300, AccordionTransitionAnimation.COLLAPSE);
            paragraph.startAnimation(collapseAnimation);

        } else {
            paragraph.setVisibility(GONE);

        }

        partition.setVisibility(INVISIBLE);

        dropupImage.setVisibility(GONE);
        dropdownImage.setVisibility(VISIBLE);

        if(!WidgetHelper.isNullOrBlank(listener)) {
            listener.onCollapsed(this);
        }


    }


    private void setOnClickListenerOnHeading() {
        heading.setOnClickListener(toggleParagraphVisiblity);
        dropdownImage.setOnClickListener(toggleParagraphVisiblity);
        dropupImage.setOnClickListener(toggleParagraphVisiblity);

    }

    OnClickListener toggleParagraphVisiblity = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (paragraph.getVisibility() == VISIBLE) {
                collapse();
            } else
                expand();
        }
    };


    /***
     * This function adds the view to the body or the 'paragraph'
     * @param child
     */
    public void addViewToBody(View child) {
        paragraph.addView(child);
    }

    /***
     * Set the heading of the accordion
     * @param headingString
     */
    public void setHeadingString(String headingString) {
        heading.setText(headingString);
    }


    public void setIsAnimated(Boolean isAnimated) {
        this.isAnimated = isAnimated;
    }

    /***
     * Get the status whether the accordion is going to animate itself on expansion or collapse
     * @return
     */
    public Boolean getAnimated() {
        return isAnimated;
    }

    /***
     * Set whether the accordion would play an animation when expanding/collapsing
     * @param animated
     */
    public void setAnimated(Boolean animated) {
        isAnimated = animated;


    }

    /***
     * Tell the accordion what to do; when expanded or collapsed.
     * @param listener
     */
    public void setOnExpandCollapseListener(AccordionExpansionCollapseListener listener) {
        this.listener = listener;
    }

    /***
     * This function returns the body of the accordion
     * @return
     */
    public RelativeLayout getBody() {
        return paragraph;
    }

    /***
     * This function returns the body of the accordion
     * @return
     */
    public RelativeLayout getParagraph() {
        return paragraph;
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    /***
     * Tell the accordion whether to expand or remain collapsed by default, when drawn
     * @param expanded
     */
    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    /***
     * The the status of the partition line
      * @return
     */
    public Boolean getPartitioned() {
        return isPartitioned;
    }

    /***
     * This function tells the accordion whether to make the partition visible or not
     * @param partitioned
     */
    public void setPartitioned(Boolean partitioned) {
        isPartitioned = partitioned;
        partition.setVisibility(isPartitioned ? VISIBLE : INVISIBLE);
    }

    /***
     * This function adds a background drawable to the heading. Works only for JellyBean and above
     * @param drawable
     */
    public void setHeadingBackGround(Drawable drawable) {

        if(WidgetHelper.isNullOrBlank(headingLayout))
            headingLayout = (LinearLayout) findViewById(R.id.heading_layout);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            headingLayout.setBackground(drawable);
        }
    }


    /***
     * This function adds a background drawable to the heading. Works only for JellyBean and above
     * @param resId
     */
    public void setHeadingBackGround(int resId) {
        Drawable drawable = getResources().getDrawable(resId);

        if(WidgetHelper.isNullOrBlank(headingLayout))
            headingLayout = (LinearLayout) findViewById(R.id.heading_layout);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            headingLayout.setBackground(drawable);
        }
    }

    /***
     * This function adds a background drawable to the paragraph. Works only for JellyBean and above
     * @param drawable
     */
    public void setBodyBackGround(Drawable drawable) {

        if(WidgetHelper.isNullOrBlank(paragraph))
            paragraph = (RelativeLayout) findViewById(R.id.paragraph_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            paragraph.setBackground(drawable);
        }
    }

    /***
     * This function adds a background drawable to the paragraph. Works only for JellyBean and above
     * @param resId
     */
    public void setBodyBackGround(int resId) {
        Drawable drawable = getResources().getDrawable(resId);

        if(WidgetHelper.isNullOrBlank(paragraph))
            paragraph = (RelativeLayout) findViewById(R.id.paragraph_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            paragraph.setBackground(drawable);
        }
    }
    /***
     * This function adds a background color to the heading.
     * @param color
     */
    public void setHeadingBackGroundColor(int color) {

        if(WidgetHelper.isNullOrBlank(headingLayout))
            headingLayout = (LinearLayout) findViewById(R.id.heading_layout);
            headingLayout.setBackgroundColor(color);

    }

    /***
     * This function adds a background color to the paragraph.
     * @param color
     */
    public void setBodyBackGroundColor(int color) {

        if(WidgetHelper.isNullOrBlank(paragraph))
            paragraph = (RelativeLayout) findViewById(R.id.paragraph_layout);
            paragraph.setBackgroundColor(color);

    }




}
