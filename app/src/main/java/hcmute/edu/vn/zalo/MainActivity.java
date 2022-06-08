package hcmute.edu.vn.zalo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zing.zalo.zalosdk.oauth.LoginVia;
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;
import com.zing.zalo.zalosdk.oauth.model.ErrorResponse;

import java.security.MessageDigest;

import hcmute.edu.vn.zalo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        layBase64StringSha1(getApplicationContext());
        addEvents();
    }

    private void addEvents() {
        //gán sự kiện cho nút Login Zalo để gọi hàm đăng nhập
        binding.btnLoginZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gọi hàm đăng nhập Zalo:
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ZaloSDK.Instance.onActivityResult(this, requestCode, resultCode, data);
    }



    OAuthCompleteListener listener=new OAuthCompleteListener() {
        @Override
        public void onGetOAuthComplete(OauthResponse response) {
            if(TextUtils.isEmpty(response.getOauthCode())) {
                onLoginError(response.getErrorCode(),response.getErrorMessage());
            } else {
                onLoginSuccess();
            }
        }

        @Override
        public void onAuthenError(ErrorResponse errorResponse) {
            super.onAuthenError(errorResponse);
        }
    };
    private void onLoginSuccess() {
        Toast.makeText(MainActivity.this,"Bạn đã đăng nhập Zalo thành công",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void onLoginError(int errorCode,String errorMessage) {
        String msg="Đăng nhập thất bại, mã lỗi="+errorCode +", chi tiết lỗi="+errorMessage;
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    public static void layBase64StringSha1(Context ctx) {
        try
        {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sig = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim();
                if (sig.trim().length() > 0) {
                    Log.d("MY_SHA",sig);
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("LOI_SHA",ex.toString());
        }
    }



}