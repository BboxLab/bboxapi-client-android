package fr.lab.bbox.bboxapirunner.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.lab.bbox.bboxapirunner.MainActivity;
import fr.lab.bbox.bboxapirunner.MyBboxNotFoundException;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;
import tv.bouyguestelecom.fr.bboxapilibrary.Bbox;
import tv.bouyguestelecom.fr.bboxapilibrary.callback.IBboxMessage;
import tv.bouyguestelecom.fr.bboxapilibrary.callback.IBboxRegisterApp;
import tv.bouyguestelecom.fr.bboxapilibrary.callback.IBboxSendMessage;
import tv.bouyguestelecom.fr.bboxapilibrary.callback.IBboxSubscribe;
import tv.bouyguestelecom.fr.bboxapilibrary.model.MessageResource;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by rmessara on 15/05/17.
 * bboxapi-client-android
 */

public class NotificationCreateNotificationFragment extends Fragment implements View.OnClickListener{

    private Button mButton;
    private Context mContext;
    private Handler handler;

    private String room = " ";
    private String appId = " ";
    private String msg = " ";
    private JSONObject mMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_createnotification, container, false);

        mButton = (Button) view.findViewById(R.id.try_sendmsgtoroom);
        mButton.setOnClickListener(this);

        mContext = getActivity().getApplicationContext();
        handler = new Handler();

        init();
        return view;
    }


    @Override
    public void onClick(final View v) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        final String ip = sharedPref.getString("bboxip", "");
        room = "Message/Notification";
        appId = "Create Notification";
        msg =  "demo";

        Bbox.getInstance().sendMessage(ip,
                getResources().getString(R.string.APP_ID),
                getResources().getString(R.string.APP_SECRET),
                room, appId, msg,
                new IBboxSendMessage() {

                    @Override
                    public void onResponse() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Send message success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Send message failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }

    public void init() {

        room = "Message/Notification";
        handler = new Handler();

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        final String ip = sharedPref.getString("bboxip", "");
        String appName = mContext.getString(R.string.app_name);
        Bbox.getInstance().registerApp(ip,
                mContext.getString(R.string.APP_ID),
                mContext.getString(R.string.APP_SECRET),
                appName,
                new IBboxRegisterApp() {
                    @Override
                    public void onResponse(final String registerApp) {
                        System.out.println("register app : " + registerApp);
                        if (registerApp != null && !registerApp.isEmpty()) {
                            // Subscribe for create room
                            Bbox.getInstance().subscribeNotification(ip,
                                    mContext.getString(R.string.APP_ID),
                                    mContext.getString(R.string.APP_SECRET),
                                    registerApp,
                                    room,
                                    new IBboxSubscribe() {
                                        @Override
                                        public void onSubscribe() {
                                            //received message from room
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mContext, "Subscribe success", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Bbox.getInstance().addListener(ip,
                                                    registerApp,
                                                    new IBboxMessage() {
                                                        @Override
                                                        public void onNewMessage(MessageResource message) {
                                                            Log.i("onNewMessage", "message : " + message.toString());
                                                            try {
                                                                mMessage = new JSONObject(message.toString());
                                                                if (!mMessage.get("message").equals(mContext.getPackageName()))
                                                                   createNotification(mContext.getPackageName());

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onFailure(Request request, int errorCode) {
                                            Log.i("onSubscribe", "subscribe failed");
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mContext, "subscribe failed", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });
                            // end create room
                        }
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        Log.i("IBboxRegisterApp", "register app failed");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "register app failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }


    public void createNotification(String title) {
        Bitmap icon = ((BitmapDrawable) getResources().getDrawable(R.drawable.logo)).getBitmap();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setLargeIcon(icon)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setOngoing(false)
                        .setContentText(title);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
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
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        int mId = 1;
        mNotificationManager.notify(mId, mBuilder.build());
    }

}

