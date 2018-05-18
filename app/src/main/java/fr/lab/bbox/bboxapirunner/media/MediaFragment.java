package fr.lab.bbox.bboxapirunner.media;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import fr.lab.bbox.bboxapirunner.R;

/**
 * Created by dinh on 01/07/16.
 */
public class MediaFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_media, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_media);
        spinner.setOnItemSelectedListener(this);

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        String item = parent.getItemAtPosition(position).toString();

        switch (item) {
            case "Get current program":
                fragmentTransaction.replace(R.id.container_media_parameter, new MediaGetCurrentProgramFragment());
                break;

            case "Get TV channel list":
                fragmentTransaction.replace(R.id.container_media_parameter, new MediaGetChannelListFragment());
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