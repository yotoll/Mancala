import javax.swing.JButton;



// Player class for shifting array based on player information and index selected (passed to the takeTurn method)
//		parent class to Computer class
public class Player
{
	
	protected int whichPlayer;				// stores whether or not this is player one or player two
	protected int store;					// index of this user's store in the array
	protected int opponentStore;			// index of the opponent's store in the array
	protected boolean freeTurn = false;		// true if this player gets another turn
	protected int points;					// stores the total number of points this player has collectd
	protected String name;					// stores the name of this player
	
	
	// set member variables passed in
	public Player(int player, int st, int oppSt)
	{
		whichPlayer = player;
		store = st;
		opponentStore = oppSt;	
		points = 0;
	}
	
	
	// l is the index of the array of buttons and array of integers (storing number of tokens per slot)
	public int takeTurn(int hand, int l, int [] arr, JButton [] slot, boolean CAPTURE, boolean AVALANCHE)
	{
		// get end index from selecting index l as a starting point
		int i = shiftBoard(hand,l,arr,slot,CAPTURE,AVALANCHE);
		
		// do not give the player another turn if the the last token does not in player's store
		if (i != store)
		{
			freeTurn = false;
		}
		else
			freeTurn = true;
		
		return i;	
	}
	

	// pass in the array and shift it's elements
	// pass in a copy of the array of buttons
	protected int shiftBoard(int hand, int l, int [] arr, JButton [] slot, boolean CAPTURE, boolean AVALANCHE)
	{
		
		l = shiftArray(hand,l,arr,slot,CAPTURE,AVALANCHE);
		
		
		if (CAPTURE)
		{
			Capture(arr,l);				
		}
		
		
		// continue shifting until last token lands in empty hollow or player's store
		if (AVALANCHE)
		{			
			while (arr[l] != 1 && arr[l] != 0 && l != store)
			{
				l = shiftArray(arr[l],l,arr,slot,CAPTURE,AVALANCHE);
			}
			
		}

		points = arr[store];
	
		return l;
		
		
		
	}
	
	
	protected int shiftArray(int hand, int l, int [] arr, JButton [] slot, boolean CAPTURE, boolean AVALANCHE)
	{
		
		int k = hand;
		
		// for the number of seeds in that hole
		for (int j = 0; j <= hand; j++)
		{
			// at first, pick up all the stone
			if (j == 0)
			{
				arr[l] = 0;
			}
			
			// otherwise, set that element to what it was before plus one
			else
			{
				if (l != opponentStore)
				{
					arr[l] = arr[l] + 1;
					
				
					// lost one stone
					k--;
					
				}
				else
					j--;
				

			}
			

			// go to the next element in the array
			l++;
			
			
			// if j is arr[i] - 1 and the hand (k) is not empty
			if (l == (arr.length) && k != 0)
			{
				l = 0;
			}

			
		}
		
		l--;
		
		return l;
		
		
		
	}
	
	
	
	protected int getOpponentIndex(int [] arr, int i)
	{
		/*
			this function returns the index opposite the given index in the array
			(the opponents corresponding index)
			
			this function uses an array to determine what should be added to the given index
			to get the opponent's corresponding index
			
			ex. for an array with size 14, index opposite to position 4 is 4 + 4 = 8,
				and the index opposite to position 5 is 5 + 2 = 7
				
				Array of size 14 (where 6 and 13 are the player "stores")
				0 1 2 3 4 5 	6 		7 8 9 10 11 12 			13

				i			   i    c[i]
				5 --> return = 5  +  2  =  7
				4 --> return = 4  +  4  =  8
				3 --> return = 3  +  6  =  9
				2 --> return = 2  +  8  =  10
				1 --> return = 1  +  10  =  11
				0 --> return = 0  +  12  =  12
		
		*/
		
		
		
		// contains values for what needs to be added to each corresponding 
		//	element in array to get opponent spot

		int [] c = new int[arr.length];
		int add = arr.length - 2;
		
		// first half
		for (int k = 0; k < 6; k++)
		{
			c[k] = add;
			add -= 2;
		}
		
		add -= 2;
		c[6] = 7;
		c[13] = -7;
		
		// second half of array
		for (int k = 7; k < 13; k++)
		{
			c[k] = add;
			add -= 2;
		}
		
		
		return (i + c[i]);
		
	}
	
	
	
	protected void Capture(int [] arr, int l)
	{
		int oppIndex;

		// check opponent index only if we've landed in an empty hollow on CORRECT SIDE
		//		and not landing in a player's "hole"
		if ((whichPlayer == 1 && l < store) || (whichPlayer == 2 && l > opponentStore))
		{
			if (arr[l] == 1 && l != store && l != opponentStore)
			{
				
				oppIndex = getOpponentIndex(arr,l);
				
				
				if (arr[oppIndex] != 0)
				{
					arr[l] = arr[l] + arr[oppIndex];
					arr[oppIndex] = 0;
					
					
					arr[store] = arr[store] + arr[l];
					arr[l] = 0;
					
					freeTurn = false;
				
				}
			}
		}

		
		
	}


	
}























