package com.gangtise.cloud.vendors.alibaba.osm;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.workorder.model.v20200326.*;
import com.gangtise.cloud.common.constant.SystemConstant;
import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.vendors.alibaba.utils.Client;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    public Object listBusinessProducts(String productCategoryId) throws Exception {
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
    public Object listProductCatgories(String productCategoryName) throws Exception {

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

    @Override
    public Object listCase(String status, Integer page, String startTime, String endTime) throws Exception {
        ListTicketsRequest request = new ListTicketsRequest();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (startTime != null) {
                request.setCreatedAfterTime(simpleDateFormat.parse(startTime).getTime());
            }
            if (endTime != null) {
                request.setCreatedBeforeTime(simpleDateFormat.parse(endTime).getTime());
            }
        } catch (ParseException e) {
            throw e;
        }
        if (status != null) {
            request.setTicketStatus(status);
        }
        request.setPageSize(SystemConstant.size.intValue());
        if (page == null)
            page = 0;
        request.setPageStart(page);

        try {
            ListTicketsResponse response = Client.client().getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            error(e);
        } catch (ClientException e) {
            error(e.getErrCode(), e.getRequestId(), e.getErrMsg(), e);
        }
        return null;
    }

    @Override
    public Object insertCaseMessage(String caseId, String message, Integer type) throws Exception {
        ReplyTicketRequest request = new ReplyTicketRequest();
        request.setTicketId(caseId);
        request.setContent(message);

        try {
            ReplyTicketResponse response = Client.client().getAcsResponse(request);
            if (response.getSuccess())
                return response;
            return null;
        } catch (ServerException e) {
            error(e);
        } catch (ClientException e) {
            error(e.getErrCode(), e.getRequestId(), e.getErrMsg(), e);
        }
        return null;
    }

    @Override
    public Object caseUnread(String caseId) throws Exception {
        throw new NoSuchMethodException();
    }

    @Override
    public Object listUnread(String caseId) throws Exception {
        throw new NoSuchMethodException();
    }

    /**
     * 工单关闭
     *
     * @param caseId 工单ID
     * @param action BusinessConstant.huaweiCaseActionType
     * @return
     */
    @Override
    public Object caseAction(String caseId, String action) throws Exception {
        CloseTicketRequest request = new CloseTicketRequest();
        request.setTicketId(caseId);
        try {
            CloseTicketResponse response = Client.client().getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            error(e);
        } catch (ClientException e) {
            error(e.getErrCode(), e.getRequestId(), e.getErrMsg(), e);
        }
        return null;
    }

    @Override
    public Object showCaseDetail(String caseId) throws Exception {
        throw new NoSuchMethodException();
    }

    @Override
    public Object listMessages(String caseId, Integer page) throws Exception {
        ListTicketNotesRequest request = new ListTicketNotesRequest();
        request.setTicketId(caseId);
        try {
            ListTicketNotesResponse response = Client.client().getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            error(e);
        } catch (ClientException e) {
            error(e.getErrCode(), e.getRequestId(), e.getErrMsg(), e);
        }
        return null;
    }


    private void error(Exception e) throws Exception {
        log.error("", e);
        throw e;
    }

    private void error(String errCode, String requestId, String errMsg, Exception e) throws Exception {
        log.error("ErrCode:" + errCode + "ErrMsg:" + errMsg + "RequestId:" + requestId, e);
        throw e;
    }

}
