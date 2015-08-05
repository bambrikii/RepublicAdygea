package ru.sovzond.mgis2.fgistp.sax_handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.sovzond.mgis2.fgistp.model.Entry;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
public class FeedHandler extends DefaultHandler {

	private Entry feed;
	private Entry entry;

	private StringBuilder feedIdStringBuilder;
	private StringBuilder entryIdStringBuilder;

	private String inProperty;
	private StringBuilder inPropertyBuilder;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
			case "feed":
				feed = new Entry();
				break;
			case "entry":
				entry = new Entry();
				break;
			case "id":
				if (entry != null) {
					entryIdStringBuilder = new StringBuilder();
				} else {
					feedIdStringBuilder = new StringBuilder();
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
			case "d:NAME":
				break;
			default:
				if (qName.startsWith("d:")) {
					inProperty = qName;
					inPropertyBuilder = new StringBuilder();
//					entry.getProperties().put(qName.substring(2, qName.length()), attributes.getValue(""));
				}
				break;
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
			case "feed":
				break;
			case "entry":
				feed.getEntries().add(entry);
				entry = null;
				break;
			case "id":
				if (entry != null) {
					entry.setId(entryIdStringBuilder.toString());
					entryIdStringBuilder = null;
				} else if (feedIdStringBuilder != null) {
					feed.setId(feedIdStringBuilder.toString());
					feedIdStringBuilder = null;
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
		} else if (feedIdStringBuilder != null) {
			feedIdStringBuilder.append(ch, start, length);
		}
	}

	public Entry getContent() {
		return feed;
	}
}
