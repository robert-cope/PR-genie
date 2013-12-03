import re,sys,os

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
infile = sys.argv[1]+'-out-reduced.txt'
inFile = open(filedir+'/'+infile,'r')
outFile = open(filedir + '/extracted-s.txt','w')
matchingRegex0 = re.compile("\[(?:\(([0-9]{1,10})(?:,\(([0-9]{1,10})\,(?:[0-9]{1,10})?\))?\)(?:\[[0-9\.E]{1,28}\]\[[01\-MFU]{3,7}\])?\ ([a-zA-Z0-9]{1,12})(?:\,\ {0,3})?){1,3}\]")
pop=[]
names=[]
for line in inFile:
  match0 = matchingRegex0.match(line)
  if match0:
    if line not in pop:
      pop.append(line)
      names.append(match0.group(1))
    #start the first document and the loci list
    #print >> outFile, line,
pop.sort(key=lambda names: names)
for line in pop:
  print>> outFile, line,
outFile.close() 

