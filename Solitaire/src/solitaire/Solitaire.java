package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire
 * Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire
{

	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;

	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a
	 * circular linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck()
	{
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i = 0; i < cardValues.length; i++)
		{
			cardValues[i] = i + 1;
		}

		// shuffle the cards
		Random randgen = new Random();
		for (int i = 0; i < cardValues.length; i++)
		{
			int other = randgen.nextInt(28);
			int temp = cardValues[i];
			cardValues[i] = cardValues[other];
			cardValues[other] = temp;
		}

		// create a circular linked list from this deck and make deckRear point
		// to its last node
		CardNode cn = new CardNode();
		cn.cardValue = cardValues[0];
		cn.next = cn;
		deckRear = cn;
		for (int i = 1; i < cardValues.length; i++)
		{
			cn = new CardNode();
			cn.cardValue = cardValues[i];
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) throws IOException
	{
		CardNode cn = null;
		if (scanner.hasNextInt())
		{
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = cn;
			deckRear = cn;
		}
		while (scanner.hasNextInt())
		{
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA()
	{
		System.out.println("Original Deck");
		printList(deckRear);
		CardNode curr = deckRear.next;
		CardNode temp = null;
		int tempData;
		int target = 27;

		do
		{
			temp = curr;
			curr = curr.next;
		} while (curr.cardValue != target);

		temp = temp.next;
		curr = curr.next;

		tempData = temp.cardValue;
		temp.cardValue = curr.cardValue;
		curr.cardValue = tempData;

		System.out.println("After JokerA Call");
		printList(deckRear);
	}

	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB()
	{
		CardNode curr = deckRear.next;
		CardNode temp = null;
		int tempData;
		int target = 28;

		do
		{
			temp = curr;
			curr = curr.next;
		} while (curr.cardValue != target);

		temp = temp.next;
		curr = curr.next;

		tempData = temp.cardValue;
		temp.cardValue = curr.cardValue;
		curr.cardValue = tempData;

		temp = temp.next;
		curr = curr.next;

		tempData = temp.cardValue;
		temp.cardValue = curr.cardValue;
		curr.cardValue = tempData;

		System.out.println("After JokerB Call");
		printList(deckRear);
	}

	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut()
	{
		CardNode front1 = deckRear.next;
		int jokerVal = getFirstJ();

		// Get the value of the first joker

		CardNode firstJ = null;
		CardNode secondJ = null;
		if (jokerVal == 1)
		{
			CardNode curr = deckRear;
			while (curr.cardValue != 27)
				curr = curr.next;
			firstJ = curr;
			CardNode curr2 = deckRear;
			while (curr2.cardValue != 28)
				curr2 = curr2.next;
			secondJ = curr2;
		}
		else if (jokerVal == 2)
		{
			CardNode curr2 = deckRear;
			while (curr2.cardValue != 28)
				curr2 = curr2.next;
			firstJ = curr2;
			CardNode curr = deckRear;
			while (curr.cardValue != 27)
				curr = curr.next;
			secondJ = curr;
		}

		CardNode curr = deckRear.next;
		CardNode temp = null;
		if (curr == firstJ)
		{
			temp = curr;
			curr = curr.next;
		}
		while (curr != firstJ)
		{
			temp = curr;
			curr = curr.next;
		}
		CardNode beforeFirstJ = temp;

		if (deckRear.next == firstJ)
		{
			deckRear.next = secondJ.next;
			beforeFirstJ.next = firstJ;
			deckRear = secondJ;
		}
		else if (secondJ.next == deckRear.next)
		{
			deckRear = beforeFirstJ;
			secondJ = front1;
			deckRear.next = firstJ;

		}
		else
		{
			beforeFirstJ.next = secondJ.next;
			deckRear.next = firstJ;
			secondJ.next = front1;
			deckRear = beforeFirstJ;
		}

		System.out.println("After TripleCut Call");
		printList(deckRear);
	}

	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut()
	{
		int counter = deckRear.cardValue;
		if (counter == 28 || counter == 27)
			return;
		CardNode start = deckRear.next;
		CardNode end = null;
		CardNode beforeRear = getBeforeRear();

		while (counter != 0)
		{
			end = start;
			start = start.next;
			counter--;
		}
		start = deckRear.next;
		deckRear.next = end.next;
		beforeRear.next = start;
		end.next = deckRear;

		System.out.println("After CountCut ");
		printList(deckRear);
	}

	private int getFirstJ()
	{
		CardNode firstJ = deckRear.next;
		while (firstJ != deckRear)
		{
			if (firstJ.cardValue == 27)
			{
				return 1;
			}
			else if (firstJ.cardValue == 28)
			{
				return 2;
			}
			firstJ = firstJ.next;
		}
		return 0;
	}

	private CardNode getBeforeRear()
	{
		CardNode curr = deckRear.next;
		while (curr.next != deckRear)
		{
			curr = curr.next;
		}
		return curr;
	}

	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count
	 * Cut, then counts down based on the value of the first card and extracts
	 * the next card value as key. But if that value is 27 or 28, repeats the
	 * whole process (Joker A through Count Cut) on the latest (current) deck,
	 * until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey()
	{
		jokerA();
		jokerB();
		tripleCut();
		countCut();

		CardNode front = deckRear.next;
		int counter = front.cardValue;
		int key;
		if (counter == 27 || counter == 28)
		{
			while (front.next != deckRear)
				front = front.next;
			key = front.next.cardValue;
		}
		else
		{
			while (counter != 0)
			{
				front = front.next;
				counter--;
			}
			key = front.cardValue;
		}
		if (key == 27 || key == 28)
		{
			key = getKey();
		}
		return key;
	}

	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear
	 *            Rear pointer
	 */
	private static void printList(CardNode rear)
	{
		if (rear == null)
		{
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do
		{
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message
	 *            Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message)
	{
		message = message.toUpperCase();
		message = message.replaceAll("\\W", "");
		String encoded = "";
		System.out.println(message);
		System.out.println("-------------------------------------------------------------------------------------");
		int key;
		for (int i = 0; i < message.length(); i++)
		{
			key = getKey();
			char chr = message.charAt(i);
			int chrVal = (chr - 'A' + 1);
			chrVal = chrVal + key;
			if (chrVal > 26)
				chrVal = chrVal - 26;
			chr = (char) (chrVal - 1 + 'A');
			encoded += chr;
			System.out.println("The key is " + key);
			System.out.println("This is the new Char: " + chrVal);
			System.out.println("-------------------------------------------------------------------------------------");

		}
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return encoded;
	}

	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message
	 *            Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message)
	{
		message = message.toUpperCase();
		String decoded = "";
		System.out.println(message);
		System.out.println("-------------------------------------------------------------------------------------");
		int key;
		for (int i = 0; i < message.length(); i++)
		{
			key = getKey();
			char chr = message.charAt(i);
			int chrVal = (chr - 'A' + 1);
			if (chrVal <= key)
				chrVal = chrVal + 26;
			chrVal = chrVal - key;
			chr = (char) (chrVal - 1 + 'A');
			decoded += chr;
			System.out.println("The key is " + key);
			System.out.println("This is the new Char: " + chrVal);
			System.out.println("-------------------------------------------------------------------------------------");
		}
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return decoded;
	}
}