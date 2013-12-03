import re,sys,os,math

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
infile = 'results-c.txt'#the two files, which is first will effect colours
inFile = open(filedir+'/'+infile,'r')
outFile = open(filedir + '/graph.dot','w')
##the 4 regexes match chid nodes, child-parent connector nodes
#and then lines of various types
matchingRegex0 = re.compile('^\ {0,4}(\d{1,10})(?:\ {1,4}(\d{1,6})(?:\ {1,4}(\d{1,6}))?)?\ {1,4}(\d{2,4}|\?)(?:\ {1,3}(\d{1,4})\ {1,3}([MmFfUu\?01]))?\s{0,80}$')
matchingRegex1 = re.compile('^\ {0,4}(\d{1,10})(?:\ {1,4}(\d{1,10})(?:\ {1,4}(\d{1,10}))?)\s{1,4}([0-9\.]{1,28})(?:E([0-9]{1,3}))?\ {1,3}ok\s{0,80}$')
matchingRegex2 = re.compile('^\ {0,4}(\d{1,10})(?:\ {1,4}(\d{1,10})(?:\ {1,4}(\d{1,10}))?)\s{1,4}([0-9\.]{1,28})(?:E([0-9]{1,3}))?\ {1,3}not\ {1,3}ok\s{0,80}$')
l = []
l2=[]
ranks=[]
f = 1
f2=0
######
#initial stuff
print >> outFile, 'digraph GRAPH_0 {'
print >> outFile, 'edge [ dir=arrow];'
print >> outFile, 'graph [ rankdir = TB ];'
print >> outFile, 'ratio = auto;'
print >> outFile, 'mincross = 2.0;'
print >> outFile, 'node ['
print >> outFile, '    fontsize=11,'
print >> outFile, '    fillcolor=white,'
print >> outFile, '    style=filled,'
print >> outFile, '];'
print >> outFile, 'subgraph cluster_0 {'
print >> outFile, 'label = \"\"'
######
#put in all the nodes
for line in inFile:
  #match to see lines  
  match0 = matchingRegex0.match(line)
#  match1 = matchingRegex1.match(line)
#  match2 = matchingRegex2.match(line)
  #match3 = matchingRegex3.match(line)
  if match0:
  #if its a normal line, print it
    rnk=5
    if match0.group(4)!='?':
      rnk=int(match0.group(4))
      ranks.append(rnk)
      
    col="black"
    if match0.group(5)=='2':
      col="green"
    elif match0.group(5)=='1':
      col="blue"
    if match0.group(6)=='M' or match0.group(6)=='m' or match0.group(6)=='1':
      print >> outFile, '%s [ label=\"%s\", rank=%s, shape=rectangle, width=0.000, height=0.000, fillcolor=\"%s\", fontcolor=\"white\" ]'%(match0.group(1), match0.group(1),str(int(math.floor(rnk/5))),col)
    elif match0.group(6)=='F' or match0.group(6)=='f' or match0.group(6)=='0':
      print >> outFile, '%s [ label=\"%s\", rank=%s, shape=circle, width=0.000, height=0.000, fillcolor=\"%s\", fontcolor=\"white\" ]'%(match0.group(1), match0.group(1),str(int(math.floor(rnk/5))),col)
    else:
      print >> outFile, '%s [ label=\"%s\", rank=%s, shape=diamond, width=0.000, height=0.000, fillcolor=\"%s\", fontcolor=\"white\" ]'%(match0.group(1), match0.group(1),str(int(math.floor(rnk/5))),col)
print >> outFile, "}"
matchingRegex0 = re.compile('^\ {0,4}(\d{1,6})(?:\ {1,4}(\d{1,6})(?:\ {1,4}(\d{1,6}))?)\ {1,4}\d{2,3}\s{0,80}$')
inFile = open(filedir+'/'+infile,'r')
done = []
for line in inFile:
  if line not in done:
    match = matchingRegex0.match(line)
    match1 = matchingRegex1.match(line)
    match2 = matchingRegex2.match(line)
    if match:
      print >> outFile, '%s -> %s [ weight=2 ]'%(match.group(2),match.group(1))
      if match.group(3)!=None:
        print >> outFile, '%s -> %s [ weight=2 ]'%(match.group(3),match.group(1))
    elif match1:
      print >> outFile, '%s -> %s [ weight=2 ]'%(match1.group(2),match1.group(1))
      if match1.group(3)!=None:
        print >> outFile, '%s -> %s [ weight=2 ]'%(match1.group(3),match1.group(1))
    elif match2:
      print >> outFile, '%s -> %s [ weight=2 ]'%(match2.group(2),match2.group(1))
      if match2.group(3)!=None:
        print >> outFile, '%s -> %s [ weight=2 ]'%(match2.group(3),match2.group(1))
    done.append(line)


print >> outFile, '}'
outFile.close()
