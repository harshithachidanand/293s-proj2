f = open('input/lines-trec45.txt','r')

counter = 1

for line in f:
	filename = 'input2/doc' + str(counter) + '.txt'
	g = open(filename,'w')
	g.write(line)
	g.close()
	counter += 1

f.close()