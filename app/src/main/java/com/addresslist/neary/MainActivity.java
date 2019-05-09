package com.addresslist.neary;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.addresslist.neary.adapter.BaseAdapter;
import com.addresslist.neary.adapter.BaseViewHolder;
import com.addresslist.neary.bean.AppDatabase;
import com.addresslist.neary.bean.User;
import com.addresslist.neary.pinyin.PinyinComparator;
import com.addresslist.neary.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * 首页
 */
public class MainActivity extends BaseActivity {


    private List<User> lists = new ArrayList<>(); //数据源
    private RecyclerView mRecyclerView; //listview
    private BaseAdapter mAdapter; //适配器
    private SideBar mSideBar; //右侧字母搜索框
    private PinyinComparator pinyinComparator; //对排序的compare
    private FloatingActionMenu menuLabelsRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHead();

        initView();
        pinyinComparator = new PinyinComparator();
        initFloatAction();
        initRecyclerView();
        updateUser();
        menuLabelsRight.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lists.size() == 0) {
                    Toast.makeText(MainActivity.this, "请先添加联系人", Toast.LENGTH_SHORT).show();
                    menuLabelsRight.showMenu(true);
                }

            }
        }, 1000);
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRecyclerView = findViewById(R.id.main_recyclerview);
        mSideBar = findViewById(R.id.contact_sidebar);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = -1;
                for (int i = 0; i < lists.size(); i++) {
                    if (s.equals(lists.get(i).getPrefix())) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    mRecyclerView.getLayoutManager().scrollToPosition(position);
                }


            }
        });
    }


    /**
     * 初始化head信息
     */
    private void initHead() {
        setHeadTitle("通讯录");
        setBackClick(null);
        setMoreClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AblotUsActivity.class);
                startActivity(intent);

            }
        });

        setSearchClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (lists.size() == 0) {
                    Toast.makeText(MainActivity.this, "没有可查询的联系人,请先添加", Toast.LENGTH_SHORT).show();
                    menuLabelsRight.showMenu(true);
                    return;
                }

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化新增相关
     */
    private void initFloatAction() {

        menuLabelsRight = findViewById(R.id.menu_red);

        FloatingActionButton add = findViewById(R.id.action_add);
        FloatingActionButton qr = findViewById(R.id.action_qr);
        FloatingActionButton rili = findViewById(R.id.action_rili);
        FloatingActionButton naozhong = findViewById(R.id.action_naozhong);
        FloatingActionButton action_net = findViewById(R.id.action_net);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLabelsRight.close(false);
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivityForResult(intent, 102);

            }
        });
        action_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLabelsRight.close(false);
                Uri uri = Uri.parse("https://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLabelsRight.close(false);

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 333);

            }
        });


        rili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLabelsRight.close(false);

                Intent i = new Intent();
                ComponentName cn = null;
                if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                    cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
                } else {
                    cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");

                }

                i.setComponent(cn);

                startActivity(i);
            }
        });

        naozhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLabelsRight.close(false);

                Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);

                startActivity(alarms);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 333 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gotoQrCodeActivity();
        } else {
            Toast.makeText(this, "无法获取相机权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoQrCodeActivity() {
        menuLabelsRight.close(true);
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 101);
    }

    /**
     * 初始化recyclerview
     */
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new BaseAdapter() {
            @Override
            public void onBindView(BaseViewHolder holder, final int position) {
                holder.setText(R.id.user_name, lists.get(position).getName())
                        .setText(R.id.prefix, lists.get(position).getPrefix().substring(0, 1).toUpperCase());

                if (position != 0) {
                    String prefix = lists.get(position).getPrefix();
                    String lastPrefix = lists.get(position - 1).getPrefix();
                    if (TextUtils.isEmpty(prefix) || TextUtils.isEmpty(lastPrefix)) {
                        holder.setVisibility(R.id.layout_prefix, BaseViewHolder.GONE);
                    } else {
                        if (prefix.equals(lastPrefix)) {
                            holder.setVisibility(R.id.layout_prefix, BaseViewHolder.GONE);
                        } else {
                            holder.setVisibility(R.id.layout_prefix, BaseViewHolder.VISIBLE);
                        }
                    }
                } else {
                    holder.setVisibility(R.id.layout_prefix, BaseViewHolder.VISIBLE);
                }
                //随机生成一种颜色
                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                ImageView imageView = holder.getView(R.id.icon);


                //显示用户名最后一位
                String subStr = lists.get(position).getName().substring(lists.get(position).getName().length() - 1);


                //使用三方库转换成drawable
                imageView.setImageDrawable(TextDrawable.builder().buildRound(subStr, Color.rgb(r, g, b)));


                RelativeLayout linearLayout = holder.getView(R.id.content);
                linearLayout.setClickable(true);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("id", lists.get(position).getUid());
                        startActivityForResult(intent, 100);
                    }
                });

            }

            @Override
            public int getLayoutID(int position) {
                return R.layout.item_main;
            }

            @Override
            public boolean clickable() {
                return false;
            }

            @Override
            public int getItemCount() {
                return lists.size();
            }
        };
        mRecyclerView.setAdapter(mAdapter);


    }

    /**
     * 更新通讯录数据
     */
    private void updateUser() {
        lists.clear();
        lists.addAll(AppDatabase.getInstance(MainActivity.this).userDao().getAll());
        Collections.sort(lists, pinyinComparator);
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            //获取二维码返回的结果，转换成用户信息
            if (data.getIntExtra(CodeUtils.RESULT_TYPE, -1) == CodeUtils.RESULT_SUCCESS) {
                String json = data.getStringExtra(CodeUtils.RESULT_STRING);
                final User user = new Gson().fromJson(json, User.class);
                user.setUid(0);

                new AlertDialog.Builder(MainActivity.this).setTitle("确认保存" + user.getName() + "的个人信息?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();


                        user.setUid(AppDatabase.getInstance(MainActivity.this).userDao().insertUser(user));
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("id", user.getUid());
                        startActivityForResult(intent, 100);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).create().show();


            } else {
                Toast.makeText(this, "解析失败", Toast.LENGTH_SHORT).show();
            }

        } else {
            updateUser();
        }
    }
}
