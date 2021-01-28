package com.gangtise.cloud.launcher.mp.service;

import com.aliyuncs.workorder.model.v20200326.ListCategoriesResponse;
import com.aliyuncs.workorder.model.v20200326.ListProductsResponse;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gangtise.cloud.common.constant.BusinessConstant;
import com.gangtise.cloud.common.constant.CloudName;
import com.gangtise.cloud.common.constant.SystemConstant;
import com.gangtise.cloud.common.osm.service.OSMService;
import com.gangtise.cloud.launcher.mp.entity.CloudOsmProduct;
import com.gangtise.cloud.launcher.mp.entity.CloudOsmProductType;
import com.gangtise.cloud.launcher.mp.entity.CloudUser;
import com.gangtise.cloud.launcher.util.CloudBuild;
import com.huaweicloud.sdk.osm.v2.model.IncidentProductCategoryV2;
import com.huaweicloud.sdk.osm.v2.model.ListProblemTypesResponse;
import com.huaweicloud.sdk.osm.v2.model.ListProductCategoriesResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
@Service
@Log4j
public class InitService {

    @Autowired
    private CloudUserService cloudUserService;
    @Autowired
    private CloudOsmProductService cloudOsmProductService;
    @Autowired
    private CloudOsmProductTypeService cloudOsmProductTypeService;

    @SneakyThrows
    @PostConstruct
    public void init() {
        //数据初始化，禁止异步执行 Arnold.zhao 2021/1/20
        initCaseCode();
        initCloudUser();
        initHuaWeiCaseActionType();
        initOsmProduct();
        initOsmProductType();
    }

    private void initCloudUser() {
        List<CloudUser> list = cloudUserService.list(Wrappers.<CloudUser>lambdaQuery().eq(CloudUser::getType, CloudName.ALIBABA).or().eq(CloudUser::getType, CloudName.HUAWEI));
        list.forEach(v -> {
            if (v.getType().equals(CloudName.ALIBABA)) {
                BusinessConstant.cloudUserMap.put(CloudName.ALIBABA, new com.gangtise.cloud.common.entity.CloudUser(v.getRegionId(), v.getSecretId(), v.getSecretKeySecret()));
            } else if (v.getType().equals(CloudName.HUAWEI)) {
                BusinessConstant.cloudUserMap.put(CloudName.HUAWEI, new com.gangtise.cloud.common.entity.CloudUser(v.getRegionId(), v.getSecretId(), v.getSecretKeySecret()));
            }
        });
    }

    private void initCaseCode() {
        BusinessConstant.huaweiCaseCode.put("0", "待受理");
        BusinessConstant.huaweiCaseCode.put("1", "处理中");
        BusinessConstant.huaweiCaseCode.put("2", "待确认结果");
        BusinessConstant.huaweiCaseCode.put("3", "已完成");
        BusinessConstant.huaweiCaseCode.put("4", "已撤销");
        BusinessConstant.huaweiCaseCode.put("12", "无效");
        BusinessConstant.huaweiCaseCode.put("17", "待反馈");
        //ali
        BusinessConstant.alibabaCaseCode.put("in_progress", "处理中");
        BusinessConstant.alibabaCaseCode.put("wait_feedback", "待您反馈");
        BusinessConstant.alibabaCaseCode.put("wait_confirm", "待您确认");
        BusinessConstant.alibabaCaseCode.put("completed", "已关闭");
    }

    private void initHuaWeiCaseActionType() {
        BusinessConstant.huaweiCaseActionType.put("cancel", "撤销工单");
        BusinessConstant.huaweiCaseActionType.put(SystemConstant.OSM_CLOSE, "关闭工单");
        BusinessConstant.huaweiCaseActionType.put("press", "催单");
//        BusinessConstant.huaweiCaseActionType.put("delete", "删除工单");
    }

