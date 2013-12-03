import re,sys,os,random

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



filedir = './'
infile = 'graph.dot'#the two files, which is first will effect colours
inFile = open(filedir+'/'+infile,'r')
outFile = open(filedir + '/graph-interesting.dot','w')
##the 4 regexes match chid nodes, child-parent connector nodes
#and then lines of various types
matchingRegex0 = re.compile('^\ {0,4}(\d{1,10})\ {1,3}\[\ {1,3}label.{20,180}\]\s{0,80}$')
matchingRegex1 = re.compile('^\ {0,4}(\d{1,10})\ {1,3}\-\>\ {1,3}(\d{1,10})\ {1,3}\[\ {1,3}weight\=[0-9]\ {1,3}\]\s{0,80}$')
######
#initial stuff
l = [] #this will be the list of nodes we care about
######
#put in all the nodes
for line in inFile:
  #match to see lines  
  match1 = matchingRegex1.match(line)
#  match1 = matchingRegex1.match(line)
#  match2 = matchingRegex2.match(line)
  #match3 = matchingRegex3.match(line)
  if match1:
  #if its a normal line, print it
  #  if match1.group(3) =='EE0000':
    l.append(match1.group(1)) 
    l.append(match1.group(2))
lLines = []
inFile = open(filedir+'/'+infile,'r')
for line in inFile:
    match = matchingRegex0.match(line)
    #match1 = matchingRegex1.match(line)
    if match:
      if match.group(1) in l:
        #print >> outFile, line,
        lLines.append(line)
    #elif match1:
    #  if match1.group(1) in l:
    #    print >> outFile, line,
    #    l.append(match1.group(2))
    #  else:
    #    if match1.group(2) in l:
    #      print >> outFile, line,
    #      l.append(match1.group(1))
    #else:
    #  print line,
    #  print >> outFile, line,

#randomly sort lLines
random.shuffle(lLines)
f = 0
inFile = open(filedir+'/'+infile,'r')
for line in inFile:
    match = matchingRegex0.match(line)
    #match1 = matchingRegex1.match(line)
    if match:
      if f==0:
        f = 1
        for l1 in lLines:
          print >> outFile, l1,
        #print >> outFile, line,
        #lLines.append(line)
    #elif match1:
    #  if match1.group(1) in l:
    #    print >> outFile, line,
    #    l.append(match1.group(2))
    #  else:
    #    if match1.group(2) in l:
    #      print >> outFile, line,
    #      l.append(match1.group(1))
    else:
      print line,
      print >> outFile, line,

outFile.close()

#infile = 'graph-big-interesting.dot'#the two files, which is first will effect colours
#inFile = open(filedir+'/'+infile,'r')
#outFile = open(filedir + '/graph-big-interestinger.dot','w')
#matchingRegex0 = re.compile('^\ {0,4}(\d{1,10})\ {1,3}\[\ {1,3}label.{20,180}\]\s{0,80}$')
#for line in inFile:
#  match = matchingRegex0.match(line)
#  if match:
#    if match.group(1) in l:
#      print >> outFile, line,
#  else:
#    print >> outFile, line,
#outFile.close()
