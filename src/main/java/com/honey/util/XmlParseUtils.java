package com.honey.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.google.common.collect.Maps;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class XmlParseUtils {
	private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	private XPathFactory xpathFactory = XPathFactory.newInstance();
	
	//freemarker
	private Configuration config;
	
	/**
	 * 
	 * @param param	数据内容
	 * @param paths 表达式
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> parse(String param, Map<String,String> paths) throws Exception{
		InputSource inputSource = new InputSource(new StringReader(param));
		Document document = dbFactory.newDocumentBuilder().parse(inputSource);
		Map<String,Object> map = Maps.newHashMap();
		for(String key : paths.keySet()) {
			XPath xpath = xpathFactory.newXPath();
			Node node = (Node) xpath.evaluate(paths.get(key), document, XPathConstants.NODE);
			if(node == null) {
				throw new Exception("node not found, xpath is " + paths.get(key));
			}
			map.put(key, node.getTextContent());
		}
		return map;
	}
	/**
	 * 
	 * @param param FreeMarker模板
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public String render(String param, Map<String,Object> context) throws Exception {
		Configuration cfg = new Configuration();
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("myTemplate",param);
		cfg.setTemplateLoader(stringLoader);
		Template template = cfg.getTemplate("myTemplate","utf-8");
		StringWriter writer = new StringWriter();
		template.process(context, writer);
		return writer.toString();
	}
	
	public Object evaluateByJexl(String expression, Map<String,Object> context) {
		JexlEngine jexl = new JexlBuilder().create();
		JexlExpression e = jexl.createExpression(expression);
		JexlContext jc = new MapContext(context);
		return e.evaluate(jc);
	}
	
	public Object evaluate(String expression) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		return engine.eval(expression);
	}

	
	
}
