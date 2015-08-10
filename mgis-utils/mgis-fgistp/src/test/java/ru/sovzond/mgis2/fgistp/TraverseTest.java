package ru.sovzond.mgis2.fgistp;

import org.junit.Test;
import ru.sovzond.mgis2.fgistp.db_handlers.DatabasePersister;
import ru.sovzond.mgis2.fgistp.db_handlers.DirectoryTraverser;
import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class TraverseTest {
	@Test
	public void testTraverse() throws IOException, JAXBException, ParseException, SQLException {
		DataSourceContainer container = new DataSourceContainer(
				"ds1",
				"127.0.0.1",
				5432,
				"id_space_adygea",
				"id_space_adygea",
				"id_space_adygea"
		);
		DatabasePersister persister = new DatabasePersister(container);
		DirectoryTraverser traverser = new DirectoryTraverser(persister);
		traverser.go("/home/asd/fgistp3/Республика Адыгея/");
	}

	@Test
	public void testParseDate() throws ParseException {
		String str = "2011-10-10T00:00:00";
		Date date = new SimpleDateFormat("y-M-d'T'H:m:s").parse(str);
		System.out.println(date.toString());
	}
}
