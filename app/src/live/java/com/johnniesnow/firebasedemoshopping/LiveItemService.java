package com.johnniesnow.firebasedemoshopping;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.johnniesnow.firebasedemoshopping.entities.Item;
import com.johnniesnow.firebasedemoshopping.infrastructure.FirebaseDemoShoppingApplication;
import com.johnniesnow.firebasedemoshopping.infrastructure.Utils;
import com.johnniesnow.firebasedemoshopping.services.ItemService;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class LiveItemService extends BaseLiveService {
    public LiveItemService(FirebaseDemoShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void AddItem(ItemService.AddItemRequest request){
        ItemService.AddItemResponse response = new ItemService.AddItemResponse();

        if (request.itemName.isEmpty()){
            response.setPropertyErrors("itemName","Item must have a name.");
        }

        if (response.didSuceed()){

            DatabaseReference itemReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId).push();

            Item item = new Item(itemReference.getKey(), request.itemName, request.userEmail,"",false);
            itemReference.child("id").setValue(item.getId());
            itemReference.child("itemName").setValue(item.getItemName());
            itemReference.child("ownerEmail").setValue(item.getOwnerEmail());
            itemReference.child("boughtBy").setValue(item.getBoughtBy());
            itemReference.child("bought").setValue(item.isBought());

            DatabaseReference listReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.userEmail + "/" + request.shoppingListId);

            HashMap<String,Object> timeLastChanged = new HashMap<>();
            timeLastChanged.put("date", ServerValue.TIMESTAMP);
            Map newListData = new HashMap();
            newListData.put("dateLastChanged",timeLastChanged);
            listReference.updateChildren(newListData);



        }

        bus.post(response);
    }

    @Subscribe
    public void DeleteItem(ItemService.DeleteItemRequest request){
        DatabaseReference itemReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId + "/" + request.itemId);

        DatabaseReference listReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.userEmail + "/" + request.shoppingListId);

        itemReference.removeValue();
        HashMap<String,Object> timeLastChanged = new HashMap<>();
        timeLastChanged.put("date", ServerValue.TIMESTAMP);
        Map newListData = new HashMap();
        newListData.put("dateLastChanged",timeLastChanged);
        listReference.updateChildren(newListData);
    }

    @Subscribe
    public void ChangeItemName(ItemService.ChangeItemNameRequest request){

        ItemService.ChangeItemNameResponse response = new ItemService.ChangeItemNameResponse();

        if (request.newItemName.isEmpty()){
            response.setPropertyErrors("itemName","Item must have a name.");
        }

        if (response.didSuceed()){
            DatabaseReference itemReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId + "/" + request.itemId);
            DatabaseReference listReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.userEmail + "/" + request.shoppingListId);

            Map newItemData = new HashMap();
            newItemData.put("itemName",request.newItemName);

            itemReference.updateChildren(newItemData);

            HashMap<String,Object> timeLastChanged = new HashMap<>();
            timeLastChanged.put("date", ServerValue.TIMESTAMP);
            Map newListData = new HashMap();
            newListData.put("dateLastChanged",timeLastChanged);
            listReference.updateChildren(newListData);

        }

        bus.post(response);
    }

    @Subscribe
    public void ChangeItemBoughtStatus(ItemService.ChangeBoughtItemStatusRequest request){
        DatabaseReference itemReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId + "/" + request.item.getId());
        if (!request.item.isBought()){
            Map newItemData = new HashMap();
            newItemData.put("boughtBy",request.currentUserEmail);
            newItemData.put("bought",true);
            itemReference.updateChildren(newItemData);
        } else if(request.item.getBoughtBy().equals(request.currentUserEmail)){
            Map newItemData = new HashMap();
            newItemData.put("boughtBy","");
            newItemData.put("bought",false);
            itemReference.updateChildren(newItemData);
        }
    }
}
