package fr.lab.bbox.bboxapirunner.notification;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxSendMessage;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class NotificationSendMsgToChannelIdFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private EditText channelEdit;
    private EditText appIdEdit;
    private EditText msgEdit;

    private Bbox mBbox = Bbox.getInstance(false);

    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_sendmsgtochannelid, container, false);

        mButton = (Button) view.findViewById(R.id.try_sendmsgtochannelid);
        channelEdit = (EditText) view.findViewById(R.id.notification_channelidnumber);
        appIdEdit = (EditText) view.findViewById(R.id.notification_appIdchannel);
        msgEdit = (EditText) view.findViewById(R.id.notification_msgchannel);

        mButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(final View v) {
        String channel = channelEdit.getText().toString();
        String appId = appIdEdit.getText().toString();
        String msg = msgEdit.getText().toString();

        mBbox.sendMessage(channel, appId, msg, new IBboxSendMessage() {
            @Override
            public void onResponse() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Send message success", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Send message failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}