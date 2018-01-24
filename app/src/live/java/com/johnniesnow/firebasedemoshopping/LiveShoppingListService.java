package com.johnniesnow.firebasedemoshopping;

import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.johnniesnow.firebasedemoshopping.entities.ShoppingList;
import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

/**
 * Created by jonathan on 1/24/18.
 */

public class LiveShoppingListService extends BaseLiveService {

    public LiveShoppingListService(FirebaseDemoShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void AddShoppingList (ShoppingListServices.AddShoppingListRequest request){

        ShoppingListServices.AddShoppingListResponse response = new ShoppingListServices.AddShoppingListResponse();


        if (request.shoppingListName.isEmpty()){
            response.setPropertyErrors("listName","Shopping list must have a name");
        }

        if (response.didSuceed()){

            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIRE_BASE_USER_SHOPPING_LIST_REFERENCE + request.ownerEmail).push();

            HashMap<String, Object> timestampedCreated = new HashMap<>();
            timestampedCreated.put("timestamp", ServerValue.TIMESTAMP);

            ShoppingList shoppingList = new ShoppingList(reference.getKey(),request.shoppingListName,
                    Utils.decodeEmail(request.ownerEmail), request.ownerName, timestampedCreated);


            reference.child("id").setValue(shoppingList.getId());
            reference.child("listName").setValue(shoppingList.getListName());
            reference.child("ownerEmail").setValue(shoppingList.getOwnerEmail());
            reference.child("ownerName").setValue(shoppingList.getOwnerName());
            reference.child("dateCreated").setValue(shoppingList.getDateCreated());
            reference.child("dateLastChanged").setValue(shoppingList.getDateLastChanged());


            Toast.makeText(application.getApplicationContext(), "List created", Toast.LENGTH_LONG).show();
        }

        bus.post(response);

    }
}

