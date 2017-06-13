package com.johnniesnow.firebasedemoshopping.infrastructure;

import java.util.HashMap;

/**
 * Created by Jonathan on 12/06/17.
 */

public class ServiceResponse  {

    private HashMap<String, String> propertyErrors;

    public ServiceResponse() {
        propertyErrors = new HashMap<>();
    }

    public void setPropertyErrors(String property, String error){
        propertyErrors.put(property, error);
    }

    public String getPropertyError(String property){
        return  propertyErrors.get(property);
    }

    public boolean didSuceed(){
        return (propertyErrors.size() == 0);
    }
}
