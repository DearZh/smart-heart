package com.gangtise.cloud.vendors.alibaba.osm;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.workorder.model.v20200326.*;
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

    /**
     * 获取产品的问题类别
     *
     * @param productCategoryId 根据产品类别ID，获取对应的问题类别；（阿里无需传送该参数，直接获取所有问题类别）
     * @return
     */
    @Override
    public Object listBusinessProducts(String productCategoryId) {
        ListCategoriesRequest request = new ListCategoriesRequest();
        request.setProductCode(productCategoryId);
        try {
            ListCategoriesResponse response = Client.client().getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            error(e);
        } catch (ClientException e) {
            error(e.getErrCode(), e.getRequestId(), e.getErrMsg(), e);
        }
        return null;
    }


    /**
     * 获取产品类别
     *
     * @param productCategoryName （阿里接口无需根据产品名称查找）
     * @return
     */
    @Override
    public Object listProductCatgories(String productCategoryName) {

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
    public String listOsmSource() throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    @Override
    public Object insertOsm(String email, String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setContent(withSimpleDescription);
        request.setProductCode(productCategoryId);
        request.setCategory(withBusinessTypeId);
        request.setEmail(email);
        try {
            CreateTicketResponse response = Client.client().getAcsResponse(request);
            if (response.getSuccess()) {
                return response;
            }
        } catch (ServerException e) {
            error(e);
        } catch (ClientException e) {
            error(e.getErrCode(), e.getRequestId(), e.getErrMsg(), e);
        }
        return null;
    }


    private void error(Exception e) {
        log.error("", e);
    }

    private void error(String errCode, String requestId, String errMsg, Exception e) {
        log.error("ErrCode:" + errCode + "ErrMsg:" + errMsg + "RequestId:" + requestId, e);
    }

}
