package com.honey.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.namespace.NamespaceContext;
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
	private String namespace;
	private NamespaceContext namespaceContext;
	
	//freemarker
	private Configuration config;
	
	public void init() {
		this.namespaceContext = getNamespaceContext();
	}
	public Map<String,Object> parse(String param, Map<String,String> paths) throws Exception{
		InputSource inputSource = new InputSource(new StringReader(param));
		Document document = dbFactory.newDocumentBuilder().parse(inputSource);
		Map<String,Object> map = Maps.newHashMap();
		for(String key : paths.keySet()) {
			XPath xpath = xpathFactory.newXPath();
			xpath.setNamespaceContext(namespaceContext);
			Node node = (Node) xpath.evaluate(paths.get(key), document, XPathConstants.NODE);
			if(node == null) {
				throw new Exception("node not found, xpath is " + paths.get(key));
			}
			map.put(key, node.getTextContent());
		}
		return map;
	}
	
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

	private NamespaceContext getNamespaceContext() {
		return new NamespaceContext() {

			@Override
			public String getNamespaceURI(String arg0) {
				return namespace;
			}

			@Override
			public String getPrefix(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Iterator getPrefixes(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	
}
