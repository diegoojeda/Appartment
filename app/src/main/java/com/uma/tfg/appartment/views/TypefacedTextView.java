package com.uma.tfg.appartment.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefacedTextView extends TextView{

    public TypefacedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TypefacedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypefacedTextView(Context context) {
        super(context);
        init();
    }

    private void init(){
        if (!this.isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/reis.ttf");
            setTypeface(tf);
        }
    }
}
