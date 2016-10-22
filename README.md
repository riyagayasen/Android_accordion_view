# Android_accordion_view
A very easy to use accordion component for android. 

I created this component because I wanted a component that could behave as an accordion, where in, the developer could set the heading and the UI elements in the body right from the layout xml or from the class. I also wanted to have various other UI features like animation to be controlled by the developer from the xml itself. 

To add the Accordion component,(Note: this project was built using Android Studio 2.2)
  1. Take the 'easyaccordion' folder and save it in your project's root directory (the directory containing the 'app' folder).  
  2. Add 
  
          'compile project(path: ':easyaccordion')' 
          
    to the build.gradle file inside the app folder 
  3. Now that you have added the project in your app, it is time to use it. 
 
The best way to use this component is from the layout xml file. The following example with llustrate the use: 

      <com.riyagayasen.easyaccordion.AccordionView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:visibility="visible"
        app:isAnimated="false"
        app:heading="This is a demo accordion"
        app:isExpanded="true"
        app:isPartitioned="true">

        <TextView
            android:id="@+id/textView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Demo accordion text" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Test Button"
            android:id="@+id/button_2"
            android:layout_below="@+id/textView" />
        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Test Button 2"
            android:layout_below="@+id/button_2" />


    </com.riyagayasen.easyaccordion.AccordionView>
The 'app' namespace can be defined as 
     
     xmlns:app="http://schemas.android.com/apk/res-auto"
     
This generates the layout as 

![Alt text](/screenshot.png?raw=true "Optional Title")

The accordion component has two parts. 
  1. Heading: the top part of the accordion, 'This is a demo accordion' in the above example. 
  2. The body, that I call 'paragraph'. This body is a RelativeLayout and would contain what ever UI elements you add to the accordion. 
  
You would see that there are several new attributes I have defined for the accordion. 
  1. Heading 
  
         app:heading="This is a demo accordion"
   This defines the string to be used as heading. 
  
  2. Partition (boolean): the 'line' between the heading and the paragraph. 
  
        app:isPartitioned="false"
    
    This  value determines if the line is drawn between the heading and the paragraph. 
  
  3. Expanded (boolean): this value determines if the paragraph is expanded by default. 
  
        app:isExpanded="true"
      
  
 
To add different elements into the body (paragraph) of the accordion component, you can simply define them within the AccordionView tag. 
