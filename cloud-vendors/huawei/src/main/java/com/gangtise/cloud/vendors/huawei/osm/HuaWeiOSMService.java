package com.gangtise.cloud.vendors.huawei.osm;

import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.vendors.huawei.utils.Client;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.osm.v2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Slf4j
public class HuaWeiOSMService implements OSMService {

    /**
     * @param productCategoryId 根据产品类别ID，获取对应的问题类别；
     * @return
     */
    @Override
    public Object listProducts(String productCategoryId) {

        ListProblemTypesRequest request = new ListProblemTypesRequest();
        if (StringUtils.isNotBlank(productCategoryId)) {
            request.withProductCategoryId(productCategoryId);
        }
        try {
            ListProblemTypesResponse response = Client.create().listProblemTypes(request);
            return response;
        } catch (ConnectionException e) {
            error(e);
        } catch (RequestTimeoutException e) {
            error(e);
        } catch (ServiceResponseException e) {
            error(e.getErrorCode(), e.getHttpStatusCode(), e.getErrorMsg(), e);
        }
        return null;
    }


    /**
     * 查询产品类型列表 （华为）
     *
     * @param productCategoryName 产品类型名称进行查找
     * @return
     */
    @Override
    public Object listProductCatgories(String productCategoryName) {
        ListProductCategoriesRequest request = new ListProductCategoriesRequest();
        if (StringUtils.isNotBlank(productCategoryName)) {
            request.withProductCategoryName(productCategoryName);
        }
        try {
            ListProductCategoriesResponse response = Client.create().listProductCategories(request);
            return response;
        } catch (ConnectionException e) {
            error(e);
        } catch (RequestTimeoutException e) {
            error(e);
        } catch (ServiceResponseException e) {
            error(e.getErrorCode(), e.getHttpStatusCode(), e.getErrorMsg(), e);
        }
        return null;
    }

    @Override
    public String listOsmSource() {
        return null;
    }

    @Override
    public Object insertOsm(String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception {
        CreateCasesRequest request = new CreateCasesRequest();
        try {
            CreateOrderIncidentV2Req body = new CreateOrderIncidentV2Req();
            body.withProductCategoryId(productCategoryId);
            body.withRegionId("cn-north-1");
            body.withSourceId(withSourceId);
            body.withSimpleDescription(withSimpleDescription);
            body.withBusinessTypeId(withBusinessTypeId);
            request.withBody(body);
            CreateCasesResponse response = Client.create().createCases(request);
            return response;
        } catch (ConnectionException e) {
            error(e);
        } catch (RequestTimeoutException e) {
            error(e);
        } catch (ServiceResponseException e) {
            error(e.getErrorCode(), e.getHttpStatusCode(), e.getErrorMsg(), e);
        }
        return null;
    }


    private void error(Exception e) {
        log.error("", e);
    }

    private void error(String errorCode, int httpStatusCode, String errorMsg, Exception e) {
        log.error("errorCode" + errorCode + "httpStatusCode" + httpStatusCode + "errorMsg" + errorMsg, e);
    }
}
