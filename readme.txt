====NEWSPARSER====

NewsParser was a program that I used to research
on media discussions of Cold War genocides in summer of 2012.

The current version takes a PDF file of LexisNexis search result
articles as well as a set of keywords. It shows the frequency of
each keyword over the date range of the body of articles you feed it.


Important notes:

 * Each article must be separated by a "<NEW ARTICLE>" tag
 * NewsParser currently doesn't address gaps in years of your body of articles. 
	Eg if your articles are from 1982, 1983, and 1985, the output from the program
 	Will NOT show 1984 at all. I will fix this soon.
 * User has full control over the PDF address everytime the program runs. 
	In order to change the output address, alter the directoryAddress variable in PRINTALL method
	of PressRange()
