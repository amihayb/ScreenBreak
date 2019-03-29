package com.screenbreak.app;


import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.screenbreak.R;
import com.screenbreak.common.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;


public class AppFragment extends BaseFragment implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {

    private ImageView m_imageView;

    private int m_state;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppFragment.
     */
    public static AppFragment newInstance() {
        return new AppFragment();
    }

    public AppFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("in onCreate");
        checkNfc();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("in onCreateView");

        View fragmentView = inflater.inflate(R.layout.fragment_app, container, false);
        m_imageView = fragmentView.findViewById(R.id.fragment_app_image);
        m_imageView.setOnClickListener(v -> {
            if (m_state == 2 || m_state == 5) {
                return;
            }

            updateState(m_state + 1);
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("State");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer state = dataSnapshot.child("App").getValue(Integer.class);

                if (state == null) {
                    return;
                }

                updateState(state);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return fragmentView;
    }


    private void updateState(int state) {

        Timber.i("Changing state form %d to %d", m_state, state);

        m_state = state;

        @DrawableRes final int imgRes;

        switch (state) {
            case 0:
                imgRes = R.drawable.screen0;
                break;
            case 1:
                imgRes = R.drawable.screen1;
                break;
            case 2:
                imgRes = R.drawable.screen2;
                break;
            case 3:
                imgRes = R.drawable.screen3;
                break;
            case 4:
                imgRes = R.drawable.screen4;
                break;
            case 5:
                imgRes = R.drawable.screen5;
                break;
            default:
                Timber.e("Failed on try to recognize the state %d image", state);
                return;
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            getActivity().runOnUiThread(() -> m_imageView.setImageResource(imgRes));
        } else {
            m_imageView.setImageResource(imgRes);
        }
    }

    private void checkNfc() {
        Timber.d("in checkNfc");

        //Check if NFC is available on device
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
        if (nfcAdapter == null) {
            showToast("NFC not available on this device", false);
        } else {
            //This will refer back to createNdefMessage for what it will send
            nfcAdapter.setNdefPushMessageCallback(this, getActivity());
            //This will be called if the message is sent successfully
            nfcAdapter.setOnNdefPushCompleteCallback(this, getActivity());
        }
    }


    private static final NdefRecord s_ndefRecord = new NdefRecord(
            NdefRecord.TNF_WELL_KNOWN,  //Our 3-bit Type name format
            NdefRecord.RTD_TEXT,        //Description of our payload
            new byte[0],                //The optional id for our Record
            "Hello World!".getBytes(Charset.forName("UTF-8")));

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        Timber.d("in createNdefMessage");
        FirebaseDatabase.getInstance().getReference().child("State").child("App").setValue(m_state + 1);
        return new NdefMessage(s_ndefRecord);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        Timber.d("in onNdefPushComplete");
    }
}
