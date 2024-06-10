package at.gammastrahlung.monopoly_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PurchaseDialogFragment extends DialogFragment {

    public interface PurchaseDialogListener {
        void onNoButtonClicked();

        void onYesButtonClicked();
    }

    private PurchaseDialogListener listener;

    public static PurchaseDialogFragment newInstance(String propertyName, int propertyId) {
        PurchaseDialogFragment fragment = new PurchaseDialogFragment();
        Bundle args = new Bundle();
        args.putString("property_name", propertyName);
        args.putInt("property_id", propertyId);
        fragment.setArguments(args);
        return fragment;
    }
public void setPurchaseDialogListener(PurchaseDialogListener listener) {
    this.listener = listener;
}







}