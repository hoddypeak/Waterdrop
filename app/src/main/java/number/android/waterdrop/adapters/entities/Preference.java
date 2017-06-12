package number.android.waterdrop.adapters.entities;

import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by user on 8/31/2016.
 */
public class Preference {
    private int id;
    private IconicsDrawable icon;
    private String name;

    public Preference() {
    }

    public Preference(int id, IconicsDrawable icon, String name) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IconicsDrawable getIcon() {
        return icon;
    }

    public void setIcon(IconicsDrawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
