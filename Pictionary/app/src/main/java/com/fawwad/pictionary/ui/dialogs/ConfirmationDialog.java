package com.fawwad.pictionary.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fawwad.pictionary.Pictionary;
import com.fawwad.pictionary.R;

public class ConfirmationDialog extends Dialog implements View.OnClickListener {

    private String messageText, positiveButtonText, negativeButtonText;
    private OnClickListener positiveListener, negativeListener;

    private TextView message;
    private Button positiveButton, negativeButton;

    public ConfirmationDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmationDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected ConfirmationDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirmation);
        if (getWindow() != null) {
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout((int) (getContext().getResources().getDisplayMetrics().widthPixels * 1.00), ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        message = findViewById(R.id.tv_message);
        positiveButton = findViewById(R.id.btn_positive);
        negativeButton = findViewById(R.id.btn_negative);
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        setData();
    }

    public void setData() {
        if (message != null)
            message.setText(messageText);
        if (positiveButton != null)
            positiveButton.setText(positiveButtonText);
        if (negativeButton != null)
            negativeButton.setText(negativeButtonText);
    }

    public ConfirmationDialog setMessage(String message) {
        this.messageText = message;
        return this;
    }

    public ConfirmationDialog setPositiveButton(String text, OnClickListener onClickListener) {
        positiveButtonText = text;
        positiveListener = onClickListener;
        return this;
    }

    public ConfirmationDialog setNegativeButton(String text, OnClickListener onClickListener) {
        negativeButtonText = text;
        negativeListener = onClickListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_positive:
                if (positiveListener != null) {
                    positiveListener.onClick(this, 1);
                }
                break;
            case R.id.btn_negative:
                if (negativeListener != null) {
                    negativeListener.onClick(this, -1);
                } else dismiss();
                break;
        }
    }
}

