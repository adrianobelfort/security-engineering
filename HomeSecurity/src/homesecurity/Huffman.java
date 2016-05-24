/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 *
 * @author adria
 */
public class Huffman{
	// The class Node is used for representing each
	// node in the Huffman tree

	private static class Node implements Comparable{
		public Node[] children;

		// Constructor for reading BitSet data
		Node(BitSet bits, int start){
			// If it's 1, it's leaf node.
			leaf = bits.get(start);

			if(leaf){
				// If the node is leaf, we define it's value to what is in the next
				// 8 bits, then we define the last position of bit data of this node
				// at the BitSet

				value = (byte)readBits(bits, start+1, 8);
				end = start + 9;
			}else{
				// If it isn't, we allocate two children and read their data

				children = new Node[2];

				Node childLeft = new Node(bits, start+1);
				Node childRight = new Node(bits, childLeft.end);

				children[0] = childLeft;
				children[1] = childRight;

				end = childRight.end;
			}
		}

		Node(byte v, int f){
			// Constructor from value and frequency.
			// Implies that the node isn't leaf.
			value = v;
			frequency = f;
			leaf = true;
		}

		Node(Node left, Node right){
			// Constructor for child nodes. If it is the case,
			// then the node obviously isn't leaf.
			leaf = false;

			// Sets children nodes
			children = new Node[2];
			children[0] = left;
			children[1] = right;

			// Sets frequency
			frequency = left.frequency + right.frequency;
		}

		// Writes Node information starting at start
		public void writeNode(BitSet bits, int start){
			// Writes 1 if it's a leaf node. Writes 0 otherwise.
			bits.set(start, leaf);

			if(leaf){
				// If it is a leaf not, writes it's value
				writeBits(bits, start+1, value);
				end = start+9;
			}else{
				// If it isn't, writes their children to the BitSet
				children[0].writeNode(bits, start+1);
				children[1].writeNode(bits, children[0].end);

				end = children[1].end;
			}
		}

		// Adds a new node to the HashMap
		public void addToMap(Map m, BitSet lcode, int depth){
			if(leaf){
				m.put(value, this);
				code = lcode;
				this.depth = depth;
			}else{
				BitSet rcode = (BitSet)lcode.clone();

				lcode.set(depth, false);
				rcode.set(depth, true);

				children[0].addToMap(m, lcode, depth+1);
				children[1].addToMap(m, rcode, depth+1);
			}
		}

		public boolean leaf;
		public int frequency;
		public byte value;

		public int end;
		public int depth;
		public BitSet code;

		public int compareTo(Object o){
			return frequency - ((Node)o).frequency;
		}
	}

	// Reads bitamount bits starting from start from BitSet data
	public static int readBits(BitSet data, int start, int bitamount){
		int ret = 0;
		int i;

		for(i=0; i<bitamount; i++){
			ret = (ret << 1) + (data.get(start+i) ? 1 : 0);
		}

		return ret;
	}

	// Same as before, but for 8 bits
	public static int readBits(BitSet data, int start){
		return readBits(data, start, 8);
	}

	// Writes bitamount bits starting at start with value value, in the BitSet data
	public static void writeBits(BitSet data, int start, int bitamount, int value){
		int i;

		for(i=0; i<bitamount; i++){
			data.set(start+(bitamount-1-i), ((value >> i) & 1) == 1);
		}
	}

	// Same as the last function, but standardized for 8 bits
	public static void writeBits(BitSet data, int start, int value){
		writeBits(data, start, 8, value);
	}

	// For debugging purposes, prints all bits in a bitset
	public static void printBitSet(BitSet bits, int amount){
		String s = "";

		for(int i=0; i<amount; i++){
			s = s + (bits.get(i) ? 1 : 0);
		}

		System.out.println(s);
	}

