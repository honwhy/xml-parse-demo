package com.honey.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.honey.test.BaseTest;

public class XmlParseUtilsTest extends BaseTest{

	@Autowired
	private XmlParseUtils xmlParseUtils;
	
	@Test
	public void testHandle() throws Exception {
		String param = FileUtils.readFileToString(new File("order.xml"), Charsets.UTF_8.toString());
		Map<String,String> paths = Maps.newHashMap();
		paths.put("orderNo", "/CEBROOT/Order/OrderNo");
		paths.put("status", "/CEBROOT/Order/status");
		paths.put("msg", "/CEBROOT/Order/Msg");
		
		Map<String,Object> context = xmlParseUtils.parse(param, paths);
		
		String expression = FileUtils.readFileToString(new File("status.js"), Charsets.UTF_8.toString());
		expression = xmlParseUtils.render(expression, context);
		
		Object ret = xmlParseUtils.evaluate(expression);
		assertTrue(ret instanceof Double);
		
		expression = "status == 2 ? 2 : 1";
		Object o = xmlParseUtils.evaluateByJexl(expression, context);
		assertTrue(o instanceof Integer);
		assertTrue(o.equals(2));
	}
}
