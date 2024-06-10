package at.gammastrahlung.monopoly_app.fragments;

import androidx.fragment.app.DialogFragment;

public class PurchaseDialogFragment extends DialogFragment {

    public interface PurchaseDialogListener {
        void onNoButtonClicked();
        void onYesButtonClicked();
    }

    private PurchaseDialogListener listener;
}