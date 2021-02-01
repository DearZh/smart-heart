package com.gangtise.cloud.vendors.huawei.osm;

import com.gangtise.cloud.common.constant.SystemConstant;
import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.vendors.huawei.utils.Client;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.osm.v2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
    public Object listBusinessProducts(String productCategoryId) throws Exception {

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
    public Object listProductCatgories(String productCategoryName) throws Exception {
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
    public Object insertOsm(String email, String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception {
        CreateCasesRequest request = new CreateCasesRequest();
        try {
            CreateOrderIncidentV2Req body = new CreateOrderIncidentV2Req();
            body.withProductCategoryId(productCategoryId);
            body.withRegionId("cn-north-1");
            if (StringUtils.isNotBlank(email)) {
                body.withRemindMail(email);
            }
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

    @Override
    public Object listCase(String status, Integer page, String startTime, String endTime) throws Exception {
        ListCasesRequest request = new ListCasesRequest();
        if (StringUtils.isNotBlank(startTime)) {
            request.withQueryStartTime(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            request.withQueryEndTime(endTime);
        }
        if (StringUtils.isNotBlank(status)) {
            request.withStatus(Integer.parseInt(status));
        }
        //华为根据对应偏移量进行查询
        Long offset = 0L;
        if (page != null && page != 0 && page != 1) {
            offset = SystemConstant.size * page;
        }
        request.withOffset(offset.intValue());
        request.withLimit(SystemConstant.size.intValue());
        try {
            ListCasesResponse response = Client.create().listCases(request);
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
    public Object insertCaseMessage(String caseId, String message, Integer type) throws Exception {
        CreateMessagesRequest request = new CreateMessagesRequest();
        request.withCaseId(caseId);
        CreateMessageV2Req body = new CreateMessageV2Req();
        CreateMessageDoV2 messagebody = new CreateMessageDoV2();
        messagebody.withContent(message);
        body.withType(type);
        body.withMessage(messagebody);
        request.withBody(body);
        try {
            CreateMessagesResponse response = Client.create().createMessages(request);
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
     * 查询未读消息
     *
     * @param caseId 工单ID
     * @return
     */
    @Override
    public Object listUnread(String caseId) throws Exception {
        ListUnreadNewInstantMessagesRequest request = new ListUnreadNewInstantMessagesRequest();
        List<String> listRequestCaseIds = new ArrayList<>();
        listRequestCaseIds.add(caseId);
        request.withCaseIds(listRequestCaseIds);
        try {
            ListUnreadNewInstantMessagesResponse response = Client.create().listUnreadNewInstantMessages(request);
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
     * 设置消息为已读
     *
     * @param caseId 工单ID
     * @return
     */
    @Override
    public Object caseUnread(String caseId) throws Exception {
        UpdateNewInstantMessagesReadRequest request = new UpdateNewInstantMessagesReadRequest();
        request.withCaseId(caseId);
        try {
            UpdateNewInstantMessagesReadResponse response = Client.create().updateNewInstantMessagesRead(request);
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
     * 工单操作
     *
     * @param caseId 工单ID
     * @param action BusinessConstant.huaweiCaseActionType
     * @return
     */
    @Override
    public Object caseAction(String caseId, String action) throws Exception {
        UpdateCasesRequest request = new UpdateCasesRequest();
        request.withCaseId(caseId);
        request.withActionId(action);
        try {
            UpdateCasesResponse response = Client.create().updateCases(request.withBody(new WorkOrderOperateV2Req()));
            if (response.getErrorCode() == null) {
                return response;
            }
            return null;
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
    public Object showCaseDetail(String caseId) throws Exception {
        ShowCaseDetailRequest request = new ShowCaseDetailRequest();
        request.withCaseId(caseId);
        try {
            ShowCaseDetailResponse response = Client.create().showCaseDetail(request);
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
    public Object listMessages(String caseId, Integer page) throws Exception {
        ListMessagesRequest request = new ListMessagesRequest();
        request.withCaseId(caseId);
        //page 转换为对应的偏移量进行查询
        Long offset = 0L;
        if (page != null && page != 0 && page != 1) {
            offset = SystemConstant.size * page;
        }
        request.withOffset(offset.intValue());
        request.withLimit(SystemConstant.size.intValue());
        try {
            ListMessagesResponse response = Client.create().listMessages(request);
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


    private void error(Exception e) throws Exception {
        log.error("", e);
        throw e;
    }

    private void error(String errorCode, int httpStatusCode, String errorMsg, Exception e) throws Exception {
        log.error("errorCode" + errorCode + "httpStatusCode" + httpStatusCode + "errorMsg" + errorMsg, e);
        throw e;
    }
}
