package uk.co.thomas_cross.personalorganiser.util;

import android.content.Context;

import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by thomas on 12/04/18.
 */
public class RoleCheckBox extends android.support.v7.widget.AppCompatCheckBox
{

    private Role role = null;

    public RoleCheckBox(Context context) {
        super(context);
    }

    public void setRole(Role role){
        this.role = role;
    }

    public Role getRole(){
        return this.role;
    }

    public String getText(){
        return this.role.getTitle();
    }

}
