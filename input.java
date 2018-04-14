//File input

FileReader loadgame = new FileReader(<insert name>);
//Check to make sure file exists
while(!(loadgame.exists()))
{
	//Output invalid file; request new one
	loadgame = new FileReader(<insert name>);
}//Loop until they give a valid file



p1name =
p2name = 
rules = 
theme = 
for(int i = 0; i < boardsize; i++)
{
	rows[i] = loadgame.read();
}