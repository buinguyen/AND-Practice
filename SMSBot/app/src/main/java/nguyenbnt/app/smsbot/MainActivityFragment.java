package nguyenbnt.app.smsbot;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mediatek.telephony.SmsManagerEx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainActivity.IOnClickFabListener {

    private EditText edtPhone;
    private EditText edtMessage;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        edtPhone = v.findViewById(R.id.edt_receiver);
        edtMessage = v.findViewById(R.id.edt_content);

        ((MainActivity) getActivity()).setOnFabClickListener(this);

        return v;
    }

    @Override
    public void onFabClick() {
        sendMessage();
    }

    private void sendMessage() {

//        String phone = "+841663729672";
//        String text = "Hello. I'm Nguyen";
        String phone = edtPhone.getText().toString();
        String text = edtMessage.getText().toString();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(text)) {
            Toast.makeText(getActivity().getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (((MainActivity) getActivity()).isGranted) {
            Toast.makeText(getActivity().getApplicationContext(), "Sending...", Toast.LENGTH_SHORT).show();
            sendSMS(getActivity(), phone, text);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Don't have permission send sms", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(Context mContext, String phoneNumber, String smsText) {
        ArrayList<String> parts = SimPeer.divideMessage(smsText);
        int size = parts.size();
        if (size == 1) {
            sendTextMessage(mContext, phoneNumber, smsText, null, null);
        } else {
            Log.e("sms_over_length", parts.toString());
            sendMultipartTextMessage(mContext, phoneNumber, parts, null, null);
        }
    }

    private boolean sendTextMessage(Context mContext, String toNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        try {
            if (!SimPeer.sendTextMessage(mContext, 0, toNum, smsText, sentIntent, deliveryIntent)) {
                SmsManagerEx.getDefault().sendTextMessage(toNum, null, smsText, sentIntent, deliveryIntent, 0);
            }
            return true;
        } catch (Exception e) {
            Log.e("TelephoneEx", "Exception:" + e.getMessage());
        }
        return false;
    }

    private boolean sendMultipartTextMessage(Context mContext, String toNum, ArrayList<String> smsTextlist, ArrayList<PendingIntent> sentIntentList, ArrayList<PendingIntent> deliveryIntentList) {
        try {
            if (!SimPeer.sendMultipartTextMessage(mContext, 0, toNum, smsTextlist, sentIntentList, deliveryIntentList)) {
                SmsManagerEx.getDefault().sendMultipartTextMessage(toNum, null, smsTextlist, sentIntentList, deliveryIntentList, 0);
            }
            return true;
        } catch (Exception e) {
            Log.e("TelephoneEx", "Exception:" + e.getMessage());
        }
        return false;
    }
}
