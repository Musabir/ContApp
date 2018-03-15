package androlite.androidfragmentsearchview.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class Textview_Font extends android.support.v7.widget.AppCompatTextView {

    public Textview_Font(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Textview_Font(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Textview_Font(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "gillsansmt.ttf");
        setTypeface(tf ,1);

    }
}