package com.sunchang.callhtmltest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginStatusCard {

    private Activity mActivity;

    private CardView mCardView;

    private LinearLayout mLinearLayout;

    private TextView mTvLogin;

    private TextView mTvSignup;

    private ProgressBar pbLogout;

    public LoginStatusCard(Activity activity, CardView cardView, LinearLayout linearLayout, TextView tvLogin, TextView tvSignup) {
        this.mActivity = activity;
        this.mCardView = cardView;
        this.mLinearLayout = linearLayout;
        this.mTvLogin = tvLogin;
        this.mTvSignup = tvSignup;
        pbLogout = new ProgressBar(activity);
    }

    public void setLoginViewStyles() {
        mCardView.setCardBackgroundColor(mActivity.getResources().getColor(R.color.login_card_bg));
        mCardView.setCardElevation(10);
        mTvLogin.setVisibility(View.INVISIBLE);
        mTvSignup.setText("退出登录");
    }

    public void setLogoutViewStyles() {
        mCardView.setCardBackgroundColor(mActivity.getResources().getColor(R.color.logout_card_bg));
        mCardView.setCardElevation(0);
        mTvLogin.setVisibility(View.VISIBLE);
        mLinearLayout.removeView(pbLogout);
        mTvSignup.setVisibility(View.VISIBLE);
        mTvSignup.setText("注册");
    }

    public void updateLogoutViews() {
        mTvSignup.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
        params.setMargins(60, 0, 118, 0);
        params.gravity = Gravity.CENTER;
        pbLogout.setLayoutParams(params);
        mLinearLayout.addView(pbLogout);
    }

    public void setOnLoginClickListener(final String baseUrl, final String loginUrl) {
        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, LoginDialog.class);
                intent.putExtra("baseUrl", baseUrl);
                intent.putExtra("loginUrl", loginUrl);
                mActivity.startActivityForResult(intent, 0);
                mActivity.overridePendingTransition(0, 0);
            }
        });
    }

    public void setOnSignupClickListener(final int index, final LoginActivity.LogoutWebView logoutWebView,
                                         final String baseUrl, final String loginUrl, final String registerUrl) {
        mTvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.LOGIN_STATUS[index]) {
                    logoutWebView.startLoad(loginUrl);
                    logoutWebView.setBaseUrl(baseUrl);
                    updateLogoutViews();
                } else {
                    Intent intent = new Intent(mActivity, SignupActivity.class);
                    intent.putExtra("signupUrl", registerUrl);
                    intent.putExtra("baseUrl", baseUrl);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(0, 0);
                }
            }
        });
    }
}
