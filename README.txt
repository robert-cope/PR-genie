################################################
The included code is liscenced under GPL, see attached 
Copyright 2013 Robert Cope cope.robert.c@gmail.com
################################################

This program, PR-genie, is designed to reconstruct pedigrees for wildlife populations based on genetic and size/maturity-class data.
Details appear in this paper:


Cope RC, Lanyon JM, Seddon JM, Pollett PK (2014) Development and testing of a genetic marker based pedigree reconstruction system ''PR-genie'' incorporating size-class data. Molecular Ecology Resources. DOI: 10.1111/1755-0998.12219


Population simulation code (i.e., for testing) exists is also available. See https://github.com/robert-cope/popSimulation1.git


################################################

1. PrGenie
The src folder contains java source code for the PR-Genie program. This must be compiled in Java 1.6, and then called from the command line.
The compiled program takes as input the location of an .xml file containing the population data, e.g. in windows:
java PrGenie G:\your\directory \sim-folder\data \allelesX\testY
It returns, in the same location, a text file [name]-out-reduced.txt.

2. .xml input file
The xml file details the number of loci, the number of individuals, and the parameters of each individual (sex, size, genotype, etc.). An example is provided (see below).

3. Output
The output text file gives the progression of the CE algorithm at each step, the assigned relationships for each individual in the calculated maximum likelihood pedigree, and the allele frequencies for each loci.
e.g.:
[(201527,(201379,201218))[262645.86179245467][MMF] x1]
This indicates that individual 201527 (a Male) was assigned parents 201379 and 201218 (M and F respectively).

4. Post processing
The scripts folder contains some useful python scripts for extracting basic information from the pedigree. The scripts tend to work together, running processAll.py will make everything work in the correct order.
e.g.
python processAll.py test100-40-40-0
This requires *both* the .xml file and the PR-Genie -out-reduced.txt file.
The primary interesting outputs of these scripts are "ChildrenCountX.txt", which lists the offspring assigned for each parent,
and "graph-interesting.dot", which is formatted to be read in graphviz or similar software and displays a directed graph of the pedigree.
In this graph, rectangles indicate male individuals, circles indicate females, black nodes are individuals recorded as adults, blue are subadults, and green are calves.

5. Example.
The example folder contains an example .xml simulated data set, with 25 loci and 150 individuals. The scripts/exOutput/ folder contains an output maximum likelihood pedigree along with the files resulting from running the utility scripts on this output. A pdf of the .dot graph is also provided.
