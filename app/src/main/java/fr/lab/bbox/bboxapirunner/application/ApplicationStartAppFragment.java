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
public class ApplicationStartAppFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private EditText packageNameEdit;
    private EditText deeplinkEdit;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_start, container, false);

        mButton = (Button) view.findViewById(R.id.try_startapp);
        packageNameEdit = (EditText) view.findViewById(R.id.app_start_packagename);
        deeplinkEdit = (EditText) view.findViewById(R.id.app_start_deeplink);

        mButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(final View v) {
        String packageName = packageNameEdit.getText().toString();
        String deeplink = deeplinkEdit.getText().toString();

        if (deeplink.isEmpty()) {
            mBbox.startApp(packageName, new IBboxStartApplication() {
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
        } else {
            mBbox.startApp(packageName, deeplink, new IBboxStartApplication() {
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
    }
}