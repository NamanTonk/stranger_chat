package com.newEra.strangers.chat.util;

import android.os.Build;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.newEra.strangers.chat.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class AdRequest {

    public static com.google.android.gms.ads.AdRequest getRequest() {
        if (BuildConfig.VERSION_CODE <= 28)
            return new com.google.android.gms.ads.AdRequest.Builder().addTestDevice("909CC080B50837C15345729C43D635BF").build();
        else {

            ArrayList<String> adsIds = new ArrayList();
            adsIds.add("455AE398671B2CB011A196C27E3B6E3F");
            adsIds.add("909CC080B50837C15345729C43D635BF");
            adsIds.add(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR);
            MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(adsIds).build());
            return new com.google.android.gms.ads.AdRequest.Builder().build();
        }
    }
}
