package com.johnniesnow.firebasedemoshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.entities.User;
import com.johnniesnow.firebasedemoshopping.entities.UsersSharedWith;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.services.GetUsersService;
import com.johnniesnow.firebasedemoshopping.services.ShoppingListServices;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeListNameDialogFragment extends BaseDialog implements View.OnClickListener {

    @BindView(R.id.dialog_change_list_name_editText)
    EditText newListName;


    public static final String SHOPPING_LIST_EXTRA_INFO = "SHOPPING_LIST_EXTRA_INFO";

    private String mShoppingListId;


    private ValueEventListener mShareWIthListener;
    private UsersSharedWith mSharedWith;
    private DatabaseReference mShareWithReference;



    public static ChangeListNameDialogFragment newInstance(ArrayList<String> shoppingListInfo){
        Bundle arguments = new Bundle();
        arguments.putStringArrayList(SHOPPING_LIST_EXTRA_INFO,shoppingListInfo);
        ChangeListNameDialogFragment dialogFragment = new ChangeListNameDialogFragment();
        dialogFragment.setArguments(arguments);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingListId = getArguments().getStringArrayList(SHOPPING_LIST_EXTRA_INFO).get(0);
        mShareWithReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHARED_WITH_REFERENCE + mShoppingListId);
        bus.post(new GetUsersService.GetSharedWithRequest(mShareWithReference));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_list_name,null);

        ButterKnife.bind(this,rootView);

        newListName.setText(getArguments().getStringArrayList(SHOPPING_LIST_EXTRA_INFO).get(1));

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton("Change Name",null)
                .setNegativeButton("Cancel",null)
                .setTitle("Change Shopping List Name?")
                .show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;



    }


    @Override
    public void onClick(View v) {

        //bus.post(new ShoppingListServices.ChangeListNameRequest(newListName.getText().toString(), mShoppingListId, userEmail));

        changeAllShoppingListsName(mSharedWith.getSharedWith(),bus,mShoppingListId,userEmail,newListName.getText().toString());
    }

    @Subscribe
    public void ChangeListName(ShoppingListServices.ChangeListNameResponse response){
        if (!response.didSuceed()){
            newListName.setError(response.getPropertyError("listName"));
        }
        dismiss();
    }

    public static void changeAllShoppingListsName(HashMap<String,User> usersSharedWith, Bus bus, String shoppingListId, String ownerEmail, String newListName){
        if (usersSharedWith !=null && !usersSharedWith.isEmpty()){
            for(User user: usersSharedWith.values()){
                if (usersSharedWith.containsKey(Utils.encodeEmail(user.getEmail()))) {
                    DatabaseReference friendListsReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE +
                                    Utils.encodeEmail(user.getEmail()) + "/" + shoppingListId);

                    bus.post(new ShoppingListServices.ChangeListNameRequest(newListName,shoppingListId,Utils.encodeEmail(user.getEmail())));
                    bus.post(new ShoppingListServices.UpdateShoppingListTimeStampRequest(friendListsReference));
                }
            }
        }
        bus.post(new ShoppingListServices.ChangeListNameRequest(newListName,shoppingListId,ownerEmail));
        DatabaseReference ownerReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + "/" +
                ownerEmail + "/" +shoppingListId);

        bus.post(new ShoppingListServices.UpdateShoppingListTimeStampRequest(ownerReference));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mShareWithReference.removeEventListener(mShareWIthListener);
    }



    @Subscribe
    public void getUsersSharedWith(GetUsersService.GetSharedWithResponse response){
        mShareWIthListener = response.listener;

        if (response.usersSharedWith!=null){
            mSharedWith = response.usersSharedWith;
        } else{
            mSharedWith = new UsersSharedWith();
        }
    }

}


