package com.addresslist.neary;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * app的基类
 */
public class BaseActivity extends AppCompatActivity {


    void setHeadTitle(String value) {
        TextView title = findViewById(R.id.h_title);
        title.setText(value);
    }

    void setMoreClick(Drawable drawable, View.OnClickListener click) {
        ImageView more = findViewById(R.id.h_more);
        more.setImageDrawable(drawable);
        if (click == null) {
            more.setVisibility(View.GONE);
        } else {
            more.setVisibility(View.VISIBLE);
            more.setClickable(true);
            more.setOnClickListener(click);
        }
    }

    void setMoreClick(View.OnClickListener click) {
        View more = findViewById(R.id.h_more);
        if (click == null) {
            more.setVisibility(View.GONE);
        } else {
            more.setVisibility(View.VISIBLE);
            more.setClickable(true);
            more.setOnClickListener(click);
        }
    }

    void setBackClick(View.OnClickListener click) {
        View more = findViewById(R.id.back);
        if (click == null) {
            more.setVisibility(View.GONE);
        } else {
            more.setVisibility(View.VISIBLE);
            more.setClickable(true);
            more.setOnClickListener(click);
        }
    }

    void setSearchClick(View.OnClickListener click) {
        View search = findViewById(R.id.h_search);
        if (click == null) {
            search.setVisibility(View.GONE);
        } else {
            search.setVisibility(View.VISIBLE);
            search.setClickable(true);
            search.setOnClickListener(click);
        }
    }


}
