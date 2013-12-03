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
infile = sys.argv[1]
os.system('python cleaned-cleaned.py %s'%(infile))
os.system('python sort-rel.py %s'%(infile))
os.system('python condense-rel.py %s'%(infile))
os.system('python finish-graph.py %s'%(infile))
os.system('python make-graph.py %s'%(infile))
os.system('python make-graph-interestingbits.py %s'%(infile))
os.system('python countChildren.py %s'%(infile))
