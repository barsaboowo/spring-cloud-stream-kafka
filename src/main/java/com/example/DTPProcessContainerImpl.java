package com.example;

import com.example.model.Request;
import com.example.model.Response;

import java.util.Objects;
import java.util.Set;

/**
 * Created by b on 12/3/17.
 */
public class DTPProcessContainerImpl implements DTPProcessContainer<Request, Response> {

    private final Set<String> validCounterparties;

    public DTPProcessContainerImpl(Set<String> validCounterparties) {
        Objects.requireNonNull(validCounterparties, "validCounterparties cannot be null");
        this.validCounterparties = validCounterparties;
    }


    @Override
    public Response process(Request payload, Response response) {
        if (validCounterparties.contains(payload.getCounterparty())) {
            response.setIsAllowed(true);
        } else {
            response.setIsAllowed(false);
        }
        return response;

    }
}
