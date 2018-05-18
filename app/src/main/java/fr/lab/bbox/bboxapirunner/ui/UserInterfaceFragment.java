package fr.lab.bbox.bboxapirunner.ui;

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
public class UserInterfaceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_userinterface, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_userinterface);
        spinner.setOnItemSelectedListener(this);

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch (item) {
            case "Get volume":
                fragmentTransaction.replace(R.id.container_userinterface_parameter, new UserInterfaceGetVolumeFragment());
                break;

            case "Set volume":
                fragmentTransaction.replace(R.id.container_userinterface_parameter, new UserInterfaceSetVolumeFragment());
                break;

            case "Display toast":
                fragmentTransaction.replace(R.id.container_userinterface_parameter, new UserInterfaceDisplayToast());
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