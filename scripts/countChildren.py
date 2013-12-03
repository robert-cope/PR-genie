import os,sys,re

#
#    Copyright 2013 Robert Cope cope.robert.c@gmail.com
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#
#



#x=sys.argv[1]
#print x
infile = "cleaned.txt"
infile2 = "graph-interesting.dot"
outfile = "ChildrenCountX.txt"
outFile = open("./"+outfile,'w')
#outFile2 = open("./"+outfile2,'w')
matchingRegex = re.compile("^\s{0,5}([0-9]{2,10})\s{1,3}([0-9]{1,5}|\?)\ {1,6}([0-1\?])\ {1,5}([MmFfUu\?01])")
matchingRegex1=re.compile("^\s{0,5}([0-9]{2,10})\ {1,3}\-\>\ {1,3}([0-9]{2,10})\ {1,3}\[\ {1,3}we")
#0 [ label="0", shape=circle, width=0.000000, height=0.000000, fillcolor="black", fontcolor="white" ]
inFile = open("./"+infile,'r')
#f2=1 #ie. is there anything with children? 0 means yes
tot = 0
for line in inFile:
    matchObject=matchingRegex.match(line)
    if matchObject:
        c=0
        s = ''
        inFile2 = open("./"+infile2,'r')
        for line2 in inFile2:
            matchObject1 = matchingRegex1.match(line2)
            if matchObject1:
                if (matchObject1.group(1)==matchObject.group(1)):
                    c=c+1
                    s = s + matchObject1.group(2)+' '
        if c>0:
            tot = tot + 1
            #ie. it has no children, ie. exists no e such that this is a parent of e
            print >> outFile, "%s (%s) %s : %s"%(matchObject.group(1),matchObject.group(4), str(c),s)
    #else:
    #    print >> outFile, line,
###################################################
print >> outFile, "Total: %d "%(tot)
outFile.close()
        
#if f2==0:
#    python splitChildrenMod.py x+1
