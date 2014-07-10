package com.tradeplatform.order;

import java.util.HashMap;
import java.util.Map;

public class OrderCatagories {
	// TODO: make map unmodifiable
	private final static Map<String, String> orderCatagoriesIdMap = new HashMap<String, String>();
	private final static String[] catagoriesContext = { "衣帽", "装饰", "食品", "母婴",
			"其它", "--请选择类别--" };
	private final static String[] orderIDs = { "1", "2", "3", "4", "5", "0" };
	static {
		for (int i = 0; i < catagoriesContext.length; i++) {
			orderCatagoriesIdMap.put(catagoriesContext[i], orderIDs[i]);
		}
	}

	public static String[] getOrderCatagories() {
		return catagoriesContext;
	}

	public static String getId(String catagory) {
		if (catagory.equals("--请选择类别--")) {
			throw new MakeOrderRuntimeException("请选择类别");
		}
		return orderCatagoriesIdMap.get(catagory);
	}

	public static String getName(String Id) {
		int index = -1;
		try {
			index = Integer.parseInt(Id);
		} catch (NumberFormatException ex) {
			throw new MakeOrderRuntimeException("类别无效");
		}
		if (index <= 0 || index >= catagoriesContext.length - 1) {
			return catagoriesContext[catagoriesContext.length - 2];
		}
		return catagoriesContext[index - 1];
	}

	public static int getDefaultSelection() {
		return catagoriesContext.length - 1;
	}
}
