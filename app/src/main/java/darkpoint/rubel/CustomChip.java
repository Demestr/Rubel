package darkpoint.rubel;

import android.content.Context;

import com.google.android.material.chip.Chip;

public class CustomChip extends Chip {
    private int SELECTED_ID;

    public int getSELECTED_ID() {
        return SELECTED_ID;
    }

    public void setSELECTED_ID(int SELECTED_ID) {
        this.SELECTED_ID = SELECTED_ID;
    }

    public CustomChip(Context context) {
        super(context);
    }
}
