package at.gammastrahlung.monopoly_app.fragments;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Observable.OnPropertyChangedCallback;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textview.MaterialTextView;

import java.util.Map;
import java.util.Observable;

import javax.security.auth.callback.Callback;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;


public class FieldInfoFragment extends DialogFragment {
    private Field field;
    //private Observable.OnPropertyChangedCallback callback;

    public FieldInfoFragment(Field field) {
        this.field = field;
    }


    @NonNull
    @Nullable
    public <container> Dialog onCreateDialog(@Nullable Bundle savedInstanceOf, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View inflatedView;
        if(field instanceof Property){
            inflatedView = inflater.inflate(R.layout.fragment_propertyinfo, null);
            View view = setUpPropertyInfo(inflater, container, savedInstanceState);
            builder.setView(view).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));

        }else{
            inflatedView = inflater.inflate(R.layout.fragment_fieldinfo, null);
            MaterialTextView title = inflatedView.findViewById(R.id.fieldName);
            title.setText(field.getName());
        }



        // Add close button
        builder.setView(inflatedView).setNegativeButton(R.string.close, ((dialog, which) -> dialog.cancel()));
        return builder.create();



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;

        if(field instanceof Property){
            view = setUpPropertyInfo(inflater, container, savedInstanceState);
        }else {
            view = inflater.inflate(R.layout.fragment_fieldinfo, container, false);
            TextView fieldName = view.findViewById(R.id.fieldName);
            fieldName.setText(field.getName());
        }

        return view;
    }

    @SuppressLint({"SetTextI18n", "StringFormatInvalid", "ResourceType"})
    private View setUpPropertyInfo(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        Property property;
        property = (Property) field;

        View view = inflater.inflate(R.layout.fragment_propertyinfo, container, false);

        ImageView propertyPicture = view.findViewById(R.id.propertyPicture);
        int propertyPictureValue = getResources().getIdentifier("property_" + field.getFieldId(), "drawable", "at.gammastrahlung.monopoly_app");
        propertyPicture.setImageResource(propertyPictureValue);

        TextView propertyName = view.findViewById(R.id.property_name);
        TextView currentRent = view.findViewById(R.id.current_rent);
        TextView fullSetRent = view.findViewById(R.id.full_set_rent);
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
        int currentRentString;
        Map<Object, Integer> rentPrices;
        rentPrices = property.getRentPrices();

        if (property.getHouseCount() < 4) {
            currentRentString = rentPrices.get(String.valueOf(property.getHouseCount()));
            houseCount.setText(getString(R.string.house_count, property.getHouseCount()));
            hotelCount.setText(getString(R.string.hotel_count, 0));
        }else {
            currentRentString = rentPrices.get("hotel");
            houseCount.setText(getString(R.string.house_count, 0));
            hotelCount.setText(getString(R.string.hotel_count, 1));
        }

        propertyName.setText(getString(R.string.property_name, property.getName()));
        if(property.getOwner().getName()==null){
            owner.setText(getString(R.string.owner, "Bank"));
        }else{
        owner.setText(getString(R.string.owner, property.getOwner().getName()));
        }
        currentRent.setText(getString(R.string.current_rent,currentRentString));
        fullSetRent.setText(getString(R.string.rent_full_set, rentPrices.get("full_set")));
        hotelRent.setText(getString(R.string.hotel_rent, rentPrices.get("hotel")));
        baseRent.setText(getString(R.string.base_rent, rentPrices.get("0")));
        oneHouseRent.setText(getString(R.string.one_house_rent, rentPrices.get("1")));
        twoHouseRent.setText(getString(R.string.two_houses_rent, rentPrices.get("2")));
        threeHouseRent.setText(getString(R.string.three_house_rent, rentPrices.get("3")));
        fourHouseRent.setText(getString(R.string.four_houses_rent, rentPrices.get("4")));
        price.setText(getString(R.string.price, property.getPrice()));

        if(property.getHouseCount()>0){
            buildingSlotOne.setImageResource(R.drawable.house);
            if(property.getHouseCount()>4){
                buildingSlotOne.setImageResource(0);
                buildingSlotTwo.setImageResource(0);
                buildingSlotThree.setImageResource(0);
                buildingSlotFour.setImageResource(R.drawable.hotel);
            } else if(property.getHouseCount()>1){
                    buildingSlotTwo.setImageResource(R.drawable.house);
                    if(property.getHouseCount()>2){
                        buildingSlotThree.setImageResource(R.drawable.house);
                            if(property.getHouseCount()>3){
                                buildingSlotFour.setImageResource(R.drawable.house);

                        }
                    }
                }
            }

        Button buildButton = view.findViewById(R.id.build_button);
        buildButton.setOnClickListener(v -> {
            MonopolyClient.getMonopolyClient().buildHouse(property.getFieldId());
            TextView buildMessage = view.findViewById(R.id.build_message);
            if(GameData.getGameData().getLastMessageType() == ServerMessage.MessageType.SUCCESS){
                buildMessage.setText(R.string.successful_build);
                setUpPropertyInfo(inflater, container, savedInstanceState);  // Update UI after building
            } else {
                buildMessage.setText(R.string.failed_build);
            }
        });

        //registerPropertyChangedCallback();

        return view;
    }

    /*private void registerPropertyChangedCallback() {
        callback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.game) {
                    Field newField = GameData.getGameData().getGame().getGameBoard().getFields()[field.getFieldId()];

                    if (field instanceof Property && newField instanceof Property) {
                        Property property = (Property) newField;
                        getActivity().runOnUiThread(() -> {
                            TextView buildMessage = getView().findViewById(R.id.build_message);
                            buildMessage.setText(R.string.successful_build);
                            setUpPropertyInfo(getLayoutInflater(), null, null);
                        });
                    }
                }
            }
        };
        GameData.getGameData().addOnPropertyChangedCallback(callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (callback != null) {
            GameData.getGameData().removeOnPropertyChangedCallback(callback);
        }
    }
   /*@Override
    public void onDestroyView(){
        GameData.getGameData().removeOnPropertyChangedCallback(callback);
        super.onDestroyView();
    }

    Observable.OnPropertyChangedCallback callback = new androidx.databinding.Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {

            public void onPropertyChanged;(Observable sender, int propertyId){
                if (propertyId == BR.game) {
                    Field newField = GameData.getGameData().getGame().getGameBoard().getFields()[field.getFieldId()];

                    if(field instanceof Property){
                        if (newField instanceof Property) {
                            Property property = (Property) newField;
                            Property oldProperty = (Property) field;

                            getActivity().runOnUiThread(() -> {
                                MonopolyClient.getMonopolyClient().buildHouse(property.getFieldId());
                                TextView buildMessage = view.findViewById(R.id.build_message);
                            });
                        }
                    }
                }

            }
        }
    }

            Observable.OnPropertyChangedCallback callback = new androidx.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {

                public void onPropertyChanged;(Observable sender, int propertyId){
                    if (propertyId == BR.game) {
                        Field newField = GameData.getGameData().getGame().getGameBoard().getFields()[field.getFieldId()];

                        if(field instanceof Property){
                            if (newField instanceof Property) {
                                Property property = (Property) newField;
                                Property oldProperty = (Property) field;

                                getActivity().runOnUiThread(() -> {
                                    MonopolyClient.getMonopolyClient().buildHouse(property.getFieldId());
                                    TextView buildMessage = view.findViewById(R.id.build_message);
                                });
                            }
                        }
                    }

                }
            }
        };*/
}

