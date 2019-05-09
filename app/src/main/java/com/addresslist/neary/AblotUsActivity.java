package com.addresslist.neary;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 关于
 */
public class AblotUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ablot_us);
        setHeadTitle("关于我们");
        setMoreClick(null);
        setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AblotUsActivity.this.finish();
            }
        });
        setSearchClick(null);
        TextView view = findViewById(R.id.about_us);
        String aboutusStr = "coded by Neary\t\n邮箱：neary_li@163.com\t\n学校：北方民族大学\t\n指导老师：李强";
        view.setText(aboutusStr);
    }
}
