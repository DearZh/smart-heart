package com.gangtise.cloud.common.osm.service;

/**
 * @Description: osm
 * @Author: Arnold.zhao
 * @Date: 2021/1/11
 */
public interface OSMService {


    /**
     * 获取问题类别 （华为阿里通用）
     *
     * @param productCategoryId 根据产品类别ID，获取对应的问题类别；（阿里无需传送该参数，直接获取所有问题类别）
     * @return
     */
    Object listProducts(String productCategoryId);


    /**
     * 查询产品类型列表 （华为）
     *
     * @param productCategoryName 产品类型名称进行查找
     * @return
     */
    Object listProductCatgories(String productCategoryName) throws Exception;

    /**
     * 查询工单来源（华为）
     *
     * @return
     */
    Object listOsmSource() throws Exception;


    /**
     * 新建工单
     *
     * @param productCategoryId     华为-工单产品类型
     * @param withSourceId          华为-工单来源| 阿里-产品code
     * @param withSimpleDescription 工单问题
     * @param withBusinessTypeId    工单问题类型| 产品问题类别ID
     * @return
     * @throws Exception
     */
    Object insertOsm(String productCategoryId, String withSourceId, String withSimpleDescription, String withBusinessTypeId) throws Exception;

}