    /**
     * 工单产品数据初始化
     */
    private void initOsmProduct() {
        int count = cloudOsmProductService.count();
        //表里没有数据时，初次启动同步数据
        if (count == 0) {
            try {
                OSMService osmService = CloudBuild.OSM().create(CloudName.HUAWEI);
                ListProductCategoriesResponse listProductCategoriesResponse = (ListProductCategoriesResponse) osmService.listProductCatgories(null);
                List<IncidentProductCategoryV2> list = listProductCategoriesResponse.getIncidentProductCategoryList();
                List<CloudOsmProduct> cloudOsmProducts = new ArrayList<>();
                list.forEach(v -> {
                    CloudOsmProduct cloudOsmProduct = new CloudOsmProduct();
                    cloudOsmProduct.setAcronym(v.getIncidentProductCategoryAcronym());
                    cloudOsmProduct.setDescription(v.getIncidentProductCategoryDesc());
                    cloudOsmProduct.setId(v.getIncidentProductCategoryId());
                    cloudOsmProduct.setName(v.getIncidentProductCategoryName());
                    cloudOsmProduct.setType(CloudName.HUAWEI.name());
                    cloudOsmProduct.setCreateTime(System.currentTimeMillis());
                    cloudOsmProducts.add(cloudOsmProduct);
                });
                //>>> 阿里 >>>
                osmService = CloudBuild.OSM().create(CloudName.ALIBABA);
                ListProductsResponse listProductsResponse = (ListProductsResponse) osmService.listProductCatgories(null);
                listProductsResponse.getData().getConsultationMore().forEach(v -> {
                    alibabaProduct(cloudOsmProducts, v.getName(), v.getDescription(), v.getProductCode());
                });
                listProductsResponse.getData().getHotConsultation().forEach(v -> {
                    alibabaProduct(cloudOsmProducts, v.getName(), v.getDescription(), v.getProductCode());
                });
                listProductsResponse.getData().getHotTech().forEach(v -> {
                    alibabaProduct(cloudOsmProducts, v.getName(), v.getDescription(), v.getProductCode());
                });
                listProductsResponse.getData().getTechMore().forEach(v -> {
                    v.getProductList().forEach(f -> {
                        alibabaProduct(cloudOsmProducts, f.getName(), f.getDescription(), f.getProductCode());
                    });
                });
                cloudOsmProductService.saveBatch(cloudOsmProducts);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private void alibabaProduct(List<CloudOsmProduct> cloudOsmProducts, String name, String desc, String code) {
        CloudOsmProduct cloudOsmProduct = new CloudOsmProduct();
        cloudOsmProduct.setName(name);
        cloudOsmProduct.setDescription(desc);
        cloudOsmProduct.setId(code);
        cloudOsmProduct.setType(CloudName.ALIBABA.name());
        cloudOsmProduct.setCreateTime(System.currentTimeMillis());
        cloudOsmProducts.add(cloudOsmProduct);
    }

    /**
     * 工单产品问题类型初始化
     */
    private void initOsmProductType() {
        int count = cloudOsmProductTypeService.count();
        if (count == 0) {
            //>>> 阿里 init >>>
            OSMService osmService = CloudBuild.OSM().create(CloudName.ALIBABA);
            List<CloudOsmProduct> list = cloudOsmProductService.list(Wrappers.<CloudOsmProduct>lambdaQuery().eq(CloudOsmProduct::getType, CloudName.ALIBABA.name()));
            Map<String, CloudOsmProductType> mapBatch = new HashMap<>();
            list.forEach(v -> {
                try {
                    ListCategoriesResponse listCategoriesResponse = (ListCategoriesResponse) osmService.listBusinessProducts(v.getId());
                    listCategoriesResponse.getData().getList().forEach(f -> {
                        CloudOsmProductType cloudOsmProductType = new CloudOsmProductType();
                        cloudOsmProductType.setId(f.getId().toString());
                        cloudOsmProductType.setName(f.getName());
                        cloudOsmProductType.setType(CloudName.ALIBABA.name());
                        cloudOsmProductType.setCreateTime(System.currentTimeMillis());
                        mapBatch.put(f.getId().toString(), cloudOsmProductType);
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
            //>>> HUAWEI >>>
            list = cloudOsmProductService.list(Wrappers.<CloudOsmProduct>lambdaQuery().eq(CloudOsmProduct::getType, CloudName.HUAWEI.name()));
            OSMService osmService1 = CloudBuild.OSM().create(CloudName.HUAWEI);
            list.forEach(f -> {
                try {
                    ListProblemTypesResponse listProblemTypesResponse = (ListProblemTypesResponse) osmService1.listBusinessProducts(f.getId());
                    listProblemTypesResponse.getIncidentBusinessTypeList().forEach(v -> {
                        CloudOsmProductType cloudOsmProductType = new CloudOsmProductType();
                        cloudOsmProductType.setId(v.getBusinessTypeId());
                        cloudOsmProductType.setName(v.getBusinessTypeName());
                        cloudOsmProductType.setType(CloudName.HUAWEI.name());
                        cloudOsmProductType.setCreateTime(System.currentTimeMillis());
                        mapBatch.put(v.getBusinessTypeId(), cloudOsmProductType);
                    });
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            saveBatch(mapBatch);
        }
    }

    private void saveBatch(Map<String, CloudOsmProductType> mapBatch) {
        List<CloudOsmProductType> listBatch = new ArrayList<>();
        Iterator<Map.Entry<String, CloudOsmProductType>> iterator = mapBatch.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CloudOsmProductType> entry = iterator.next();
            listBatch.add(entry.getValue());
        }
        cloudOsmProductTypeService.saveBatch(listBatch);
    }

}
