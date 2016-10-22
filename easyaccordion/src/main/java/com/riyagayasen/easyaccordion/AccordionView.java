package com.riyagayasen.easyaccordion;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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

    /***
     * Constructor taking only the context. This is useful in case
     * the developer wants to programatically create an accordion view.
     *
     * @param context
     */
    public AccordionView(Context context) {
        super(context);
        initializeViewWithoutChildren(context);
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

    private void prepareLayout(Context context) {
        initializeViews(context);
        partition.setVisibility(isPartitioned ? VISIBLE : INVISIBLE);
        heading.setText(headingString);
        heading.setTextSize(headingTextSize);
        paragraph.setVisibility(VISIBLE);
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

    private void expand() {
        if (isAnimated) {
          /*  LinearLayout parent = (LinearLayout)paragraph.getParent();
            parent.setLayoutTransition(new LayoutTransition());
          */
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


    }

    private void collapse() {

        if (isAnimated) {
            /*LinearLayout parent = (LinearLayout)paragraph.getParent();
            parent.setLayoutTransition(new LayoutTransition());
*/
            AccordionTransitionAnimation collapseAnimation = new AccordionTransitionAnimation(paragraph, 300, AccordionTransitionAnimation.COLLAPSE);
            paragraph.startAnimation(collapseAnimation);

        } else {
            paragraph.setVisibility(GONE);

        }

        partition.setVisibility(INVISIBLE);

        dropupImage.setVisibility(GONE);
        dropdownImage.setVisibility(VISIBLE);


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


    protected void addViewToBody(View child) {
        paragraph.addView(child);
    }

    protected void setHeadingString(String headingString) {
        heading.setText(headingString);
    }

    protected void setIsAnimated(Boolean isAnimated) {
        this.isAnimated = isAnimated;
    }

    public Boolean getAnimated() {
        return isAnimated;
    }

    public void setAnimated(Boolean animated) {
        isAnimated = animated;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public Boolean getPartitioned() {
        return isPartitioned;
    }

    public void setPartitioned(Boolean partitioned) {
        isPartitioned = partitioned;
    }


}
