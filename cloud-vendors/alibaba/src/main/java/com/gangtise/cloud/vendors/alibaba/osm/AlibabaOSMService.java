package com.gangtise.cloud.vendors.alibaba.osm;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.workorder.model.v20200326.ListProductsRequest;
import com.aliyuncs.workorder.model.v20200326.ListProductsResponse;
import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.vendors.alibaba.utils.Client;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Slf4j
public class AlibabaOSMService implements OSMService {

    @Override
    public Object listProducts(String productCategoryId) {

        ListProductsRequest request = new ListProductsRequest();
        try {
            ListProductsResponse response = Client.client().getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            error(e);
        } catch (ClientException e) {
            error(e.getErrCode(), e.getRequestId(), e.getErrMsg(), e);
        }
        return null;
    }


    @Override
    public String listProductCatgories(String productCategoryName) throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    @Override
    public String listOsmSource() throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    @Override
    public Object insertOsm(String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception {
        throw new NoSuchMethodException();
    }


    private void error(Exception e) {
        log.error("", e);
    }

    private void error(String errCode, String requestId, String errMsg, Exception e) {
        log.error("ErrCode:" + errCode + "ErrMsg:" + errMsg + "RequestId:" + requestId, e);
    }

}
