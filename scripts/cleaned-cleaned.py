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
infile = sys.argv[1]+'.xml'
#infile = 'AllTogether.xml'
inFile = open(filedir+'/'+infile,'r')
outFile = open(filedir + '/cleaned.txt','w')
matchingRegex0 = re.compile("\s{0,30}\<ind\ {1,3}id\=\"?\ ?([0-9]{2}[0-9]{2,12})\"?"\
                            "\ {1,3}sex\=\"([MFU\?01])\""\
                            "\ {1,3}birth\=\"\?\""\
                            "\ {1,3}observed\=\"([0-9]{1,4}|\?)\""\
                            "\ {1,3}sizeClass\=\"([0-2?])\""\
                            "\ {1,3}geno",re.I)
                            
                            # 1- ID
                            # 2- year
                            # 3- sex
                            # 4- size
                            #5 wrong
                            # 6- genotype
c = 0
for line in inFile:
  match0 = matchingRegex0.match(line)
  
  if  match0:
    print line
    print >> outFile, "%s %s %s %s " %(match0.group(1),match0.group(3),match0.group(4),match0.group(2))

outFile.close() 
print c
