package com.smart.heart.java.w3c;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultDocument;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arnold.zhao
 * @version Dom4jTest.java, v 0.1 2021-12-06 16:35 Arnold.zhao Exp $$
 */
public class Dom4jTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("D");
        list.add("C");
        List<String> list1 = list.stream().map(v -> {
            if (v.equals("A")) {
                return null;
            }
            return v;
        }).filter(v -> {
            if (v == null) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        System.out.println(list1.toString());
    }
    /*
    public static void main(String[] args) {

        Document document = null;
        try {
            //String path = DomTest.class.getClassLoader().getResource("log4j2.xml").getPath();
            String location = "W:\\JAVA\\ManBangWrokSpace\\commons\\common-logger-log4j2\\src\\test\\resources\\log4j2-test-20211202.xml";
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(new File(location)); // 读取XML文件,获得document对象
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(document);
        Element element = document.getRootElement();
        //attribute
        element.attributes().forEach(v -> {
//            System.out.println(v.getQName());//org.dom4j.tree.DefaultDocument@404b9385 [Document: name file:///W:/JAVA/ManBangWrokSpace/commons/common-logger-log4j2/src/test/resources/log4j2-test-20211202.xml]
//            System.out.println(v.getNamespacePrefix());//org.dom4j.QName@cacdcff2 [name: status namespace: "org.dom4j.Namespace@babe [Namespace: prefix  mapped to URI ""]"]
//            System.out.println(v.getQualifiedName());//status
//            System.out.println(v.getName());//status
//            System.out.println(v.getValue());//info
        });

        element.elements().forEach(v -> {
            System.out.println("二级元素名称：" + v.getName());
            v.elements().forEach(f -> {
                System.out.println("三级元素名称：" + f.getName());
                f.attributes().stream().forEach(a -> {
                    System.out.println("元素属性：" + a.getName() + " = " + a.getValue());
                });
            });
        });

        Element createElement = element.addElement("createElement");
//        Attribute attribute = new DefaultAttribute(new QName("name"), "nameValue");
        createElement.addAttribute("nameTest", "nameValue");
        createElement.addElement("AppenderRef").addAttribute("ref", "ymmFileMetric");


        *//*String filename = "D:\\logs\\test.xml";
        try {
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), "UTF-8"));
            writer.write(document);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        *//*
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(
                    byteArrayOutputStream, "UTF-8"));
            writer.write(document);
            writer.close();
            System.out.println(byteArrayOutputStream.toString("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
