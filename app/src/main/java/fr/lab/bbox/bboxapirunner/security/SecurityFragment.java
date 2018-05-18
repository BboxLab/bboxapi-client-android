package fr.lab.bbox.bboxapirunner.security;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetSessionId;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class SecurityFragment extends Fragment {

    private TextView sessionid;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_security, container, false);

        sessionid = (TextView) view.findViewById(R.id.sessionid);

        mBbox.getSessionId(new IBboxGetSessionId() {
            @Override
            public void onResponse(final String sessionId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sessionid.setText(sessionId);
                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sessionid.setText("Authentication problem");
                    }
                });
            }
        });

        return view;
    }
}