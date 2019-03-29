package com.screenbreak;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;

import com.screenbreak.app.AppFragment;
import com.screenbreak.common.FragmentAnimationCycle;

import java.util.Arrays;
import java.util.OptionalInt;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 123;

    private AppFragment m_appFragment;

    private Fragment m_currentFragment;

    private FragmentAnimationCycle m_fragmentAnimationCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("in onCreate");

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            String value = intent.getStringExtra("key"); //if it's a string you stored.
            Timber.d("Key = %s", value);
        }

        setContentView(R.layout.activity_main);

//        findViewById(R.id.fragment_container)
//                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Thread.currentThread().setUncaughtExceptionHandler(this::onUncaughtException);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        m_fragmentAnimationCycle = FragmentAnimationCycle.getInstance();

        m_appFragment = AppFragment.newInstance();

        showFragment(m_appFragment);
    }

    @Override
    protected void onStart() {
        Timber.d("in onStart");

        getPermission();

        super.onStart();
    }

    @Override
    public void onPause() {
        Timber.d("in onPause");

        super.onPause();
    }

    @Override
    public void onResume() {
        Timber.d("in onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Timber.d("in onStop");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Timber.d("in onDestroy");

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Timber.d("in onBackPressed");


//        if (/*some condition*/) {
//            if (Looper.myLooper() != Looper.getMainLooper()) {
//                runOnUiThread(() -> replaceFragment(/*some fragment*/, false));
//            } else {
//                replaceFragment(/*some fragment*/, false);
//            }
//        } else {
        super.onBackPressed();
//        }
    }

    public String[] readPermissions() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info != null) {
                return info.requestedPermissions;
            }
        } catch (Exception e) {
            Timber.e(e, "Failed on try to read permissions");
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getPermission() {
        String[] permissions = readPermissions();

        ActivityCompat.requestPermissions(MainActivity.this,
                permissions,
                REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        OptionalInt first = Arrays.stream(grantResults).filter(value -> value != PackageManager.PERMISSION_GRANTED).findFirst();
        if (first.isPresent()) {
            // Not all permissions granted
            getPermission();
        }
    }

    @UiThread
    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        Timber.d("in replaceFragment");

        if (m_currentFragment == fragment) {
            return;
        }

        FragmentAnimationCycle.AnimationTuple next = m_fragmentAnimationCycle.getNext();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(next.getGoInAnim(), next.getGoOutAnim());
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

        m_currentFragment = fragment;
    }

    private void showFragment(final Fragment fragment) {
        Timber.d("in showFragment");

        if (Looper.myLooper() != Looper.getMainLooper()) {
            runOnUiThread(() -> replaceFragment(fragment, false));
        } else {
            replaceFragment(fragment, false);
        }
    }

    private void onUncaughtException(Thread thread, Throwable throwable) {
        Timber.wtf(throwable, "Caught unhandled exception on %s", thread.toString());

        finishAffinity();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Timber.d("in onNewIntent");
    }


}
