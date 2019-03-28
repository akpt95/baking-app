package com.akash.baking.network;

import com.akash.baking.network.model.BakingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingAPI {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<BakingResponse>> getBakingResponse();
}
