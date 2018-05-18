package fr.lab.bbox.bboxapirunner.application;

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
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxStartApplication;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationStartInstallFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Button mButtonStartDemo;

    private EditText packageNameEdit;

    private Bbox mBbox = Bbox.getInstance(false);

    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_start_install, container, false);

        mButton = (Button) view.findViewById(R.id.try_startapp);
        mButtonStartDemo = (Button) view.findViewById(R.id.try_demo);
        packageNameEdit = (EditText) view.findViewById(R.id.app_start_packagename);

        mButton.setOnClickListener(this);
        mButtonStartDemo.setOnClickListener(this);

        return view;
    }

    private void startApp(String packageName, String deepLink) {
        mBbox.startApp(packageName, deepLink,
                new IBboxStartApplication() {

                    @Override
                    public void onResponse() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Start app success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Start app failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (mButton == v) {
            String packageName = packageNameEdit.getText().toString();
            startApp("com.android.vending", "https://play.google.com/store/apps/details?id=" + packageName);
        }
        else if (mButtonStartDemo == v) {
            startApp("com.android.vending", "https://play.google.com/store/apps/details?id=fr.bouyguestelecom.tv.ifttt");
        }
    }
}