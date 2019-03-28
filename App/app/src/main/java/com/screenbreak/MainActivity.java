package com.screenbreak;

import android.os.Bundle;
import android.os.Looper;

import com.screenbreak.app.AppFragment;
import com.screenbreak.common.FragmentAnimationCycle;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    private AppFragment m_appFragment;

    private Fragment m_currentFragment;

    private FragmentAnimationCycle m_fragmentAnimationCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("in onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Thread.currentThread().setUncaughtExceptionHandler(this::onUncaughtException);

        m_fragmentAnimationCycle = FragmentAnimationCycle.getInstance();

        m_appFragment = AppFragment.newInstance();

        showFragment(m_appFragment);
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

}