	public static byte[] Encode(byte[] msg){
		// First we make a map to contain the characters in the received
		// text and their respective frequencies

		Map CharacterFrequency = new HashMap();
		int UniqueCharacterAmount = 0;

		for(byte b: msg){
			if(CharacterFrequency.containsKey(b)){
				CharacterFrequency.put(b, (int)CharacterFrequency.get(b)+1);
			}else{
				CharacterFrequency.put(b, 1);
				UniqueCharacterAmount = UniqueCharacterAmount + 1;
			}
		}

		// Next, we build a priority queue, in which the nodes with highest
		// priority are those with lowest frequency, which makes the Huffman
		// tree have least frequent nodes deeper down in the tree

		PriorityQueue Q = new PriorityQueue();

		for(Object b: CharacterFrequency.keySet()){
			Q.add(new Node(
				(byte)b,
				(int)CharacterFrequency.get((byte)b)
				));
		}

		// While the priority queue contains more than one node, we pick the
		// two nodes with lowest priority, create a new one whose children
		// are the nodes we picked earlier and insert it back into the queue

		while(Q.size()!=1){
			Node childLeft = (Node)Q.poll();
			Node childRight = (Node)Q.poll();

			Node newNode = new Node(childLeft, childRight);
			Q.add(newNode);
		}

		Node huffmanTree = (Node)Q.poll();

		// Now that the Huffman tree has been built, we create a byte array
		// that contains representation on how to rebuild the tree later and,
		// after that, the encoded bits

		BitSet bits = new BitSet();
		int currentBit = 3;			// First three bits left to count 0's at the end

		// Creating tree data

		huffmanTree.writeNode(bits, currentBit);
		currentBit = huffmanTree.end;

		int overhead = currentBit;

		// Now we create a map do determine what is the code for each character more easily

		Map CharacterCode = new HashMap();

		huffmanTree.addToMap(CharacterCode, new BitSet(), 0);

		// Now that we can determine the code of each character we find in O(log n), we 
		// create the code

		for(byte b: msg){
			Node node = (Node)CharacterCode.get(b);

			int i;
			for(i=0; i<node.depth; i++){
				bits.set(currentBit++, node.code.get(i));
			}
		}

		// Calculates the size of the 0 padding at the end

		int remainingBits = (8 - (currentBit%8))%8;

		// Records the padding
		writeBits(bits, currentBit, remainingBits, 0);
		// Records the size of the padding
		writeBits(bits, 0, 3, remainingBits);

		currentBit = currentBit + remainingBits;

		/*System.out.println("Huffman compression stats:");
		System.out.println("\tOriginal bits: " + (msg.length*8) + " bits (" + msg.length + " bytes)");
		System.out.println("\tHuffman bits: " + (currentBit - overhead) + " bits");
		System.out.println("\tHuffman tree overhead: " + overhead + " bits");
		System.out.println("\tHuffman total size: " + currentBit + " bits (" + currentBit/8 + " bytes)");*/

		// Puts together everything so we can return
		byte[] ret = new byte[currentBit/8];
		for(int i=0; i<currentBit; i+=8){
			ret[i/8] = (byte)readBits(bits, i);
		}

		return ret;
	}

	// Converte de Vector para byte[]
    public static byte[] vectorToByte(Vector v){
        byte[] ret = new byte[v.size()];
        
        for(int i=0; i<v.size(); i++){
            ret[i] = (byte)v.get(i);
        }
        
        return ret;
    }
        
	public static byte[] Decode(byte[] msg) throws Exception{
		BitSet bits = new BitSet();
		int bitcount = 0;

		// Loads information into the BitSet
		for(byte b: msg){
			writeBits(bits, bitcount, b);
			bitcount = bitcount+8;
		}

		// Reads padding size
		int zeroPadding = readBits(bits, 0, 3);

		// Allocated first node and reads the bitset
		Node huffmanTree = new Node(bits, 3);
		Vector result = new Vector();

		Node currentNode = huffmanTree;
		int currentBit = huffmanTree.end;

		// Goes through the bits
		for(; currentBit!=(bitcount-zeroPadding); currentBit++){
			// If bit is zero, we go to the left child. We go to the right
			// child otherwise
			currentNode = currentNode.children[bits.get(currentBit) ? 1 : 0];

			// If it's a leaf node, we add it's value to the output
			if(currentNode.leaf){
				result.add(currentNode.value);
				currentNode = huffmanTree;
			}
		}

		// Returning result
		byte[] ret = new byte[result.size()];
		for(int i=0; i<result.size(); i++){
			ret[i] = (byte)result.get(i);
		}

		return ret;
	}
}
