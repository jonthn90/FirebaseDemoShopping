package com.johnniesnow.firebasedemoshopping;

import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.johnniesnow.firebasedemoshopping.entities.ShoppingList;
import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

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

    @Subscribe
    public void DeleteShoppingList(ShoppingListServices.DeleteShoppingListRequest request){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.ownerEmail + "/" + request.shoppingListId);
        reference.removeValue();

        /*
        DatabaseReference itemReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId);
        DatabaseReference sharedWithReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIRE_BASE_SHARED_WITH_REFERENCE + request.shoppingListId);

        itemReference.removeValue();
        sharedWithReference.removeValue();
        */
    }

    @Subscribe
    public void ChangeListName(ShoppingListServices.ChangeListNameRequest request){
        ShoppingListServices.ChangeListNameResponse response = new ShoppingListServices.ChangeListNameResponse();

        if (request.newShoppingListName.isEmpty()){
            response.setPropertyErrors("listName","Shopping list must a name");
        }

        if (response.didSuceed()){
            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.shoppingListOwnerEmail+ "/" + request.shoppingListId);

            HashMap<String,Object> timeLastChanged = new HashMap<>();
            timeLastChanged.put("date", ServerValue.TIMESTAMP);


            Map newListData = new HashMap();
            newListData.put("listName", request.newShoppingListName);
            newListData.put("dateLastChanged", timeLastChanged);
            reference.updateChildren(newListData);
        }

        bus.post(response);
    }

    @Subscribe
    public void getCurrentShoppingList(ShoppingListServices.GetCurrentShoppingListRequest request){
        final ShoppingListServices.GetCurrentShoppingListResponse response = new ShoppingListServices.GetCurrentShoppingListResponse();
        response.valueEventListener = request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.shoppingList = dataSnapshot.getValue(ShoppingList.class);
                if (response.shoppingList!=null){
                    bus.post(response);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

