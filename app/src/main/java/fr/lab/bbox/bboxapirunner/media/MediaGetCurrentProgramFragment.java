package fr.lab.bbox.bboxapirunner.media;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetCurrentChannel;
import fr.bouyguestelecom.tv.bboxapi.model.Channel;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class MediaGetCurrentProgramFragment extends Fragment implements View.OnClickListener {

    private Button mButton;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_media_getcurrentprogram, container, false);

        mButton = (Button) view.findViewById(R.id.try_getcurrentprogram);
        mButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(final View v) {
        mBbox.getCurrentChannel(new IBboxGetCurrentChannel() {
            @Override
            public void onResponse(final Channel channel) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "mediaState : " + channel.getMediaState() + "\n" +
                                        "mediaTitle : " + channel.getName() + "\n" +
                                        "positionId : " + channel.getPositionId()
                                , Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get current channel failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}