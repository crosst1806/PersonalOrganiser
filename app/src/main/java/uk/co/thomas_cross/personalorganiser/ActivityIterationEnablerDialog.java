package uk.co.thomas_cross.personalorganiser;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivityIteration;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.model.POModel;

/**
 * Created by thomas on 19/03/18.
 */

public class ActivityIterationEnablerDialog extends DialogFragment implements View.OnClickListener {

    private ActivityIteration iteration = null;
    private POModel poModel;


    private TextView descriptionView;

    private Button enableButton;
    private Button disableButton;
    private Button closeButton;


    public ActivityIterationEnablerDialog() {
    }

    public static ActivityIterationEnablerDialog newInstance(ActivityIteration iteration) {

        ActivityIterationEnablerDialog frag = new ActivityIterationEnablerDialog();
        Bundle args = new Bundle();
        args.putSerializable("activityIteration", iteration);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.iteration = (ActivityIteration) getArguments().getSerializable("activityIteration");
        poModel = POModel.getInstance(this.getContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.activity_iteration_enabler, null);

        descriptionView = view.findViewById(R.id.paa_description);

        enableButton = view.findViewById(R.id.ai_enable);
        enableButton.setOnClickListener(this);

        disableButton = view.findViewById(R.id.ai_disable);
        disableButton.setOnClickListener(this);

        closeButton = view.findViewById(R.id.ai_close);
        closeButton.setOnClickListener(this);

        Activity activity = poModel.getActivity(iteration.getActivity());
        descriptionView.setText(activity.getDescription());

        switch (iteration.getStatus()) {
            case ActivityIteration.DISABLED:
                disableButton.setVisibility(View.GONE);
                break;
            case ActivityIteration.ENABLED:
                enableButton.setVisibility(View.GONE);
                break;
        }

        builder.setView(view);

        return builder.create();
    }


    @Override
    public void onClick(View v) {

        if (v == enableButton) {

            // The activity may be pending or paused, in both cases
            // we set the new start date and time to now and update the
            // status to executing

            poModel.enableActivityIteration(iteration);
            iteration.setStatus(ActivityIteration.ENABLED);
            poModel.updateActivityIteration(iteration);
            getDialog().dismiss();

        } else if (v == disableButton) {

            poModel.disableActivityIteration(iteration);
            iteration.setStatus(ActivityIteration.DISABLED);
            poModel.updateActivityIteration(iteration);
            getDialog().dismiss();

        } else if ( v == closeButton ){

            getDialog().dismiss();

        }

    }


}




