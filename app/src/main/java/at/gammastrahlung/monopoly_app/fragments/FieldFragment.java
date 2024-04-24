package at.gammastrahlung.monopoly_app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import at.gammastrahlung.monopoly_app.R;

public class FieldFragment extends Fragment {

    // Color bar positions
    public static final int COLOR_BAR_TOP = 0;
    public static final int COLOR_BAR_BOTTOM = 1;
    public static final int COLOR_BAR_LEFT = 2;
    public static final int COLOR_BAR_RIGHT = 3;
    public static final int COLOR_BAR_NONE = 4; // Used to prevent using a number that is in use

    public FieldFragment() {
        super(R.layout.fragment_field);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setColorBarLocation(requireArguments().getInt("color_bar"));
        setFieldTitle(requireArguments().getString("title"));
        setPlayers(requireArguments().getIntArray("players"));
    }

    private void setColorBarLocation(int location) {
        Activity a = getActivity();
        if (a == null)
            return;

        switch (location) {
            case COLOR_BAR_TOP:
                a.findViewById(R.id.colorBar_top).setVisibility(View.VISIBLE);
                break;
            case COLOR_BAR_BOTTOM:
                a.findViewById(R.id.colorBar_bottom).setVisibility(View.VISIBLE);
                break;
            case COLOR_BAR_LEFT:
                a.findViewById(R.id.colorBar_left).setVisibility(View.VISIBLE);
                break;
            case COLOR_BAR_RIGHT:
                a.findViewById(R.id.colorBar_right).setVisibility(View.VISIBLE);
                break;
            default:
                break; // No color bar
        }
    }

    private void setFieldTitle(String title) {
        Activity a = getActivity();
        if (a == null)
            return;

        ((TextView) a.findViewById(R.id.field_title)).setText(title);
    }

    private void setPlayers(int[] players) {

    }
}
