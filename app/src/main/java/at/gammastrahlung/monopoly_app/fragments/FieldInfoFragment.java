package at.gammastrahlung.monopoly_app.fragments;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.library.baseAdapters.BR;
import androidx.databinding.Observable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textview.MaterialTextView;

import java.util.Map;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.game.GameData;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;
import at.gammastrahlung.monopoly_app.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_app.game.gameboard.Utility;
import at.gammastrahlung.monopoly_app.network.MonopolyClient;
import at.gammastrahlung.monopoly_app.network.dtos.ServerMessage;


public class FieldInfoFragment extends DialogFragment {
    private Field field;
    private Observable.OnPropertyChangedCallback callback;

    public FieldInfoFragment(Field field) {
        this.field = field;
    }


    @NonNull
    @Nullable
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = null;

        if (field instanceof Property) {
            view = inflater.inflate(R.layout.fragment_propertyinfo, null);
            setUpPropertyInfo(view);
        } else if (field instanceof Utility) {
            view = inflater.inflate(R.layout.fragment_utility_info, null);
            setUpUtilityInfo(view);
        } else if (field instanceof Railroad) {
            view = inflater.inflate(R.layout.fragment_railroad_info, null);
            setUpRailroadInfo(view);
        } else {
            view = inflater.inflate(R.layout.fragment_fieldinfo, null);
            setUpFieldInfo(view);

        }

        builder.setView(view)
                .setNegativeButton(R.string.close, (dialog, which) -> dialog.cancel());

        return builder.create();
    }

    @SuppressLint({"SetTextI18n", "StringFormatInvalid", "ResourceType"})
    private void setUpFieldInfo(View view) {
        ImageView fieldPicture = view.findViewById(R.id.field_picture);
        int fieldPictureValue = getResources().getIdentifier("field_" + field.getFieldId(), "drawable", "at.gammastrahlung.monopoly_app");
        fieldPicture.setImageResource(fieldPictureValue);
        MaterialTextView title = view.findViewById(R.id.fieldName);
        title.setText(field.getName());
    }

    @SuppressLint({"SetTextI18n", "StringFormatInvalid", "ResourceType"})
    private void setUpPropertyInfo(View view) {
        Property property = (Property) field;


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
        buildButton.setEnabled(property.getOwner().equals(GameData.getGameData().getPlayer()));
        buildButton.setOnClickListener(v -> {
            registerPropertyChangedCallback();
            MonopolyClient.getMonopolyClient().buildHouse(property.getFieldId());
        });

    }

    @SuppressLint({"SetTextI18n", "StringFormatInvalid", "ResourceType"})
    private void setUpUtilityInfo(View view) {
        Utility utility = (Utility) field;


        TextView name = view.findViewById(R.id.utility_name);
        TextView rent = view.findViewById(R.id.utility_rent);
        TextView owner = view.findViewById(R.id.utility_owner);
        TextView price = view.findViewById(R.id.utility_price);

        ImageView utilityPicture = view.findViewById(R.id.picture);
        int utilityPictureValue = getResources().getIdentifier("property_" + field.getFieldId(), "drawable", "at.gammastrahlung.monopoly_app");
        utilityPicture.setImageResource(utilityPictureValue);

        name.setText(getString(R.string.utility_name, utility.getName()));
        if(utility.getOwner().getName()==null){
            owner.setText(getString(R.string.utility_owner, "Bank"));
        }else{
            owner.setText(getString(R.string.utility_owner, utility.getOwner().getName()));
        }
        rent.setText(getString(R.string.utility_rent, utility.getToPay()));
        price.setText(getString(R.string.utility_price, utility.getPrice()));

    }

    @SuppressLint({"SetTextI18n", "StringFormatInvalid", "ResourceType"})
    private void setUpRailroadInfo(View view) {
        Railroad railroad = (Railroad) field;

        TextView name = view.findViewById(R.id.railroad_name);
        TextView rent_1 = view.findViewById(R.id.railroad_rent_1);
        TextView rent_2 = view.findViewById(R.id.railroad_rent_2);
        TextView rent_3 = view.findViewById(R.id.railroad_rent_3);
        TextView rent_4 = view.findViewById(R.id.railroad_rent_4);
        TextView owner = view.findViewById(R.id.railroad_owner);
        TextView price = view.findViewById(R.id.railroad_price);

        ImageView railroadPicture = view.findViewById(R.id.railroad_image);
        int railroadPictureValue = getResources().getIdentifier("property_" + field.getFieldId(), "drawable", "at.gammastrahlung.monopoly_app");
        railroadPicture.setImageResource(railroadPictureValue);

        name.setText(getString(R.string.utility_name, railroad.getName()));
        if(railroad.getOwner().getName()==null){
            owner.setText(getString(R.string.utility_owner, "Bank"));
        }else{
            owner.setText(getString(R.string.utility_owner, railroad.getOwner().getName()));
        }

        rent_1.setText(getString(R.string.railroad_rent_1, 25));
        rent_2.setText(getString(R.string.railroad_rent_2, 50));
        rent_3.setText(getString(R.string.railroad_rent_2, 100));
        rent_4.setText(getString(R.string.railroad_rent_2, 200));
        price.setText(getString(R.string.utility_price, railroad.getPrice()));

    }

    private void registerPropertyChangedCallback() {
        callback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.game) {
                    Field newField = GameData.getGameData().getGame().getGameBoard().getFields()[field.getFieldId()];

                    if (field instanceof Property && newField instanceof Property) {
                        Property property = (Property) newField;
                        getActivity().runOnUiThread(() -> {
                            TextView buildMessage = getView().findViewById(R.id.build_message);
                            if(GameData.getGameData().getLastMessageType() == ServerMessage.MessageType.SUCCESS){
                                buildMessage.setText(R.string.successful_build);
                            } else {
                                buildMessage.setText(R.string.failed_build);
                            }
                            setUpPropertyInfo(getView());
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

}

