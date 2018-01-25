package com.johnniesnow.firebasedemoshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.services.ShoppingListServices;

/**
 * Created by jonathan on 1/24/18.
 */

public class DeleteListDialogFragment extends BaseDialog implements View.OnClickListener {

    public static final String EXTRA_SHOPPING_LIST_ID = "EXTRA_SHOPPING_LIST_ID";
    public static final String EXTRA_BOOLEAN = "EXTRA_BOOLEAN";

    private String mShoppingListId;
    private boolean mIsLongedClicked;


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
            bus.post(new ShoppingListServices.DeleteShoppingListRequest(userEmail, mShoppingListId));
        }
    }


}
