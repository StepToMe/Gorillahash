


/*************************************************************************
 *  Compilation:  javac -cp .;algs4.jar Gorillahash.java
 *  Execution:    java -cp .;algs4.jar Gorillahash < HbB_FASTAs.in
 *  Dependencies: StdIn.java StdOut.java Stopwatch.java 

 *************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Stopwatch;
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
		double[][] similarities = profileSimilarities(sequences);	
		StdOut.println("Time to build data and calculate similarities: " + timer.elapsedTime() + "\n");
		DecimalFormat format = new DecimalFormat("0.000");
		
		for (int i = 0; i < similarities.length; i++){
			//System.out.println(" Name: "+x.name + "\n Sequence: " + x.sequence);	
			System.out.println(" Name: "+ sequences.get(i).name);
			StdOut.println("*** Similarity ***");
			for(int j = 0; j < similarities.length; j++){
				double similarity = similarities[i][j];
				StdOut.println("With " + sequences.get(j).name + " " + format.format(similarity));
			}
			StdOut.println();
	
		}
	}

	/**
	 *  This method reads the input and stores each name and protein 
	 *  sequence into a ProteinSeq object and then adds it to an arrayList of sequences.
	 * @param : input the file input
	 * @return : an arrayList of all sequences. 
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
	 *  The similarity for each sequence with all other sequences is added to a 2D array and  
	 * @param sequences : an arrayList of sequences 
	 */
	public static double[][] profileSimilarities(ArrayList<ProteinSeq> sequences) {
		int size = sequences.size();
		double[][] allSimilarities = new double[size][size];
		for(int i = 0; i < sequences.size() ; i++){
			
			int[] sequence = sequences.get(i).profile;
			for(int j = i; j < sequences.size() ; j++) {
				int[] another =sequences.get(j).profile;
				double similarity = ProteinSeq.cos_angle(sequence, another);
				allSimilarities[i][j] = similarity;
				allSimilarities[j][i] = similarity;
			}
		}
		return allSimilarities;
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
	int[] profile;   //profile vector of a sequence


	public ProteinSeq(String name, String sequence){
		this.name = name;
		this.sequence = sequence;
		this.profile = getProfile();
	}

/**
 *  This method reads the kgrams of a sequence and stores them into an double array.
 *  The double array is then passed to a Vector object.
 *  @return : a vector object representing the profile of the sequence
 */
	private int[] getProfile() {
		int[] profile = new int[D];
		String kgram = "";
		for (int i=0; i<this.sequence.length()-K; i++) {
			int endKgram = i + K;
			kgram = this.sequence.substring(i, endKgram);
			int hash = this.hashCode(kgram);
			profile[hash] += 1;
		}		
		return profile;
	}
	/**
	 * This method calculates the the inner product also known as the 'dot product' og vectors p and q.
	 * @param  p : an int[] array.
	 * @param  q : and int[] array
	 * @return   : the sum of the inner product
	 */
	public static double innerProduct(int[] p, int[] q){
		double sum = 0.0;
		for(int i = 0; i<D; i++)
			sum+= p[i]*q[i];
		return sum;
	} 
	/**
	 * This method calculates the length of a given vector
	 * @param  p : a vector
	 * @return   : the length of p
	 */
	public static double length(int[] p) {
		return Math.sqrt(innerProduct(p, p));
	}
	/**
	 * This method hashes a kgram instance in the sequence of strings to a single hash value.
	 * @return       : an integer hash value
	 */
	public int hashCode(String kgram){
		return (kgram.hashCode() & 0x7fffffff) % D;
	}
	/**
	 * This method calculates the angle between two vectors
	 * @param  p : an int[] array
	 * @param  q : an int[] array
	 * @return   : the angle between p and q
	 */
	public static double cos_angle(int[] p, int[] q) {
		return innerProduct(p, q) / (length(p) * length(q));
	}
}