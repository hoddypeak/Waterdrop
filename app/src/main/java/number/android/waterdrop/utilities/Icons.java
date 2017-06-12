package number.android.waterdrop.utilities;

import android.content.Context;
import android.graphics.Color;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import number.android.waterdrop.R;

/**
 * Created by Pandiyarajan
 * Number
 * Waterdop
 */
public class Icons {

    Context context;

    private static final String pimaryColor = "#2FA8EC";
    private static final String white = "#FFFFFF";

    public Icons(Context context) {
        this.context = context;
    }

    public IconicsDrawable tickIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_check)
                .color(Color.WHITE)
                .sizeDp(24);
    }

    public IconicsDrawable plusIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_plus)
                .color(Color.WHITE)
                .sizeDp(20);
    }

    public IconicsDrawable textCommentIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_comment_text)
                .color(Color.GRAY)
                .sizeDp(20);
    }

    public IconicsDrawable personIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_account)
                .color(Color.parseColor(pimaryColor))
                .sizeDp(14);
    }

    public IconicsDrawable mobileIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_phone)
                .color(Color.parseColor(pimaryColor))
                .sizeDp(14);
    }

    public IconicsDrawable homeIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_home)
                .color(Color.parseColor(pimaryColor))
                .sizeDp(14);
    }

    public IconicsDrawable arrowRightIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_arrow_right)
                .color(Color.parseColor(white))
                .sizeDp(14);
    }

    public IconicsDrawable settingsIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_settings)
                .color(Color.parseColor(white))
                .sizeDp(14);
    }

    public int waterDropActiveIcon(){
        return R.drawable.drop_small_hightlight;
    }

    public int waterDropInActiveIcon(){
        return R.drawable.drop_small;
    }

    public IconicsDrawable receiptIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_receipt)
                .color(Color.parseColor(white))
                .sizeDp(14);
    }

    public IconicsDrawable personChangeIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_rotate_right)
                .color(Color.parseColor(pimaryColor))
                .sizeDp(12);
    }

    public IconicsDrawable shareIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_share)
                .color(Color.parseColor(pimaryColor))
                .sizeDp(12);
    }

    public IconicsDrawable trashIcon(){
        return new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_delete)
                .color(Color.parseColor(pimaryColor))
                .sizeDp(12);
    }

}
