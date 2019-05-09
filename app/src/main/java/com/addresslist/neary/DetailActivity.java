package com.addresslist.neary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.addresslist.neary.bean.AppDatabase;
import com.addresslist.neary.bean.User;
import com.addresslist.neary.widget.QRCodeDialog;

import java.util.Random;

/**
 * 用户详情页面
 */
public class DetailActivity extends BaseActivity {

    private LinearLayout mMsg; //发消息
    private LinearLayout mCall;//打电话
    private TextView mMark;//备注
    private TextView mQq; //qq
    private TextView mWechat; //wechat
    private TextView mName; //名字
    private TextView mPhone; //手机号
    private long mId;
    private User mUser;
    private BottomSheetDialog mMBottomSheetDialog; //对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mId = getIntent().getLongExtra("id", -1);
        mUser = AppDatabase.getInstance(this).userDao().findByID(mId);
        if (mUser == null) {
            Toast.makeText(this, "未找到", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        setHeadTitle("详情");
        setMoreClick(new View.OnClickListener() {


            @Override
            public void onClick(View view1) {
                if (!mMBottomSheetDialog.isShowing()) {
                    mMBottomSheetDialog.show();
                } else {
                    mMBottomSheetDialog.dismiss();
                }


            }
        });
        setSearchClick(null);
        setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.finish();
            }
        });
        initView();
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mName.setText(mUser.getName());
        mPhone.setText(mUser.getPhone());
        mMark.setText(mUser.getMark());
        mQq.setText(mUser.getQq());
        mWechat.setText(mUser.getWechat());


    }

    /**
     * init view
     */
    private void initView() {

        mMsg = findViewById(R.id.msg);
        mCall = findViewById(R.id.call);
        mMark = findViewById(R.id.mark);
        mQq = findViewById(R.id.qq);
        mWechat = findViewById(R.id.wechat);
        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        ImageView imageView = findViewById(R.id.icon);
        String subStr = mUser.getName().substring(mUser.getName().length() - 1);


        imageView.setImageDrawable(TextDrawable.builder().buildRound(subStr, Color.rgb(r, g, b)));

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DetailActivity.this).setTitle("确认给" + mUser.getName() + "打电话?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        call(mUser.getPhone());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).create().show();

            }
        });

        mMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DetailActivity.this).setTitle("确认给" + mUser.getName() + "发送短信?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        sms(mUser.getPhone());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).create().show();


            }
        });
        WindowManager wm = DetailActivity.this.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();

        mMBottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
        //导入底部reycler布局
        View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.sheet_layout, null, false);
        mMBottomSheetDialog.setContentView(view);

        View qrCode = view.findViewById(R.id.qrcode);
        final View edit = view.findViewById(R.id.edit);
        View delete = view.findViewById(R.id.delete);

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMBottomSheetDialog.dismiss();
                QRCodeDialog qrCodeDialog = new QRCodeDialog(DetailActivity.this, mUser.getName(), new Gson().toJson(mUser));
                qrCodeDialog.show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMBottomSheetDialog.dismiss();
                Intent intent = new Intent(DetailActivity.this, AddPersonActivity.class);
                intent.putExtra("id", mUser.getUid());
                startActivityForResult(intent, 100);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMBottomSheetDialog.dismiss();

                new AlertDialog.Builder(DetailActivity.this).setTitle("确认删除该联系人?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(DetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        AppDatabase.getInstance(DetailActivity.this).userDao().delete(mUser);
                        editSuccess = true;
                        DetailActivity.this.finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).create().show();


            }
        });

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        //设置默认弹出高度为屏幕的0.4倍
        mBehavior.setPeekHeight((int) (0.4 * height));
    }


    /**
     * send sms
     *
     */
    private void sms(String phone) {
        Uri uri2 = Uri.parse("smsto:" + phone);
        Intent intentMessage = new Intent(Intent.ACTION_VIEW, uri2);
        startActivity(intentMessage);


    }

    /**
     * 调用拨号界面
     *
     */

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            editSuccess = true;
            mUser = AppDatabase.getInstance(this).userDao().findByID(mId);
            initView();
            initData();
        }
    }

    private boolean editSuccess; //用来判断当前信息有没有变动

    @Override
    public void finish() {
        if (editSuccess) {
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
