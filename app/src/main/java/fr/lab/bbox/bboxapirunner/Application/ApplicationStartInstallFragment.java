package fr.lab.bbox.bboxapirunner.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;
import tv.bouyguestelecom.fr.bboxapilibrary.Bbox;
import tv.bouyguestelecom.fr.bboxapilibrary.callback.IBboxStartApplication;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationStartInstallFragment extends Fragment {

    private Button mButton;
    private Button mButtonStartDemo;

    private Context ctxt;
    private Handler handler;
    private EditText packageNameEdit;
    private String packageName = " ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_start_install, container, false);

        mButton = (Button) view.findViewById(R.id.try_startapp);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                String ip = sharedPref.getString("bboxip", "");
                packageName = packageNameEdit.getText().toString();


                Bbox.getInstance().startApp(ip,
                        getResources().getString(R.string.APP_ID),
                        getResources().getString(R.string.APP_SECRET),
                        "com.android.vending",
                        "https://play.google.com/store/apps/details?id=" + packageName,
                        new IBboxStartApplication() {

                            @Override
                            public void onResponse() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctxt, "Start app success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Request request, int errorCode) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctxt, "Start app failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

            }
        });

        mButtonStartDemo = (Button) view.findViewById(R.id.try_demo);
        mButtonStartDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                String ip = sharedPref.getString("bboxip", "");
                Bbox.getInstance().startApp(ip,
                        getResources().getString(R.string.APP_ID),
                        getResources().getString(R.string.APP_SECRET),
                        "com.android.vending",
                        "https://play.google.com/store/apps/details?id=fr.bouyguestelecom.tv.ifttt",
                        new IBboxStartApplication() {

                            @Override
                            public void onResponse() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctxt, "Start app success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Request request, int errorCode) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctxt, "Start app failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
        });

        packageNameEdit = (EditText) view.findViewById(R.id.app_start_packagename);
        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        return view;
    }
}