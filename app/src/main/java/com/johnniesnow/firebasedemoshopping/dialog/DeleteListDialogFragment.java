package com.johnniesnow.firebasedemoshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonathan on 1/24/18.
 */

public class DeleteListDialogFragment extends BaseDialog implements View.OnClickListener {

    public static final String EXTRA_SHOPPING_LIST_ID = "EXTRA_SHOPPING_LIST_ID";
    public static final String EXTRA_BOOLEAN = "EXTRA_BOOLEAN";

    private String mShoppingListId;
    private boolean mIsLongedClicked;

    private ValueEventListener mShareWIthListener;
    private UsersSharedWith mSharedWith;
    private DatabaseReference mShareWithReference;


    public static DeleteListDialogFragment newInstance (String shoppingListId, boolean isLongClicked) {

        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_SHOPPING_LIST_ID, shoppingListId);
        arguments.putBoolean(EXTRA_BOOLEAN, isLongClicked);

        DeleteListDialogFragment dialogFragment = new DeleteListDialogFragment();
        dialogFragment.setArguments(arguments);

        return dialogFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingListId = getArguments().getString(EXTRA_SHOPPING_LIST_ID);
        mIsLongedClicked = getArguments().getBoolean(EXTRA_BOOLEAN);

        mShareWithReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHARED_WITH_REFERENCE + mShoppingListId);
        bus.post(new GetUsersService.GetSharedWithRequest(mShareWithReference));

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_list,null))
                .setPositiveButton("Confirm",null)
                .setNegativeButton("Cancel",null)
                .setTitle("Delete shopping list?")
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onClick(View view) {
        if (mIsLongedClicked){
            dismiss();
            deleteAllShoppingLists(mSharedWith.getSharedWith(), bus, mShoppingListId,userEmail);
        } else{
            dismiss();
            getActivity().finish();
            deleteAllShoppingLists(mSharedWith.getSharedWith(), bus, mShoppingListId,userEmail);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mShareWithReference.removeEventListener(mShareWIthListener);
    }

    public static void deleteAllShoppingLists(HashMap<String,User> usersSharedWith, Bus bus, String shoppingListId, String ownerEmail){

        if (usersSharedWith != null && !usersSharedWith.isEmpty()){
            for(User user: usersSharedWith.values()){
                if (usersSharedWith.containsKey(Utils.encodeEmail(user.getEmail()))) {

                    DatabaseReference friendListsReference = FirebaseDatabase.getInstance().getReferenceFromUrl(
                            Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE +
                                    Utils.encodeEmail(user.getEmail()) + "/" + shoppingListId);

                    Map newListData = new HashMap();
                    newListData.put("listName","CListIsAboutToGetDeleted");
                    friendListsReference.updateChildren(newListData);
                    friendListsReference.removeValue();

                }
            }
        }

        bus.post(new ShoppingListServices.DeleteShoppingListRequest(ownerEmail,shoppingListId));
    }


}
