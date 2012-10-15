package com.rjxde0.zhongjiang1.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rjxde0.zhongjiang1.entity.NewsInfo;

public class NewsContentHandler extends DefaultHandler{
	private List<NewsInfo> infos;
	private NewsInfo newsInfo;
	private String tagName;
	
	public NewsContentHandler(List<NewsInfo> infos) {
		super();
		this.infos = infos;
	}

	public List<NewsInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<NewsInfo> infos) {
		this.infos = infos;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String temp = new String(ch, start, length);
		if(tagName.equals("id")){
			newsInfo.setId(temp);
		}else if(tagName.equals("title")){
			newsInfo.setNewsTitle(temp);
		}else if(tagName.equals("date")){
			newsInfo.setNewsDate(temp);
		}else if(tagName.equals("link")){
			newsInfo.setLink(temp);
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
			infos.add(newsInfo);
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
			newsInfo = new NewsInfo();
		}
	}
	
}
