package com.addresslist.neary;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.addresslist.neary.bean.AppDatabase;
import com.addresslist.neary.bean.User;
import com.addresslist.neary.pinyin.CharacterParser;

public class AddPersonActivity extends BaseActivity {

    private long mId; //通过id来判断是新增还是编辑
    private EditText mName;
    private EditText mPhone;
    private EditText mQq;
    private EditText mWechat;
    private EditText mMark;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        mId = getIntent().getLongExtra("id", -1);
        initData();
        initHead();
        initView();
    }

    private void initData() {
        if (mId >= 0) {
            mUser = AppDatabase.getInstance(this).userDao().findByID(mId);

            if (mUser == null) {
                Toast.makeText(this, "你要编辑的联系人不存在", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
    }

    private void initView() {

        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mQq = findViewById(R.id.qq);
        mWechat = findViewById(R.id.wechat);
        mMark = findViewById(R.id.mark);

        if (mUser != null) {
            mName.setText(mUser.getName());
            mPhone.setText(mUser.getPhone());
            mQq.setText(mUser.getQq());
            mWechat.setText(mUser.getWechat());
            mMark.setText(mUser.getMark());

        }

    }

    private void initHead() {
        if (mId >= 0) {
            setHeadTitle("编辑联系人");
        } else {
            setHeadTitle("新增联系人");
        }

        setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPersonActivity.this.finish();
            }
        });
        setSearchClick(null);
        setMoreClick(ContextCompat.getDrawable(this, R.drawable.ic_save_black_24dp), new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mName.getText().toString().trim())) {
                    Toast.makeText(AddPersonActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mPhone.getText().toString())) {
                    Toast.makeText(AddPersonActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User();
                user.setName(mName.getText().toString().trim());
                user.setPhone(mPhone.getText().toString());
                user.setQq(mQq.getText().toString());
                user.setWechat(mWechat.getText().toString());
                user.setMark(mMark.getText().toString());

                String result = CharacterParser.getInstance().getSelling(mName.getText().toString().trim()).substring(0, 1).toUpperCase();

                try {
                    //判断是不是字母，不是的话就用#代替
                    if (!Character.isLetter(result.toCharArray()[0])) {
                        result = "#";
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    user.setPrefix(result);

                }


                if (mId >= 0) {
                    user.setUid(mId);
                    AppDatabase.getInstance(AddPersonActivity.this).userDao().update(user);
                    Toast.makeText(AddPersonActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    AddPersonActivity.this.finish();
                } else {
                    AppDatabase.getInstance(AddPersonActivity.this).userDao().insertAll(user);
                    Toast.makeText(AddPersonActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    AddPersonActivity.this.finish();
                }
            }
        });

    }
}
