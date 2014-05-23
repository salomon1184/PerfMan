package com.salomon.perfman.util;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParamRetriever {
	private Element rootElement = null;

	private static ParamRetriever param;

	public static ParamRetriever getParam() {
		if (param == null) {
			StringBuilder builder = new StringBuilder();
			builder.append(System.getProperty("user.dir"));
			builder.append(File.separator);
			builder.append("res");
			builder.append(File.separator);
			builder.append("params.xml");
			param = new ParamRetriever(builder.toString());
		}

		return param;
	}

	private ParamRetriever(String xmlConfigFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document dom;

		try {
			File paramsXml = new File(xmlConfigFile);
			// System.out.println(Config.PATH_PARAMS_XML);
			if (paramsXml.exists()) {
				builder = factory.newDocumentBuilder();
				dom = builder.parse(paramsXml);
				this.rootElement = dom.getDocumentElement();
			}
		} catch (Exception e) {
			System.err.println("读取配置文件出错:  " + xmlConfigFile);
			e.printStackTrace();
		}
	}

	public String getParam(String paramName) {
		String value = null;

		NodeList singleItems = this.rootElement.getElementsByTagName("Item");
		for (int i = 0; i < singleItems.getLength(); i++) {
			Element param = (Element) singleItems.item(i);
			if (paramName.equalsIgnoreCase(param.getAttribute("Name"))) {
				value = param.getFirstChild().getNodeValue();
				break;
			}
		}

		return value;
	}

	public int getParamInt(String paramName) {
		return Integer.parseInt(this.getParam(paramName));
	}

	public ArrayList<String> getParams(String paramName) {
		ArrayList<String> valueList = new ArrayList<String>();

		NodeList multiItems = this.rootElement.getElementsByTagName("Items");
		for (int i = 0; i < multiItems.getLength(); i++) {
			Element param = (Element) multiItems.item(i);
			if (paramName.equalsIgnoreCase(param.getAttribute("Name"))) {
				NodeList valueNodes = param.getElementsByTagName("Value");
				for (int j = 0; j < valueNodes.getLength(); j++) {
					Element value = (Element) valueNodes.item(j);
					// Uia.log.message(value.getFirstChild().getNodeValue());
					valueList.add(value.getFirstChild().getNodeValue());
				}
			}

		}

		return valueList;
	}
}
