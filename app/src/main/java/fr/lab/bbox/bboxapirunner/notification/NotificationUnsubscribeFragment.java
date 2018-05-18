package fr.lab.bbox.bboxapirunner.notification;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetOpenedChannels;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxUnsubscribe;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */

// TODO : create a button to unsubscribe to all channels
public class NotificationUnsubscribeFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Button mButtonBis;
    private EditText channelIdEdit;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_unsubscribe, container, false);

        mButtonBis = (Button) view.findViewById(R.id.try_unsubscribeall);
        mButton = (Button) view.findViewById(R.id.try_unsubscribe);

        mButtonBis.setOnClickListener(this);
        mButton.setOnClickListener(this);

        channelIdEdit = (EditText) view.findViewById(R.id.notification_channelid);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (mButtonBis == v) {
            mBbox.getOpenedChannels(new IBboxGetOpenedChannels() {

                @Override
                public void onResponse(final List<String> channels) {
                    for (final String channel : channels) {

                        mBbox.unsubscribeNotification(channel, new IBboxUnsubscribe() {
                            @Override
                            public void onUnsubscribe() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Channels being removed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                String appId = channel.substring(0, channel.lastIndexOf("-"));
                                Log.d("notif", "appId : " + appId);

                                mBbox.removeMediaListener(appId, channel);
                                mBbox.removeAppListener(appId, channel);
                                mBbox.removeMsgListener(appId, channel);
                            }

                            @Override
                            public void onFailure(Request request, int errorCode) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "unsubscribe failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }

                }

                @Override
                public void onFailure(Request request, int errorCode) {
                }
            });
        } else if (mButton == v) {
            final String channelId = channelIdEdit.getText().toString();

            mBbox.unsubscribeNotification(channelId, new IBboxUnsubscribe() {
                @Override
                public void onUnsubscribe() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), channelId + " removed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    String appId = channelId.substring(0, channelId.lastIndexOf("-"));
                    Log.d("notif", "appId : " + appId);

                    mBbox.removeMediaListener(appId, channelId);
                    mBbox.removeAppListener(appId, channelId);
                    mBbox.removeMsgListener(appId, channelId);
                }

                @Override
                public void onFailure(Request request, int errorCode) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "unsubscribe failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}