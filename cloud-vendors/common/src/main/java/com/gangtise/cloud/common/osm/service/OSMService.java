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
    String listProducts(String productCategoryId);


    /**
     * 查询产品类型列表 （华为）
     *
     * @param productCategoryName 产品类型名称进行查找
     * @return
     */
    String listProductCatgories(String productCategoryName) throws Exception;

    /**
     * 查询工单来源（华为）
     *
     * @return
     */
    String listOsmSource() throws Exception;

}
