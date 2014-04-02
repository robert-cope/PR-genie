
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import java.util.*;
import java.io.*;





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


// Reader based on the document oject traversal, described here:
// http://www.w3.org/TR/DOM-Level-2-Traversal-Range/traversal.html


/*
 * reads in a pedigree (well, a population)
 * takes a population in xml (because xml is well behaved)
 * */

public class PedigreeReader {

    private Document document;
    private DocumentTraversal trDocument;

    public PedigreeReader(String filename) throws IOException {
        //constructor - set up appropriate objects etc.
        String fname = filename.substring(filename.lastIndexOf("\\") + 1);
        String[] splitName = fname.split("\\.");
        if (splitName.length == 2 && splitName[1].equalsIgnoreCase("xml")) {
            document = SchedulerUtil.readXML(filename);
            //System.out.println(document.getImplementation()
                //.hasFeature("Traversal", "2.0"));
            trDocument = (DocumentTraversal) document;
        } else {
            throw new IOException("Invalid file type: "
                    + fname + "\nCan only read xml files.");
        }
    }

    public void getPedigree(Pedigree P) {
        //goes through the lines, works out nodes and stuff,
        //puts stuff in the right places, etc.
        //not very exciting
        NodeIterator iter = trDocument.createNodeIterator(document,
                NodeFilter.SHOW_ELEMENT, null, true);
        Node node = iter.nextNode();
        boolean pop = false;
        while (node != null) {
            //System.out.println(node.toString());
            if (node.getNodeName().equalsIgnoreCase("population")) {
                pop = true;
                //System.out.println("pedigree detected");
            }
            if (pop && node.getNodeName().equalsIgnoreCase("loci")) {
                //System.out.println("loci list found");
                NamedNodeMap nodeAtts = node.getAttributes();
                int nloci = Integer.parseInt(nodeAtts.item(0).getNodeValue());
                NodeList loci = node.getChildNodes();
                for (int i = 0; i < loci.getLength(); i++) {
                    Node iLoci = loci.item(i);
                    //System.out.println(iLoci);
                    NamedNodeMap lociAtts = iLoci.getAttributes();
                    //String lname = lociAtts.item(0).getNodeValue();
                    //do we even really care about loci names?
                    //not at the moment really
                    //but we should parse it anyway at least
                }
            } else if (pop
                    && node.getNodeName().equalsIgnoreCase("individuals")) {
                //System.out.println("individual list found");
                NamedNodeMap nodeAtts = node.getAttributes();
                int nInd = Integer.parseInt(nodeAtts.item(0).getNodeValue());
                P.setN(nInd);
                //System.out.println(nInd);
                NodeList individuals = node.getChildNodes();
                //System.out.println(individuals.toString());
                //System.out.println(individuals.getLength());
                for (int i = 0; i < individuals.getLength(); i++) {
                    Node ind = individuals.item(i);
                    //System.out.println(ind);
                    NamedNodeMap indAtts = ind.getAttributes();
                    String iId = "", sex = "", birth = "", gen = "", obsTime = "", sizeClass = "";
                    //System.out.println(indAtts + " orly");
                    if (indAtts != null) {
                        for (int l = 0; l < indAtts.getLength(); l++) {
                            Node att = indAtts.item(l);
                            //System.out.println(att.getNodeName());
                            if (att.getNodeName().equalsIgnoreCase("id")) {
                                iId = att.getNodeValue();
                            } else if (att.getNodeName()
                                    .equalsIgnoreCase("sex")) {
                                sex = (att.getNodeValue());
                            } else if (att.getNodeName()
                                    .equalsIgnoreCase("birth")) {
                                birth = (att.getNodeValue());
                            } else if (att.getNodeName()
                                    .equalsIgnoreCase("observed")) {
                                obsTime = (att.getNodeValue());
                            } else if (att.getNodeName()
                                    .equalsIgnoreCase("sizeClass")) {
                                sizeClass = (att.getNodeValue());
                            } else if (att.getNodeName()
                                    .equalsIgnoreCase("genotype")) {
                                gen = (att.getNodeValue());
                            }
                        }

                        gen = gen.trim();
                        //System.out.println(gen);
                        String[] genotype = gen.split(" ");
                        //System.out.println(genotype);
                        Individual newInd = new Individual(iId,
                                sex, birth, obsTime, sizeClass, genotype);
                        //newInd.print();
                        P.add(newInd);
                    }
                }
            }
            node = iter.nextNode();
        }
        //P.print();
    }
}
