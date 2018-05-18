package fr.lab.bbox.bboxapirunner.notification;

import android.content.Context;
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
public class NotificationSendMsgToRoomFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private EditText msgEdit;
    private EditText roomEdit;
    private EditText appIdEdit;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_sendmsgtoroom, container, false);

        mButton = (Button) view.findViewById(R.id.try_sendmsgtoroom);
        mButton.setOnClickListener(this);

        roomEdit = (EditText) view.findViewById(R.id.notification_roomname);
        appIdEdit = (EditText) view.findViewById(R.id.notification_appId);
        msgEdit = (EditText) view.findViewById(R.id.notification_msg);

        return view;
    }


    @Override
    public void onClick(final View v) {
        String room = "Message/" + roomEdit.getText().toString();
        String appId = appIdEdit.getText().toString();
        String msg = msgEdit.getText().toString();

        mBbox.sendMessage(room, appId, msg, new IBboxSendMessage() {
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