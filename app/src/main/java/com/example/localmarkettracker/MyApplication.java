package com.example.localmarkettracker;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();



        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dh1dstfzf");
        config.put("api_key", "575676121955962");
        config.put("api_secret", "GXy-2L_SfHxxrhzCBmeZUTtZEjo");
        MediaManager.init(this, config);
    }
}
