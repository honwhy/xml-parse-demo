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
		
		Map<String,String> context = xmlParseUtils.parse(param, paths);
		
		param = FileUtils.readFileToString(new File("status.js"), Charsets.UTF_8.toString());
		param = xmlParseUtils.render(param, context);
		
		Integer ret = xmlParseUtils.evaluate(param);
		assertTrue(ret == 2);
	}
}
