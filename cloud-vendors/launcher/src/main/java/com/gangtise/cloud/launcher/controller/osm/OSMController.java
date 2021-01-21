package com.gangtise.cloud.launcher.controller.osm;


import com.aliyuncs.workorder.model.v20200326.CreateTicketResponse;
import com.gangtise.cloud.common.constant.BusinessConstant;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.common.constant.SystemConstant;
import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.launcher.controller.osm.api.OSMSwaggerService;
import com.gangtise.cloud.launcher.mp.entity.CloudOsmList;
import com.gangtise.cloud.launcher.mp.service.CloudOsmListService;
import com.gangtise.cloud.launcher.util.CloudBuild;
import com.gangtise.cloud.launcher.util.ParameterCheck;
import com.gangtise.cloud.launcher.util.R;
import com.huaweicloud.sdk.osm.v2.model.CreateCasesResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/8
 */
@RestController
@RequestMapping("/osm/{type}")
public class OSMController implements OSMSwaggerService {

    @Autowired
    private CloudOsmListService cloudOsmListService;

    /**
     * 获取产品类别
     *
     * @return
     */
    @Override
    public R listProductCatgories(CloudName type, String productCategoryName) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.listProductCatgories(productCategoryName);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    /**
     * 获取问题类别
     *
     * @param type
     * @param productCategoryId 产品类别
     * @return
     * @throws Exception
     */
    @Override
    public R listProducts(CloudName type, String productCategoryId) throws Exception {
        ParameterCheck.isNull(String.class, productCategoryId);
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.listBusinessProducts(productCategoryId);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    @Override
    public R insertOsm(CloudName type, String email, String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception {
        if (CloudName.HUAWEI.equals(type)) {
            //华为无需校验email
            ParameterCheck.isNull(String.class, productCategoryId, withSourceId, withBusinessTypeId, withSimpleDescription, withBusinessTypeId);
        } else if (CloudName.ALIBABA.equals(type)) {
            //阿里无需校验工单来源
            ParameterCheck.isNull(String.class, email, productCategoryId, withBusinessTypeId, withSimpleDescription, withBusinessTypeId);
        }
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.insertOsm(email, productCategoryId, withSourceId, withSimpleDescription, withBusinessTypeId);
        if (content != null) {
            if (CloudName.HUAWEI.equals(type)) {
                CreateCasesResponse casesResponse = (CreateCasesResponse) content;
                String osmId = casesResponse.getIncidentId();
                if (StringUtils.isNotBlank(osmId)) {
                    saveDetail(type, content, email, productCategoryId, withSourceId, withSimpleDescription, withBusinessTypeId);
                    return R.ok(content);
                }
            } else if (CloudName.ALIBABA.equals(type)) {
                CreateTicketResponse createTicketResponse = (CreateTicketResponse) content;
                String code = createTicketResponse.getCode();
                if (StringUtils.isNotBlank(code) && code.equals(SystemConstant.HTTP_200)) {
                    saveDetail(type, content, email, productCategoryId, withSourceId, withSimpleDescription, withBusinessTypeId);
                    return R.ok(content);
                }
            }
        }
        return R.failed();
    }

    /**
     * 异步存储工单数据入库
     */
    private void saveDetail(CloudName type, Object content, String email, String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            CloudOsmList cloudOsmList = new CloudOsmList();
            if (CloudName.HUAWEI.equals(type)) {
                cloudOsmList.setType(CloudName.HUAWEI.name());
                CreateCasesResponse casesResponse = (CreateCasesResponse) content;
                String osmId = casesResponse.getIncidentId();
                cloudOsmList.setCloudId(osmId);
                cloudOsmList.setCustomId(BusinessConstant.cloudUserMap.get(CloudName.HUAWEI).getAk());
            } else if (CloudName.ALIBABA.equals(type)) {
                cloudOsmList.setType(CloudName.ALIBABA.name());
                CreateTicketResponse createTicketResponse = (CreateTicketResponse) content;
                String osmId = createTicketResponse.getData();
                cloudOsmList.setCloudId(osmId);
                cloudOsmList.setCustomId(BusinessConstant.cloudUserMap.get(CloudName.ALIBABA).getAk());
            }
            cloudOsmList.setEmail(email);
            cloudOsmList.setStatus(1);
            cloudOsmList.setProductId(productCategoryId);
            cloudOsmList.setProductBusinessId(withBusinessTypeId);
            cloudOsmList.setOsmId(withSourceId);
            cloudOsmList.setName(withSimpleDescription);
            cloudOsmList.setCreateTime(System.currentTimeMillis());
            cloudOsmListService.save(cloudOsmList);
        });
    }

    @Override
    public R listCaseStatus(CloudName type) throws Exception {
        if (CloudName.ALIBABA.equals(type)) {
            return R.ok(BusinessConstant.alibabaCaseCode);
        } else if (CloudName.HUAWEI.equals(type)) {
            return R.ok(BusinessConstant.huaweiCaseCode);
        }
        return R.failed();
    }

    @Override
    public R listCase(CloudName type, String status, Integer page, String startTime, String endTime) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.listCase(status, page, startTime, endTime);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    @Override
    public R insertCaseMessage(CloudName type, String caseId, String message, Integer messageType) throws Exception {
        ParameterCheck.isNull(String.class, caseId, message);
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.insertCaseMessage(caseId, message, messageType);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    @Override
    public R huaweiCaseActionType(CloudName type) throws Exception {
        return R.ok(BusinessConstant.huaweiCaseActionType);
    }

    @Override
    public R listUnread(CloudName type, String caseId) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.listUnread(caseId);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    @Override
    public R caseUnread(CloudName type, String caseId) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.caseUnread(caseId);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    @Override
    public R caseAction(CloudName type, String caseId, String caseActionType) throws Exception {
        if (type.equals(CloudName.HUAWEI)) {
            ParameterCheck.isNull(String.class, caseId);
        }
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.caseAction(caseId, caseActionType);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();
    }

    @Override
    public R showCaseDetail(CloudName type, String caseId) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.showCaseDetail(caseId);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();

    }

    @Override
    public R listMessages(CloudName type, String caseId, Integer page) throws Exception {
        OSMService osmService = CloudBuild.OSM().create(type);
        Object content = osmService.listMessages(caseId, page);
        if (content != null) {
            return R.ok(content);
        }
        return R.failed();

    }


}
