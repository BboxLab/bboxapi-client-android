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
import android.widget.TextView;
import android.widget.Toast;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxApplication;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxMedia;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxMessage;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxRegisterApp;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxSubscribe;
import fr.bouyguestelecom.tv.bboxapi.model.ApplicationResource;
import fr.bouyguestelecom.tv.bboxapi.model.MediaResource;
import fr.bouyguestelecom.tv.bboxapi.model.MessageResource;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class NotificationSubscribeFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private EditText appNameEdit;
    private EditText ressourceEdit;
    private TextView channelid_subscribe;

    private String ressource_id = " ";

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_subscribe, container, false);

        mButton = (Button) view.findViewById(R.id.try_subscribe);
        appNameEdit = (EditText) view.findViewById(R.id.notification_appname);
        ressourceEdit = (EditText) view.findViewById(R.id.notification_ressource);
        channelid_subscribe = (TextView) view.findViewById(R.id.channelid_subscribe);

        mButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(final View v) {
        String appName = appNameEdit.getText().toString();
        ressource_id = ressourceEdit.getText().toString();

        mBbox.registerApp(appName, new IBboxRegisterApp() {
            @Override
            public void onResponse(final String registerApp) {
                if (registerApp != null && !registerApp.isEmpty()) {
                    Log.i("notif", "ressource_id : " + ressource_id);
                    Log.i("notif", "registerApp : " + registerApp);

                    switch (ressource_id.toLowerCase()) {
                        case "media":
                            mBbox.subscribeNotification(registerApp, "Media", new IBboxSubscribe() {
                                @Override
                                public void onSubscribe() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            channelid_subscribe.setText("websocket opened");
                                        }
                                    });

                                    mBbox.addListener(new IBboxMedia() {
                                        @Override
                                        public void onNewMedia(final MediaResource mediaResource) {

                                            Log.i("notif", "channel changed");

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), mediaResource.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Request request, int errorCode) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Notification failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            break;

                        case "application":
                            mBbox.subscribeNotification(registerApp, "Application", new IBboxSubscribe() {
                                @Override
                                public void onSubscribe() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            channelid_subscribe.setText("websocket opened");
                                        }
                                    });

                                    mBbox.addListener(new IBboxApplication() {
                                        @Override
                                        public void onNewApplication(final ApplicationResource application) {

                                            Log.i("notif", "app changed");

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), application.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Request request, int errorCode) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Notification failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            break;
                    }

                    if (ressource_id.toLowerCase().contains("message")) {
                        if (ressource_id.contains("message")) {
                            ressource_id = ressource_id.replace("message", "Message");
                        }

                        // Subscribe ressource msg
                        mBbox.subscribeNotification(registerApp, ressource_id, new IBboxSubscribe() {
                            @Override
                            public void onSubscribe() {

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        channelid_subscribe.setText("websocket opened");
                                    }
                                });

                                mBbox.addListener(new IBboxMessage() {
                                    @Override
                                    public void onNewMessage(final MessageResource message) {

                                        Log.i("notif", "new msg received");

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Request request, int errorCode) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Notification failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        // end msg
                    } else {

                        // Subscribe ressource msg
                        mBbox.subscribeNotification(registerApp, ressource_id, new IBboxSubscribe() {
                            @Override
                            public void onSubscribe() {

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        channelid_subscribe.setText("websocket opened");
                                    }
                                });

                                mBbox.addListener(new IBboxMessage() {
                                    @Override
                                    public void onNewMessage(final MessageResource message) {

                                        Log.i("notif", "new msg received");

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Request request, int errorCode) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Notification failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        // end msg
                    }


                }
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Notification failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}