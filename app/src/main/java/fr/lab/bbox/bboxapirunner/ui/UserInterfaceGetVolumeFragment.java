package fr.lab.bbox.bboxapirunner.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetVolume;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class UserInterfaceGetVolumeFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_userinterface_getvolume, container, false);

        mButton = (Button) view.findViewById(R.id.try_getvolume);
        mButton.setOnClickListener(this);

        handler = new Handler();

        return view;
    }


    @Override
    public void onClick(final View v) {
        Bbox.getInstance(false).getVolume(new IBboxGetVolume() {
            @Override
            public void onResponse(final String volume) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), volume, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get volume failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}