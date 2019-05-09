package com.addresslist.neary.widget;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.addresslist.neary.BaseActivity;
import com.addresslist.neary.R;
import com.addresslist.neary.utils.QRUtils;

/**
 * 用于弹出二维码的对话框
 */
public class QRCodeDialog {


    private BaseActivity mActivity;
    private String title;
    private String qrContent;
    private AlertDialog mAlertDialog;

    public QRCodeDialog(BaseActivity activity, String title, String qrContent) {
        mActivity = activity;
        this.title = title;
        this.qrContent = qrContent;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_qr, null);
        mAlertDialog = new AlertDialog.Builder(mActivity)
                .setView(view)
                .create();
        mAlertDialog.setCanceledOnTouchOutside(true);

        ImageView image = view.findViewById(R.id.qrcode);
        TextView title_name = view.findViewById(R.id.title_name);

        title_name.setText(title);
        try {
            image.setImageBitmap(QRUtils.createCode(mActivity, qrContent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
            }
        });


    }


    public void show() {
        mAlertDialog.show();
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }
}
