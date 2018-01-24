package com.johnniesnow.firebasedemoshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathan on 1/24/18.
 */

public class AddListDialogFragment extends BaseDialog implements View.OnClickListener{

    @BindView(R.id.dialog_add_list_editText)
    EditText newListName;

    public static AddListDialogFragment newInstance(){
        return  new AddListDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View rootView = layoutInflater.inflate(R.layout.dialog_add_list, null);


        ButterKnife.bind(this, rootView);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Override
    public void onClick(View v) {
        bus.post(new ShoppingListServices.AddShoppingListRequest(newListName.getText().toString(), userName, userEmail));
    }

    @Subscribe
    public void AddShoppingListName (ShoppingListServices.AddShoppingListResponse response){
        if (!response.didSuceed()){
            newListName.setError(response.getPropertyError("listName"));
        } else {
            dismiss();
        }
    }
}
