package com.screenbreak.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment extends Fragment {

    @VisibleForTesting
    private ProgressDialog m_progressDialog;

    @UiThread
    protected void showProgressDialog(String msg) {
        if (StringUtils.isNullOrEmpty(msg)) {
            return;
        }

        if (m_progressDialog == null) {
            m_progressDialog = new ProgressDialog(getContext());
            m_progressDialog.setCancelable(false);
            m_progressDialog.setIndeterminate(true);
        }

        m_progressDialog.setMessage(msg);

        m_progressDialog.show();
    }

    @UiThread
    protected void hideProgressDialog() {
        if (m_progressDialog != null && m_progressDialog.isShowing()) {
            m_progressDialog.dismiss();
        }
    }

    @UiThread
    protected void showSnackBar(String msg, boolean isLong) {
        if (!StringUtils.isNullOrEmpty(msg)) {
            Snackbar.make(getView(), msg, isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
        }
    }

    @UiThread
    protected void showToast(String msg, boolean isLong) {
        if (!StringUtils.isNullOrEmpty(msg)) {
            Toast.makeText(getContext(), msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        }
    }

    protected void showInfoMessage(boolean isCancelable, String title, String msg, @Nullable DialogInterface.OnClickListener clickListener) {
        new AlertDialog.Builder(getContext())
                .setCancelable(isCancelable)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", clickListener)
                .create()
                .show();
    }


    public void hideKeyboard() {
        FragmentActivity activity = getActivity();
        if (activity != null) {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
