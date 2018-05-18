package fr.lab.bbox.bboxapirunner.application;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxRegisterApp;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationRegisterFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private EditText appNameEdit;
    private TextView appidReg;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_register, container, false);

        mButton = (Button) view.findViewById(R.id.try_regapp);
        appNameEdit = (EditText) view.findViewById(R.id.app_reg_appname);
        appidReg = (TextView) view.findViewById(R.id.appid_reg);

        mButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(final View v) {
        String appName = appNameEdit.getText().toString();

        mBbox.registerApp(appName, new IBboxRegisterApp() {
            @Override
            public void onResponse(final String appId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Register app success", Toast.LENGTH_SHORT).show();
                        appidReg.setText("appId : " + appId);
                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Register app failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}