/*************************************************************************
 *  Compilation:  javac Gorillahash.java
 *  Execution:    java Gorillahash < HbB_FASTAs.in
 *  Dependencies: StdIn.java StdOut.java Stopwatch.java Vector.java

 *************************************************************************/

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 *  The Gorillahash class stores protein sequences from an input file 
 *  and generates similarities between them.
 */

public class Gorillahash {

	public static void main(String[] args) throws FileNotFoundException {
		Stopwatch timer = new Stopwatch();
		String[] input = StdIn.readAllLines();
		ArrayList<ProteinSeq> sequences = getSequences(input);
		profileSimilarities(sequences);
		StdOut.println("Time to build data and calculate similarities: " + timer.elapsedTime() + "\n");
		// for (ProteinSeq x: sequences){
		// 	System.out.println(" Name: "+x.name + "\n Sequence: " + x.sequence);	
		// 	StdOut.println("*** Similarity ***");
		// 	for(int j = 0; j < x.similarities.length; j++){
		// 		double similarity = x.similarities[j];
		// 		StdOut.println("With " + sequences.get(j).name + " " + similarity);
		// 	}
		// 	StdOut.println();
		// }
	}

	/**
	 *  This method reads the input and stores each name and protein 
	 *  sequence into a ProteinSeq object and then adds it to an arrayList of sequences.
	 * @param input the file input
	 * @return an arrayList of all sequences. 
	 */
	public static ArrayList<ProteinSeq> getSequences(String[] input) {
		ArrayList<ProteinSeq> sequences= new ArrayList<ProteinSeq>();
		ProteinSeq sequence = null;
		String name = "";
		String seq = "";
		String line = "";
		for (String x : input) {
			if (x.startsWith(">")) {
				if (!line.isEmpty()) {
					sequence = new ProteinSeq(name, line);
					sequences.add(sequence);
					line="";
					name="";
				}
				name = x.substring(1).trim();
			} else {
				line = line+x;
			}
		}
		if (!line.isEmpty()) {
			sequence = new ProteinSeq(name, line);
			sequences.add(sequence);
			line="";
		}
		return sequences;
	}

	/**
	 *  This method calculates the similarities between sequences by using the cosAngle method. 
	 *  The similarity for each sequence with all other sequences is added to an array and  
	 *  set to the similarities field of each ProteinSeq.
	 * @param sequences an arrayList of sequences 
	 */
	public static void profileSimilarities(ArrayList<ProteinSeq> sequences) {
		for(int i = 0; i < sequences.size() ; i++){
			double[] allSimilarities = new double[sequences.size()];
			ProteinSeq sequence = sequences.get(i);
			for(int j = 0; j < sequences.size() ; j++) {
				ProteinSeq another =sequences.get(j);
				double similarity = cosAngle(sequence.profileVec, another.profileVec);
				allSimilarities[j] = similarity;
			}
			sequence.setSimilarities(allSimilarities);
		}
	}
	public static double cosAngle(Vector p, Vector q){
		double angle = (p.dot(q)) / (p.magnitude() * q.magnitude());
		return angle;
	}
}

/**
 *  The ProteinSeq class is used to create protein sequence objects.
 */
class ProteinSeq {
	public static final int K = 20;
	public static final int D = 10000;
	String name = null;    		
	String sequence = null;		
	double[] similarities;
	Vector profileVec;   //profile vector of a sequence


	public ProteinSeq(String name, String sequence){
		this.name = name;
		this.sequence = sequence;
		this.similarities = this.getSimilarities();
		this.profileVec = getProfile();
	}

/**
 *  This method reads the kgrams of a sequence and stores them into an double array.
 *  The double array is then passed to a Vector object.
 *  @return a vector object representing the profile of the sequence
 */
	private Vector getProfile() {
		double[] profile = new double[D];
		String kgram = "";
		for (int i=0; i<this.sequence.length(); i++) {
			int endKgram = i + K;
			if(endKgram <= sequence.length())
				kgram = this.sequence.substring(i, endKgram);
			else 
				kgram = this.sequence.substring(i);
			int hash = this.hashCode(kgram);
			profile[hash] += 1;
		}		
		Vector profileVec = new Vector(profile);
		return profileVec;
	}

	public double[] getSimilarities() {
		return similarities;
	}
	public void setSimilarities(double[] similarities) {
		this.similarities = similarities;
	}

	public int hashCode(String kgram){
		return (kgram.hashCode() & 0x7fffffff) % D;
	}

}