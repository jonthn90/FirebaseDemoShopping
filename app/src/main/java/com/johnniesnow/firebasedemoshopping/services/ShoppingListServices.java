package com.johnniesnow.firebasedemoshopping.services;

import com.johnniesnow.firebasedemoshopping.infrastructure.ServiceResponse;

/**
 * Created by jonathan on 1/24/18.
 */

public class ShoppingListServices {

    public ShoppingListServices() {
    }

    public static class AddShoppingListRequest {

        public String shoppingListName;
        public String ownerName;
        public String ownerEmail;

        public AddShoppingListRequest(String shoppingListName, String ownerName, String ownerEmail) {
            this.shoppingListName = shoppingListName;
            this.ownerName = ownerName;
            this.ownerEmail = ownerEmail;
        }
    }

    public static class AddShoppingListResponse extends ServiceResponse{

    }


    public static class DeleteShoppingListRequest{
        public String ownerEmail;
        public String shoppingListId;

        public DeleteShoppingListRequest(String ownerEmail, String shoppingListId) {
            this.ownerEmail = ownerEmail;
            this.shoppingListId = shoppingListId;
        }
    }

}
