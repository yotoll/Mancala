import java.util.Random;
import javax.swing.JButton;


// class for completing computer specific operations on the board array for Mancala
public class Computer extends Player
{
	
	// use parent constructor for setting which player number this is, the store number in the array and the
	//opponent store number
	public Computer(int player, int st, int oppSt)
	{
		super(player,st,oppSt);
	}
	
	
	// take turn method for computer
	public int takeTurn(int hand, int l, int [] arr, JButton [] slot, boolean CAPTURE, boolean AVALANCHE)
	{
		// get the index of the array position to move/start from
		int index = ComputerMove(arr, slot, CAPTURE, AVALANCHE);
		
		// use parent takeTurn method  a move on index
		index = super.takeTurn(arr[index], index, arr, slot, CAPTURE, AVALANCHE);
	
		return index;
	}
	
	// this function determines which index the computer will start from
	public int ComputerMove(int [] arr, JButton [] slot, boolean CAPTURE, boolean AVALANCHE)
	{
		
		// determine which set of indexes the computer is allowed to chose from
		// 		start is the store of this user minus half the possible slots to choose from (two store hollows do not count)
		int start = store - ((arr.length - 2) / 2);
		
		/* 
			The purpose of this array is to mark each position a valid or invalid position for the computer to chose from
				each valid move is marked 1 and each invalid move is marked -1. When complete, the computer choses
				randomly from the valid positions
		*/
		int [] indexes = new int[arr.length];
		
		int l;										// index
		int position;								// position to check whether or not it is empty
		boolean canCapture = false;					// set to true if there are indexes that can capture
		boolean anotherTurnIndex = false;			// set to true if there exists an index that will give
													//		the computer another turn
		boolean avalancheWithAnotherTurn = false;	// set to true if playing in avalanche mode and it's possible
													//		for the computer to get another turn
													
													
		Random random = new Random();
		
		int index = -1;
			
		// if playing in capture mode
		if (CAPTURE)
		{
			// analyze the slots on this player's side of the board
			for (int i = start; i < store; i++)
			{
				// determine where the last token will land if this index is chosen
				// if our last index was the player's store, this index will give the computer another turn
				if ((arr[i] + i) == store)
				{
					// set this index to be valid
					indexes[i] = 1;
					
					// mark that it is possible for the user to get another turn
					anotherTurnIndex = true;
				}
				
				// otherwise mark the position invalid
				else
					indexes[i] = -1;

			}
			
			// if the user can get another turn chose randomly from any positions that will do so
			if (anotherTurnIndex)
			{
				// chose a random index until arr[i] does not equal -1
				do
				{
					index = random.nextInt(indexes.length);
				} while(indexes[index] != 1);
			}
				

			// if there are no plays that will give the user another turn
			// chose randomly from any position that will allow the player to capture the opponent's tokens
			else
			{				
				// find the end position of each index
				for (int i = start; i < store; i++)
				{
					// get the index of where the last seed will land if starting from index i
					position = GetIndex(arr, i); 

					// determine if position is in range					
					if (position >= start && position < store)
					{
						// if the potential end position at this index is empty and there are seeds in opp.'s hollow
						if ((arr[i] != 0) && (arr[position] == 0) && (arr[getOpponentIndex(arr,position)] != 0))
						{
							// set index to valid
							indexes[i] = 1;
							
							// mark that it is possible to capture the opponent's tokens
							canCapture = true;
						}
						
						// otherwise mark as invalid
						else
							indexes[i] = -1;
					}
				}
			
				// if it is possible to capture the opponent's tokens, chose randomly from any position that will do so
				if (canCapture)
				{
					// chose a random index until arr[i] does not equal 1
					do
					{
						index = random.nextInt(indexes.length);
					} while(indexes[index] != 1);
					
					freeTurn = false;
				}
		
			}
		}
		
		// if playing in avalanche mode
		else if (AVALANCHE)
		{
			//no capturing				
			// if there is a move that will give the computer another turn, take it
			// otherwise chose randomly between any spot that does not contain zero tokens
			// create a temporary array of integers and buttons to work with
			
			
			// set each slot to initially be -1
			for (int i = 0; i < indexes.length; i++)
			{
				indexes[i] = -1;
			}
		
			
			// copy arr to tempArr
			// temporary array is so that we can analyze the end position from choosing an index without modifying original
			int [] tempArr = new int[arr.length];
			int endIndex;
			for (int i = 0; i < arr.length; i++)
			{
				tempArr[i] = arr[i];					
			}
		
			// analyze each slot on this player's side of the board
			for (int i = start; i < store; i++)
			{
				// get the position where the index would land if index i was chosen as a starting point
				endIndex = shiftBoard(tempArr[i], i, tempArr, slot,CAPTURE, AVALANCHE);
			
				// if the index lands in this player's store, the player gets another turn
				if (endIndex == store)
				{
					// set index to be valid
					indexes[i] = 1;
					
					// mark that the player get's another turn while playing in avalanche mode
					avalancheWithAnotherTurn = true;
				}
				
				// otherwise mark as invalid
				else
					indexes[i] = -1;

				// reset tempArr to the original array and then check the end position of the next index
				for (int j = 0; j < tempArr.length; j++)
				{
					tempArr[j] = arr[j];					
				}

			}
			
			// if playing in avalanche mode and the player get's another turn, chose randomly from indexes that will do so
			if (avalancheWithAnotherTurn)
			{
				// chose a random index until arr[i] does not equal 1
				do
				{
					index = random.nextInt(indexes.length);
				} while(indexes[index] != 1);
					
			}
			
		}
	
		
		
		// if there is no index that gets the computer another turn and
		// if there is no index that allows the computer to capture the other user's seeds and
		// if game is not being played in avalanche mode

		// then chose randomly from any spot that does not contain zero seeds
		int max = store - 1;
		int min = start;
		if (index == -1)
		{
			do
			{
				index = random.nextInt((max - min) + 1) + min;
			} while (arr[index] == 0);
		}
		

		return index;
		
	
	}
	
	
	// get end index given array, starting point and array of values per hollow
	public int GetIndex(int [] arr, int startingIndex)
	{
		int hand = arr[startingIndex];
		int index = startingIndex;
		
		while (hand != 0)
		{
			hand--;
			index++;

			if (index == arr.length)
				index = 0;
			
		}
	
		return index;
		
	}
	
	


}






