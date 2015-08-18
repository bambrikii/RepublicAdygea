package ru.sovzond.mgis2.fgistp.sax_handlers;

import ru.sovzond.mgis2.fgistp.model.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class EntryMarshaller {
	public static Entry unmarshall(InputStream is) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Entry.class);
		Unmarshaller m = context.createUnmarshaller();
		return (Entry) m.unmarshal(is);
	}
}
