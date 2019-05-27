package com.chess.chessapi.controller;

import com.chess.chessapi.model.JsonResult;
import com.chess.chessapi.services.DistrictService;
import com.chess.chessapi.services.ProvinceService;
import com.chess.chessapi.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressProviderController {
    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;

    @GetMapping("/AddressProvider/getProvinces")
    public JsonResult getProvinces(){
        return new JsonResult(null,provinceService.getAllProvince());
    }

    @GetMapping("/AddressProvider/getDistrictsByProvinceId")
    public JsonResult getDistrictsByProvinceId(@RequestParam("provindeId") String provindeId){
        return new JsonResult(null,districtService.getAllByProvinceId(provindeId));
    }

    @GetMapping("/AddressProvider/getWardsByDistrictId")
    public JsonResult getWardsByDistrictId(@RequestParam("districtId") String districtId){
        return new JsonResult(null,wardService.getAllByDistrictId(districtId));
    }
}
