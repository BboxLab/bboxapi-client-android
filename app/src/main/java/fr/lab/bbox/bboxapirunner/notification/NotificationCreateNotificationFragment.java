package fr.lab.bbox.bboxapirunner.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxMessage;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxRegisterApp;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxSendMessage;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxSubscribe;
import fr.bouyguestelecom.tv.bboxapi.model.MessageResource;
import fr.lab.bbox.bboxapirunner.MainActivity;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by rmessara on 15/05/17.
 * bboxapi-client-android
 */

public class NotificationCreateNotificationFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Handler handler = new Handler();

    private String room = " ";
    private JSONObject mMessage;

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_createnotification, container, false);

        mButton = (Button) view.findViewById(R.id.try_sendmsgtoroom);
        mButton.setOnClickListener(this);

        init();

        return view;
    }


    @Override
    public void onClick(final View v) {
        room = "Message/Notification";
        String appId = "Create Notification";
        String msg = "demo";

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

    public void init() {
        room = "Message/Notification";
        handler = new Handler();

        String appName = getString(R.string.app_name);

        mBbox.registerApp(appName, new IBboxRegisterApp() {
            @Override
            public void onResponse(final String registerApp) {
                System.out.println("register app : " + registerApp);

                if (registerApp != null && !registerApp.isEmpty()) {
                    // Subscribe for create room
                    mBbox.subscribeNotification(registerApp, room, new IBboxSubscribe() {
                        @Override
                        public void onSubscribe() {
                            //received message from room
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Subscribe success", Toast.LENGTH_SHORT).show();
                                }
                            });

                            mBbox.addListener(new IBboxMessage() {
                                @Override
                                public void onNewMessage(MessageResource message) {
                                    Log.v("onNewMessage", "message : " + message.toString());

                                    try {
                                        mMessage = new JSONObject(message.toString());

                                        String packageName = getActivity().getPackageName();

                                        if (!mMessage.get("message").equals(packageName))
                                            createNotification(packageName);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request request, int errorCode) {
                            Log.v("onSubscribe", "subscribe failed");

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "subscribe failed", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                    // end create room
                }
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                Log.e("IBboxRegisterApp", "register app failed");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "register app failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public void createNotification(String title) {
        Bitmap icon = ((BitmapDrawable) getActivity().getDrawable(R.drawable.logo)).getBitmap();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setLargeIcon(icon)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setOngoing(false)
                        .setContentText(title);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        int mId = 1;
        mNotificationManager.notify(mId, mBuilder.build());
    }

}

