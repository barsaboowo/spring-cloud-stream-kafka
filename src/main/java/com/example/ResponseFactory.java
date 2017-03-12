package com.example;

/**
 * Created by b on 12/3/17.
 */
public interface ResponseFactory<Request, Response> {
    Response generateResponse( Request payload);
}
