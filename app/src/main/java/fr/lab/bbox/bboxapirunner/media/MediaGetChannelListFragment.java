package fr.lab.bbox.bboxapirunner.media;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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

import fr.bouyguestelecom.tv.bboxapi.BboxCloud;
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetChannels;
import fr.bouyguestelecom.tv.bboxapi.model.Channel;
import fr.bouyguestelecom.tv.bboxapi.model.ChannelProfil;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class MediaGetChannelListFragment extends Fragment implements View.OnClickListener {

    private List<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private ListView list;
    private Button mButton;

    private Handler handler = new Handler();

    private BboxCloud mBboxCloud = BboxCloud.getInstance(getString(R.string.APP_ID), getString(R.string.APP_SECRET));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_media_getchannellist, container, false);

        mButton = (Button) view.findViewById(R.id.try_getchannellist);
        list = (ListView) view.findViewById(R.id.listViewChannelList);

        mButton.setOnClickListener(this);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);

        return view;
    }


    @Override
    public void onClick(final View v) {
        listItems.clear();

        mBboxCloud.getChannels(ChannelProfil.ALL, new IBboxGetChannels() {
            @Override
            public void onResponse(final List<Channel> channels) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get TV channel list success", Toast.LENGTH_SHORT).show();

                        for (Channel channel : channels) {
                            listItems.add("mediaTitle : " + channel.getName() + "\n" +
                                    "mediaState : " + channel.getMediaState() + "\n" +
                                    "positionId : " + channel.getPositionId()
                            );
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Request request, int errorCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get TV channel list failed", Toast.LENGTH_SHORT).show();
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