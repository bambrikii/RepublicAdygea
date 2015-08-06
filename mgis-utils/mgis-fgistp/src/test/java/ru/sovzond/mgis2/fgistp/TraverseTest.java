package ru.sovzond.mgis2.fgistp;

import org.junit.Test;
import ru.sovzond.mgis2.fgistp.db_handlers.DirectoryTraverser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class TraverseTest {
	@Test
	public void testTraverse() throws IOException, JAXBException {
		DirectoryTraverser traverser = new DirectoryTraverser();
		traverser.go("/home/asd/fgistp3/Республика Адыгея/");
	}
}
