package fr.lab.bbox.bboxapirunner.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ApplicationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_application, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_app);
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        String item = parent.getItemAtPosition(position).toString();

        switch (item) {
            case "Get application info":
                mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationGetAppInfoFragment());
                break;

            case "Get application list":
                mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationGetAppListFragment());
                break;

            case "Get application icon":
                mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationGetAppIconFragment());
                break;

            case "Start application":
                mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationStartAppFragment());
                break;

            case "Stop application":
                mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationStopAppFragment());
                break;

            case "Register (for notification)":
                mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationRegisterFragment());
                break;

            case "Start Install":
                mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationStartInstallFragment());
                break;

            default:
                break;
        }

        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}