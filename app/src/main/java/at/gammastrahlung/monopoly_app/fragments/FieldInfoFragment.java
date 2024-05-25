package at.gammastrahlung.monopoly_app.fragments;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textview.MaterialTextView;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.FieldType;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;

public class FieldInfoFragment extends DialogFragment {
    private Field field;

    public FieldInfoFragment(Field field) {
        this.field = field;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView;
        if(field instanceof Property){
            inflatedView = inflater.inflate(R.layout.fragment_propertyinfo, null);
        }else{
            inflatedView = inflater.inflate(R.layout.fragment_fieldinfo, null);
        }

        // Add close button
        builder.setView(inflatedView).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));

        // Add actual field display code here
        MaterialTextView title = inflatedView.findViewById(R.id.fieldName);
        title.setText(field.getName());

        return builder.create();



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;

        if(field instanceof Property){
           view = setUpPropertyInfo(inflater, container, savedInstanceState);
        }else{
            view = inflater.inflate(R.layout.fragment_fieldinfo, container, false);
            TextView fieldName = view.findViewById(R.id.fieldName);
            fieldName.setText(field.getName());
        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    private View setUpPropertyInfo(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        Property property;
        property = (Property) field;

        View view = inflater.inflate(R.layout.fragment_propertyinfo, container, false);
        TextView propertyName = view.findViewById(R.id.property_name);
        TextView currentRent = view.findViewById(R.id.current_rent);
        TextView fullStackRent = view.findViewById(R.id.full_stack_rent);
        TextView houseCount = view.findViewById(R.id.house_count);
        TextView hotelRent = view.findViewById(R.id.hotel_rent);
        TextView hotelCount = view.findViewById(R.id.hotel_count);
        TextView baseRent = view.findViewById(R.id.base_rent);
        TextView oneHouseRent = view.findViewById(R.id.one_house_rent);
        TextView twoHouseRent = view.findViewById(R.id.two_house_rent);
        TextView threeHouseRent = view.findViewById(R.id.three_house_rent);
        TextView fourHouseRent = view.findViewById(R.id.four_house_rent);
        TextView price = view.findViewById(R.id.price);
        TextView owner = view.findViewById(R.id.owner);
        ImageView buildingSlotOne = view.findViewById(R.id.buildingSlot1);
        ImageView buildingSlotTwo = view.findViewById(R.id.buildingSlot2);
        ImageView buildingSlotThree = view.findViewById(R.id.buildingSlot3);
        ImageView buildingSlotFour = view.findViewById(R.id.buildingSlot4);

        propertyName.setText(property.getName());
        owner.setText(getString(R.string.owner) +  property.getOwner().getName());
        fullStackRent.setText(property.getRentPrices().toString());

        return view;

    }
}
