package at.gammastrahlung.monopoly_app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import at.gammastrahlung.monopoly_app.R;

public class FieldFragment extends Fragment {

    // Color bar positions
    public static final int COLOR_BAR_TOP = 0;
    public static final int COLOR_BAR_BOTTOM = 1;
    public static final int COLOR_BAR_LEFT = 2;
    public static final int COLOR_BAR_RIGHT = 3;
    public static final int COLOR_BAR_NONE = 4; // Used to prevent using a number that is in use

    private View inflatedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_field, container, false);
        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setColorBar(requireArguments().getInt("color_bar_position"), requireArguments().getString("color_bar_color"));
        setFieldTitle(requireArguments().getString("title"));
        setPlayers(requireArguments().getIntArray("players"), requireArguments().getInt("orientation"));
    }

    private void setColorBar(int location, String color) {
        View v = null;

        switch (location) {
            case COLOR_BAR_TOP:
                v = inflatedView.findViewById(R.id.colorBar_top);
                break;
            case COLOR_BAR_BOTTOM:
                v = inflatedView.findViewById(R.id.colorBar_bottom);
                break;
            case COLOR_BAR_LEFT:
                v = inflatedView.findViewById(R.id.colorBar_left);
                break;
            case COLOR_BAR_RIGHT:
                v = inflatedView.findViewById(R.id.colorBar_right);
                break;
            default:
                break; // No color bar
        }

        if (v != null) {
            v.setVisibility(View.VISIBLE);
            v.setBackgroundColor(Color.parseColor(color));
        }
    }

    private void setFieldTitle(String title) {
        ((TextView) inflatedView.findViewById(R.id.field_title)).setText(title);
    }

    public void setPlayers(int[] players, int fieldRowOrientation) {
        LinearLayout playerIcons = inflatedView.findViewById(R.id.playerIcons);
        playerIcons.removeAllViews();

        if (players == null)
            return;

        // depending on field location, players will be placed horizontally or vertically
        playerIcons.setOrientation(fieldRowOrientation);

        for (int playerId : players) {
            if (playerId < 1 || playerId > 8)
                return; // Only players 1-8 are valid

            ImageView iv = new ImageView(getActivity());
            int iconId = getResources().getIdentifier("playericon_" + playerId, "drawable", getActivity().getPackageName());

            iv.setImageDrawable(AppCompatResources.getDrawable(getActivity(), iconId));
            iv.setLayoutParams(new LinearLayout.LayoutParams(22, 22));

            playerIcons.addView(iv);
        }
    }
}
