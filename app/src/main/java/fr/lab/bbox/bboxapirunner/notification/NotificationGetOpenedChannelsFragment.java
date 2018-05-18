package fr.lab.bbox.bboxapirunner.notification;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetOpenedChannels;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class NotificationGetOpenedChannelsFragment extends Fragment implements View.OnClickListener {

    private List<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private ListView list;
    private Button mButton;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_getopenedchannels, container, false);

        mButton = (Button) view.findViewById(R.id.try_getopenedchannels);
        list = (ListView) view.findViewById(R.id.listViewNotifChannelList);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);

        mButton.setOnClickListener(this);

        return view;
    }


    // TODO : allow channelId copy and paste to make notification easier
    @Override
    public void onClick(final View v) {
        listItems.clear();

        mBbox.getOpenedChannels(new IBboxGetOpenedChannels() {
            @Override
            public void onResponse(final List<String> channels) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get notification channel success", Toast.LENGTH_SHORT).show();
                        listItems.addAll(channels);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get notification channel failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                ClipData myClip = ClipData.newPlainText("copy", listItems.get(pos));
                ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

}