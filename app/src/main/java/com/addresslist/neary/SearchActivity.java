package com.addresslist.neary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.addresslist.neary.adapter.BaseAdapter;
import com.addresslist.neary.adapter.BaseViewHolder;
import com.addresslist.neary.bean.AppDatabase;
import com.addresslist.neary.bean.User;
import com.addresslist.neary.pinyin.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 搜索页面
 */
public class SearchActivity extends AppCompatActivity {

    private ImageView mBack;
    private ImageView mClear;
    private EditText mEditText;
    private List<User> lists = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initRecyclerView();
        lists.clear();
        lists.addAll(AppDatabase.getInstance(SearchActivity.this).userDao().findByName(""));
        updateUser();
    }

    private void initView() {
        pinyinComparator = new PinyinComparator();

        mBack = findViewById(R.id.back);
        mClear = findViewById(R.id.clear);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(),

                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e1) {

                } finally {
                    SearchActivity.this.finish();
                }
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
            }
        });

        mEditText = findViewById(R.id.edit);
        mRecyclerView = findViewById(R.id.main_recyclerview);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lists.clear();
                lists.addAll(AppDatabase.getInstance(SearchActivity.this).userDao().findByName(charSequence.toString()));
                updateUser();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

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
                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                ImageView imageView = holder.getView(R.id.icon);
                String subStr = lists.get(position).getName().substring(lists.get(position).getName().length() - 1);
                imageView.setImageDrawable(TextDrawable.builder().buildRound(subStr, Color.rgb(r, g, b)));

                RelativeLayout linearLayout = holder.getView(R.id.content);
                linearLayout.setClickable(true);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
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

    private void updateUser() {
        Collections.sort(lists, pinyinComparator);

        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

}
