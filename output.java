//File output

FileWriter savegame = new FileWriter(<insert name>);
//Check to make sure if file exists or not
//If exists, confirm overwrite
if(savegame.exists())
{
	//output confirm overwrite with buttons
}

//Output stuff

save.write(<p1 name> + "\n");
save.write(<p2 name>+ "\n");
save.write(<ruleset>+"\n");
save.write(<theme>+"\n");
for(int i = 0; i < boardsize; i++)
{
	save.write(rows[i]);
	save.write("\n");
}