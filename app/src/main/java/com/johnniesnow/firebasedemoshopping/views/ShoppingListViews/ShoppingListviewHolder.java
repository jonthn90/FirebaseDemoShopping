package com.johnniesnow.firebasedemoshopping.views.ShoppingListViews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.johnniesnow.firebasedemoshopping.R;
import com.johnniesnow.firebasedemoshopping.entities.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathan on 1/24/18.
 */

public class ShoppingListviewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_shopping_list_listOwnerName)
    TextView ownerName;

    @BindView(R.id.list_shopping_list_listName)
    TextView listName;

    @BindView(R.id.list_shopping_list_dateCreated)
    TextView dateCreated;

    @BindView(R.id.list_shopping_list_layout)
    public View layout;

    public ShoppingListviewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);


    }

    public void populate(ShoppingList shoppingList){
        ownerName.setText(shoppingList.getOwnerName());
        listName.setText(shoppingList.getListName());

        if (shoppingList.getDateCreated().get("timestamp") !=null){
            dateCreated.setText(convertTime((long)shoppingList.getDateCreated().get("timestamp")));
        }


    }

    private String convertTime(Long unixTime){
        Date dateObject = new Date(unixTime);
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-mm-yy kk:mm");
        return simpleDateFormat.format(dateObject);
    }
}
