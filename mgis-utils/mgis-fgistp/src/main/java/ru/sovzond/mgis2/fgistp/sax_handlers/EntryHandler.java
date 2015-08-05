package ru.sovzond.mgis2.fgistp.sax_handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.sovzond.mgis2.fgistp.model.Entry;

public class EntryHandler extends DefaultHandler {
	private Entry entry;

	private String inProperty;
	private StringBuilder inPropertyBuilder;

	private StringBuilder entryIdStringBuilder;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//		System.out.println(localName + " " + qName);
		switch (qName) {
			case "entry":
				entry = new Entry();
				break;
			case "id":
				if (entry != null) {
					entryIdStringBuilder = new StringBuilder();
				}
				break;
			case "link":
				String title = attributes.getValue("title");
				switch (title) {
					case "Entry":
						entry.setId(attributes.getValue("href"));
						break;
					case "Children":
						break;
					default:
						break;
				}
				break;
			default:
				if (qName.startsWith("d:")) {
					inProperty = qName;
					inPropertyBuilder = new StringBuilder();
				}
				break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
			case "id":
				if (entry != null) {
					entry.setId(entryIdStringBuilder.toString());
					entryIdStringBuilder = null;
				}
				break;
			default:
				if (qName.startsWith("d:")) {
					if (inProperty != null) {
						String propertyName = qName;
						String propertyValue = inPropertyBuilder.toString();
						entry.getProperties().put(propertyName, propertyValue);
						inProperty = null;
						inPropertyBuilder = null;
					}
				}
				break;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (inPropertyBuilder != null) {
			inPropertyBuilder.append(ch, start, length);
		} else if (entryIdStringBuilder != null) {
			entryIdStringBuilder.append(ch, start, length);
		}
	}

	public Entry getParsedContent() {
		return entry;
	}
}
