
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.*;
import org.w3c.dom.*;




//
//    Copyright 2013 Robert Cope cope.robert.c@gmail.com
//
//    This file is part of PR-genie.
//
//    PR-genie is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    PR-genie is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with PR-genie.  If not, see <http://www.gnu.org/licenses/>.
//

// The document handling here was adapted from the Java EE 5 Tutorial DOMSrcExample
// see e.g. here: http://docs.oracle.com/cd/E19159-01/819-3669/6n5sg7bt5/index.html






public class SchedulerUtil {
	/* Returns a Document object containing the xml elements
     * @param String filename: the name of the xml file to be read
     */

    public static Document readXML(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(filename);
        } catch (SAXException sxe) {
            // Error generated during parsing
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
            }
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        }
        return document;
    }

    /* Returns a string of tabs.
     * @param int tabs: the number of tabs include in the string
     */
    public static String printTabs(int tabs) {
        String stTabs = "";
        for (int i = 0; i < tabs; i++) {
            stTabs += "    ";
        }
        return stTabs;
    }
}
