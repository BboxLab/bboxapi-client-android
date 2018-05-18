package fr.lab.bbox.bboxapirunner.notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import fr.bouyguestelecom.tv.bboxapi.Bbox;
import fr.lab.bbox.bboxapirunner.R;

/**
 * Created by dinh on 01/07/16.
 */
public class NotificationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Bbox mBbox = Bbox.getInstance(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_notif);
        spinner.setOnItemSelectedListener(this);

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch (item) {
            case "Subscribe to resources":
                fragmentTransaction.replace(R.id.container_notif_parameter, new NotificationSubscribeFragment());
                break;

            case "Unsubscribe":
                fragmentTransaction.replace(R.id.container_notif_parameter, new NotificationUnsubscribeFragment());
                break;

            case "Get opened channels":
                fragmentTransaction.replace(R.id.container_notif_parameter, new NotificationGetOpenedChannelsFragment());
                break;

            case "Send message to room":
                fragmentTransaction.replace(R.id.container_notif_parameter, new NotificationSendMsgToRoomFragment());
                break;

            case "Send message to a specific channelId":
                fragmentTransaction.replace(R.id.container_notif_parameter, new NotificationSendMsgToChannelIdFragment());
                break;

            case "Create Notification":
                fragmentTransaction.replace(R.id.container_notif_parameter, new NotificationCreateNotificationFragment());
                break;

            default:
                break;
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}