package com.carlsberg_stack.samudayapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;


import com.carlsberg_stack.samudayapp.databinding.ActivityMainSdkBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import in.novopay.insurance.StartInsuranceSdk;
import in.novopay.supportlib.StartWaasSdk;
import in.novopay.supportlib.WaasSdkStatus;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainSdkBinding binding =
                DataBindingUtil.setContentView(
                        this,
                        R.layout.activity_main_sdk
                );

        binding.btnOpenSdSdk.setOnClickListener(view -> startWaasSdk(binding.etMobile.getText().toString()));
    }

    private void startWaasSdk(String mobileNum) {
        registerWaasSdk();

        StartInsuranceSdk.launch(
                this,
                mobileNum,
                BuildConfig.API_KEY,
                StartWaasSdk.WaasEnvironment.QA,
                getAppSignatures().get(0));

//        StartSendMoneySdk.launch(
//                requireContext(),
//                "9821166128",
//                "4051c9d9d616Jvuy0B3Y817t4ow",
//                StartWaasSdk.WaasEnvironment.QA,
//                getAppKey(requireContext()))
    }

    private void registerWaasSdk() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWaasSdkStatusUpdate(WaasSdkStatus sdkStatus) {
        Toast.makeText(this, sdkStatus.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*private void startWaasSdk(String mobileNum) {
        registerWaasSdk();

        StartCustomerAppsSdk.launch(
                this,
                mobileNum,
                BuildConfig.API_KEY,
                StartCustomerAppsSdk.WaasEnvironment.QA,
                getAppSignatures().get(0));
    }

    private void registerWaasSdk() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWaasSdkStatusUpdate(CustomerAppsSdkStatus suryodaySdkStatus) {
        Toast.makeText(this, suryodaySdkStatus.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }*/

    public ArrayList<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();

        try {
            // Get all package signatures for the current package
            String packageName = getPackageName();
            PackageManager packageManager = getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures;

            // For each signature create a compatible hash
            for (Signature signature : signatures) {
                String hash = hash(packageName, signature.toCharsString());
                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            android.util.Log.e("TAG", "Unable to find package to obtain hash.", e);
        }
        return appCodes;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String hash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(BuildConfig.HASH_TYPE);
            messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));
            byte[] hashSignature = messageDigest.digest();

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, Integer.parseInt(BuildConfig.NUM_HASHED_BYTES));
            // encode into Base64
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, Integer.parseInt(BuildConfig.NUM_BASE64_CHAR));

            android.util.Log.e("TAG", String.format("pkg: %s -- hash: %s", packageName, base64Hash));
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
            android.util.Log.e("TAG", "hash:NoSuchAlgorithm", e);
        }
        return null;
    }
}