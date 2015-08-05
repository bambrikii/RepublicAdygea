package ru.sovzond.mgis2.fgistp.sax_handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.sovzond.mgis2.fgistp.model.Entry;

public class EntryHandler extends DefaultHandler {
	private Entry ktd;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		System.out.println(localName + " " + qName);
		switch (qName) {
			case "entry":
				ktd = new Entry();
				break;
			case "link":
				String title = attributes.getValue("title");
				switch (title) {
					case "Entry":
						ktd.setId(attributes.getValue("href"));
						break;
					case "Children":
						break;
					default:
						break;
				}
				break;
			default:
				if (qName.startsWith("d:NAME")) {
					// TODO:
				} else if (qName.startsWith("d:")) {
					// TODO:
					ktd.getProperties().put(qName, attributes.getValue(""));
				}
				break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
	}

	public Entry getParsedContent() {
		return ktd;
	}
}
