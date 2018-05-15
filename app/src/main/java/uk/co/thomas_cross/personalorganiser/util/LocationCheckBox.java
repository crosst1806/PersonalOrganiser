package uk.co.thomas_cross.personalorganiser.util;

import android.content.Context;

import uk.co.thomas_cross.personalorganiser.entities.Location;

/**
 * Created by thomas on 12/04/18.
 */
public class LocationCheckBox extends android.support.v7.widget.AppCompatCheckBox
{

    private Location location = null;

    public LocationCheckBox(Context context) {
        super(context);
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public Location getLocation(){
        return this.location;
    }

    public String getText(){
        return this.location.getTitle();
    }

}
