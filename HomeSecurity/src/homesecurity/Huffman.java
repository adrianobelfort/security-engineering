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

		Node(BitSet bits, int start){
			leaf = bits.get(start);

			if(leaf){
				value = (byte)readBits(bits, start+1, 8);
				end = start + 9;
			}else{
				children = new Node[2];

				Node childLeft = new Node(bits, start+1);
				Node childRight = new Node(bits, childLeft.end);

				children[0] = childLeft;
				children[1] = childRight;

				end = childRight.end;
			}
		}

		Node(byte v, int f){
			value = v;
			frequency = f;
			leaf = true;
		}

		Node(Node left, Node right){
			leaf = false;

			children = new Node[2];
			children[0] = left;
			children[1] = right;

			frequency = left.frequency + right.frequency;
		}

		public void writeNode(BitSet bits, int start){
			bits.set(start, leaf);

			if(leaf){
				writeBits(bits, start+1, value);
				end = start+9;
			}else{
				children[0].writeNode(bits, start+1);
				children[1].writeNode(bits, children[0].end);

				end = children[1].end;
			}
		}

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

	public static int readBits(BitSet data, int start, int bitamount){
		int ret = 0;
		int i;

		for(i=0; i<bitamount; i++){
			ret = (ret << 1) + (data.get(start+i) ? 1 : 0);
		}

		return ret;
	}

	public static int readBits(BitSet data, int start){
		return readBits(data, start, 8);
	}

	public static void writeBits(BitSet data, int start, int bitamount, int value){
		int i;

		for(i=0; i<bitamount; i++){
			data.set(start+(bitamount-1-i), ((value >> i) & 1) == 1);
		}
	}

	public static void writeBits(BitSet data, int start, int value){
		writeBits(data, start, 8, value);
	}

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

		int remainingBits = (8 - (currentBit%8))%8;

		writeBits(bits, currentBit, remainingBits, 0);
		writeBits(bits, 0, 3, remainingBits);

		currentBit = currentBit + remainingBits;

		/*System.out.println("Huffman compression stats:");
		System.out.println("\tOriginal bits: " + (msg.length*8) + " bits (" + msg.length + " bytes)");
		System.out.println("\tHuffman bits: " + (currentBit - overhead) + " bits");
		System.out.println("\tHuffman tree overhead: " + overhead + " bits");
		System.out.println("\tHuffman total size: " + currentBit + " bits (" + currentBit/8 + " bytes)");*/

		//printBitSet(bits, currentBit);

		byte[] ret = new byte[currentBit/8];
		for(int i=0; i<currentBit; i+=8){
			ret[i/8] = (byte)readBits(bits, i);
			//System.out.println(readBits(bits, i/8, 8));
		}

		//return Arrays.copyOfRange(bits.toByteArray(), 0, currentBit/8);
		return ret;
	}

	public static byte[] Decode(byte[] msg){
		BitSet bits = new BitSet();
		int bitcount = 0;

		for(byte b: msg){
			writeBits(bits, bitcount, b);
			bitcount = bitcount+8;
		}

		//printBitSet(bits, bitcount);

		//byte[] ret = new byte[5];

		int zeroPadding = readBits(bits, 0, 3);

		Node huffmanTree = new Node(bits, 3);
		Vector result = new Vector();

		Node currentNode = huffmanTree;
		int currentBit = huffmanTree.end;

		for(; currentBit!=(bitcount-zeroPadding); currentBit++){
			currentNode = currentNode.children[bits.get(currentBit) ? 1 : 0];

			if(currentNode.leaf){
				result.add(currentNode.value);
				currentNode = huffmanTree;
			}
		}

		byte[] ret = new byte[result.size()];
		for(int i=0; i<result.size(); i++){
			ret[i] = (byte)result.get(i);
		}

		return ret;
	}

	public static void main(String[] args){
		String message = "test message.";

		byte[] encoded;
		byte[] decoded;

                encoded = Huffman.Encode(message.getBytes());
                decoded = Decode(encoded);

                char[] msg = new char[decoded.length];

                int i;
                for(i=0; i<msg.length; i++){
                        msg[i] = (char)decoded[i];
                }

                System.out.println(msg);
                System.out.println(new String("decoded"));
		
	}
}
