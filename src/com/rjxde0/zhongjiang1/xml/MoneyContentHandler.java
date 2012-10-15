package com.rjxde0.zhongjiang1.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rjxde0.zhongjiang1.entity.MoneyProjectInfo;

public class MoneyContentHandler extends DefaultHandler{
	private List<MoneyProjectInfo> infos;
	private MoneyProjectInfo projectInfo;
	private String tagName;
	
	public MoneyContentHandler(List<MoneyProjectInfo> infos) {
		super();
		this.infos = infos;
	}
	public List<MoneyProjectInfo> getInfos() {
		return infos;
	}
	public void setInfos(List<MoneyProjectInfo> infos) {
		this.infos = infos;
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String temp = new String(ch, start, length);
		if(tagName.equals("id")){
			projectInfo.setId(temp);
		}else if(tagName.equals("title")){
			projectInfo.setProjectTitle(temp);
		}else if(tagName.equals("link")){
			projectInfo.setProjectLink(temp);
		}
	}
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(qName.equals("resource")){
			infos.add(projectInfo);
		}
		tagName = "";
	}
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		this.tagName = localName;
		if(tagName.equals("resource")){
			projectInfo = new MoneyProjectInfo();
		}
	}

}




