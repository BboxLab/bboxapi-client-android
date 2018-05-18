package fr.lab.bbox.bboxapirunner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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

/**
 * Created by dinh on 01/07/16.
 */
public class BboxFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = BboxFragment.class.getSimpleName();

    private Button mSearchBtn;
    private Button mSaveBtn;
    private EditText mBboxIp;
    private TextView mCurrentIp;

    private SharedPreferences mSharedPreferences;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bbox, container, false);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mCurrentIp = (TextView) view.findViewById(R.id.currentip);
        mSearchBtn = view.findViewById(R.id.searchBtn);
        mSaveBtn = view.findViewById(R.id.saveBtn);
        mBboxIp = view.findViewById(R.id.bboxip);

        mCurrentIp.setText(mSharedPreferences.getString("bboxip", ""));

        mSearchBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mSearchBtn) {
            Bbox.discoverBboxApi(getActivity(), getString(R.string.APP_ID), getString(R.string.APP_SECRET), new Bbox.DiscoveryListener() {
                @Override
                public void bboxFound(final Bbox bbox) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCurrentIp.setText(bbox.getIp());
                        }
                    });
                }
            });
        } else if (v == mSaveBtn) {
            final String bboxip = mBboxIp.getText().toString();

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("bboxip", bboxip);
            editor.apply();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCurrentIp.setText(bboxip);
                    Toast.makeText(getActivity(), "Bbox IP address saved", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}