package fr.lab.bbox.bboxapirunner.application;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetApplicationIcon;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationGetAppIconFragment extends Fragment implements View.OnClickListener {

    private EditText packageNameEdit;
    private ImageView img;
    private Button mButton;

    private String packageName = " ";

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_getappicon, container, false);

        mButton = (Button) view.findViewById(R.id.try_getappicon);
        mButton.setOnClickListener(this);
        img = (ImageView) view.findViewById(R.id.imageView_logo);
        packageNameEdit = (EditText) view.findViewById(R.id.app_icon_packagename);

        return view;
    }


    @Override
    public void onClick(final View v) {
        packageName = packageNameEdit.getText().toString();

        mBbox.getAppIcon(packageName, new IBboxGetApplicationIcon() {
            @Override
            public void onResponse(final Bitmap bmp) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get app icon success", Toast.LENGTH_SHORT).show();
                        img.setImageBitmap(bmp);

                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get app icon failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}