package fr.lab.bbox.bboxapirunner.application;

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
import fr.bouyguestelecom.tv.bboxapi.callback.IBboxGetApplications;
import fr.bouyguestelecom.tv.bboxapi.model.Application;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationGetAppListFragment extends Fragment implements View.OnClickListener {

    private List<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private ListView list;
    private Button mButton;
    private Button mButtonBis;

    private Handler handler = new Handler();

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_getapplist, container, false);

        mButton = (Button) view.findViewById(R.id.try_getapplist);
        mButtonBis = (Button) view.findViewById(R.id.try_getapplist2);

        mButton.setOnClickListener(this);
        mButtonBis.setOnClickListener(this);

        list = (ListView) view.findViewById(R.id.listViewAppList);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(final View v) {
        listItems.clear();

        mBbox.getApps(new IBboxGetApplications() {
            @Override
            public void onResponse(final List<Application> applications) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Get app list success", Toast.LENGTH_SHORT).show();
                        if (mButton == v) {
                            for (Application application : applications) {
                                listItems.add("appId : " + application.getAppId() + "\n" +
                                        "appName : " + application.getAppName() + "\n" +
                                        "appState : " + application.getAppState() + "\n" +
                                        "component : " + application.getComponent() + "\n" +
                                        "data : " + application.getData() + "\n" +
                                        "leanback : " + application.isLeanback() + "\n" +
                                        "logoUrl : " + application.getUrlLogo() + "\n" +
                                        "packageName : " + application.getPackageName()
                                );
                            }
                        }
                        else if (mButtonBis == v) {
                            for (Application application : applications) {
                                listItems.add(application.getPackageName());
                            }
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
                        Toast.makeText(getActivity(), "Get app list failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                ClipData myClip = ClipData.newPlainText("copy", listItems.get(pos));
                ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }
}