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
infile = 'cleaned.txt'
infile2 = 'extracted-c.txt'
inFile = open(filedir+'/'+infile,'r')
outFile = open(filedir + '/results-c.txt','w')

matchingRegex = re.compile("^\s{0,20}(\d{1,10})\ {0,4}(\d{0,6})\s{0,4}(\d{0,6})\ {1,3}(\d{1,3}|\?)(?:\ {1,3}(\d{1,3}|\?)\ {1,3}([MmFfUu\?]))?")
#matchingRegex2 = re.compile("^\s{0,20}\[\((\d{1,6})\)\ {1,4}[x0-9]{1,6}(?:\,\ {1,3}\(\d{1,6}\,\ {0,3}\((\d{1,6})\,(\d{1,6})?\)\)\ {0,4}\[[0-9\.E]{1,20}\]\ {0,3}\[[\-10UMF]{1,6}\]\ {1,3}[x0-9]{1,6})?\]\s{0,80}$")
matchingRegex2 = re.compile("^\s{0,5}\[(?:\(([0-9]{1,10})(?:,\(([0-9]{1,10})\,(?:([0-9]{1,10}))?\))?\)(?:\[([0-9\.E]{1,28})\](\[[01\-MFU]{3,7}\]))?\ ([a-zA-Z0-9]{1,12})(?:\,\ {0,3})?)(?:\(([0-9]{1,10})(?:,(\([0-9]{1,10}\,(?:[0-9]{1,10})?\)))?\)(?:\[([0-9\.E]{1,28})\](\[[01\-MFU]{3,7}\]))?\ ([a-zA-Z0-9]{1,12})(?:\,\ {0,3})?)?(?:\(([0-9]{1,10})(?:,(\([0-9]{1,10}\,(?:[0-9]{1,10})?\)))?\)(?:\[([0-9\.E]{1,28})\](\[[01\-MFU]{3,7}\]))?\ ([a-zA-Z0-9]{1,12})(?:\,\ {0,3})?)?\]")
matchingRegex3 = re.compile("^\s{0,20}\((\d{1,10})\,(\d{1,10})?\)\ {1,4}([0-9E\.]{1,28})\ {1,4}\[[10\-UMF]{1,10}\]\s{0,80}$")
matchingRegex4 = re.compile("^\s{0,20}\[\((\d{1,10})\,\ {0,3}\((\d{1,10})\,(?:\ {0,3}(\d{1,10}))?\)\)\[([0-9\.E]{0,25})\]\[[01MF\-]{1,10}\]\ {1,3}[0-9x]{1,10}(?:\,\ {0,3}\(\d{1,10}\)\ {0,3}[x0-9]{1,10})?\]\s{0,80}$")
for line in inFile:
#match them all
  match = matchingRegex.match(line)
  if match:
    prev = ''
    last = ''
    inFile2 = open(filedir+'/'+infile2,'r')
    for line2 in inFile2:
      match2 = matchingRegex2.match(line2)
      match4 = matchingRegex4.match(line2)
      if match2:
        prev = match2.group(1)
        if match.group(1)==match2.group(1):
          print>> outFile, line,
          if (match2.group(2) == match.group(2) or match2.group(3) == match.group(2)) and last != thing:
            print >> outFile, "%s %s %s ok" %(match.group(1), match.group(2), match2.group(4))
            last = "%s %s" %(match.group(1), match.group(2)) 
          elif (match2.group(2) == match.group(3) or match2.group(3) == match.group(3)) and last != thing2:
            print >> outFile, "%s %s %s ok" %(match.group(1), match.group(3),match2.group(4))
            last = "%s %s" %(match.group(1), match.group(3)) 
          if match2.group(2)!=None and match2.group(2) != match.group(2) and match2.group(2) != match.group(3):
            print>>outFile, "%s %s %s not ok"%(match.group(1), match2.group(2),match2.group(4))
          if match2.group(3)!=None and match2.group(3) != match.group(2) and match2.group(3) != match.group(3):
            print>>outFile, "%s %s %s not ok"%(match.group(1), match2.group(3),match2.group(4))
      elif match4:
        prev = match4.group(1)
        if match.group(1)==match4.group(1):
          print>> outFile, line,
          if (match4.group(2) == match.group(2) or match4.group(3) == match.group(2)) and last != thing:
            print >> outFile, "%s %s %s ok" %(match.group(1), match.group(2),match4.group(4))
            last = "%s %s" %(match.group(1), match.group(2)) 
          if (match4.group(2) == match.group(3) or match4.group(3) == match.group(3)) and last != thing2:
            print >> outFile, "%s %s %s ok" %(match.group(1), match.group(3),match4.group(4))
            last = "%s %s" %(match.group(1), match.group(3)) 
          if match4.group(2)!=None and match4.group(2) != match.group(2) and match4.group(2) != match.group(3):
            print>>outFile, "%s %s %s not ok"%(match.group(1), match4.group(2),match4.group(4))
          if match4.group(3)!=None and match4.group(3) != match.group(2) and match4.group(3) != match.group(3):
            print>>outFile, "%s %s %s not ok"%(match.group(1), match4.group(3),match4.group(4))

      if prev == match.group(1):
        match3 = matchingRegex3.match(line2)
        if match3:
          thing = "%s %s"%(match.group(1), match.group(2))
          thing2 = "%s %s"%(match.group(1), match.group(3))
          if (match3.group(1) == match.group(2) or match3.group(2) == match.group(2)) and last != thing:
            print >> outFile, "%s %s %s ok" %(match.group(1), match.group(2),match3.group(3))
            last = "%s %s" %(match.group(1), match.group(2)) 
          if (match3.group(1) == match.group(3) or match3.group(2) == match.group(3)) and last != thing2:
            print >> outFile, "%s %s %s ok" %(match.group(1), match.group(3),match3.group(3))
            last = "%s %s" %(match.group(1), match.group(3))
          if match3.group(1) != match.group(2) and match3.group(1) != match.group(3):
            print>>outFile, "%s %s %s not ok"%(match.group(1), match3.group(1),match3.group(3))
    print>> outFile, ''
      #print >> outFile, "%s %s %s" %(match.group(1), match.group(2), match.group(3))
  #start the first document and the loci list
  #close everything
outFile.close()
