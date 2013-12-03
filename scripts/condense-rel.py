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
#infile = 'test'+sys.argv[1]+'-extracted-s.txt'
infile = 'extracted-s.txt'
inFile = open(filedir+'/'+infile,'r')
#outFile = open(filedir + '/test'+sys.argv[1]+'-extracted-c.txt','w')
outFile = open(filedir + '/extracted-c.txt','w')
matchingRegex0 = re.compile("^\s{0,5}\[(?:\(([0-9]{1,10})(?:,(\([0-9]{1,10}\,(?:[0-9]{1,10})?\)))?\)"\
                            "(?:\[([0-9\.E]{1,28})\](\[[01\-MFU]{3,7}\]))?\ ([a-zA-Z0-9]{1,12})"\
                            "(?:\,\ {0,3})?)(?:\(([0-9]{1,10})(?:,(\([0-9]{1,10}\,(?:[0-9]{1,10})?\)))?\)"\
                            "(?:\[([0-9\.E]{1,28})\](\[[01\-MFU]{3,7}\]))?\ ([a-zA-Z0-9]{1,12})(?:\,\ {0,3})?)?"\
                            "(?:\,\ {0,3})?(?:\(([0-9]{1,10})(?:,(\([0-9]{1,10}\,(?:[0-9]{1,10})?\)))?\)"\
                            "(?:\[([0-9\.E]{1,28})\](\[[01\-MFU]{3,7}\]))?\ ([a-zA-Z0-9]{1,12})(?:\,\ {0,3})?)?"\
                            "(?:\,\ {0,3})?(?:\(([0-9]{1,10})(?:,(\([0-9]{1,10}\,(?:[0-9]{1,10})?\)))?\)"\
                            "(?:\[([0-9\.E]{1,28})\](\[[01\-MFU]{3,7}\]))?\ ([a-zA-Z0-9]{1,12})(?:\,\ {0,3})?)?\]")
pop=[]
name=''
current=''
for line in inFile:
  match0 = matchingRegex0.match(line)
  if match0:
    if match0.group(1)!=name:
      pop.append(current)
      name = match0.group(1)
      current=line
    else:
      if match0.group(2):
        current= current +' '+match0.group(2) +' '+ match0.group(3)+' '+match0.group(4)+'\n'
      if match0.group(7):
        print line
        print match0.group(7)
        current= current +' '+match0.group(7) +' '+ match0.group(8)+' '+match0.group(9)+'\n'
      if match0.group(11) and match0.group(12):
        print line
        print match0.group(11)
        current= current +' '+match0.group(11) +' '+ match0.group(12)+' '+match0.group(13)+'\n'
      if match0.group(15) and match0.group(16):
        print line
        print match0.group(15)
        current= current +' '+match0.group(15) +' '+ match0.group(16)+' '+match0.group(17)+'\n'
    #start the first document and the loci list
    #print >> outFile, line,
#pop.sort(key=lambda names: names)
pop.append(current)
for line in pop:
  #print line
  print>> outFile, line,
outFile.close() 

