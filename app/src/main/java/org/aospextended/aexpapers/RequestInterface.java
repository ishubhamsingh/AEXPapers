package org.aospextended.aexpapers;

import org.aospextended.aexpapers.models.ServerRequest;
import org.aospextended.aexpapers.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ishubhamsingh on 25/9/17.
 */

public interface RequestInterface {

    @POST("aexstats_api/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}