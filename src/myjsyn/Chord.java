package myjsyn;

public class Chord {
	
	/**
	 * Returns 1-mj3-5-mj7 as frequencies
	 * @param root
	 * @param octave
	 * @return
	 */
	public static double[] major7( String root, int octave ){
		double[] chord = new double[4];
		int firstP 		= Note.convertNoteToPitch( root, octave );
		
		chord[0]	= Note.convertPitchToFrequency( firstP );
		chord[1] 	= Note.convertPitchToFrequency( firstP + 4 ); 		// major third == 4 half notes up
		chord[2]	= Note.convertPitchToFrequency( firstP + 7 );	 	// fifth == 3 half notes up from mj3
		chord[3]	= Note.convertPitchToFrequency( firstP + 11 ); 		// major 7th = 4 half notes up from 5th
		
		return chord;
	}
	/**
	 * Dominant 7 chord 1-mj3-5-7
	 * @param args
	 */
	public static double[] dominant7( String root, int octave ){
		double[] chord = new double[4];
		int firstP 		= Note.convertNoteToPitch( root, octave );
		
		chord[0]	= Note.convertPitchToFrequency( firstP );
		chord[1] 	= Note.convertPitchToFrequency( firstP + 4 ); 		// major third == 4 half notes up
		chord[2]	= Note.convertPitchToFrequency( firstP + 7 );	 	// fifth == 3 half notes up from mj3
		chord[3]	= Note.convertPitchToFrequency( firstP + 10 ); 		// major 7th = 4 half notes up from 5th
		
		return chord;
	}
	/**
	 * Minor 7 chord 1-m3-5-7
	 * @param args
	 */
	public static double[] minor7( String root, int octave ){
		double[] chord = new double[4];
		int firstP 		= Note.convertNoteToPitch( root, octave );
		
		chord[0]	= Note.convertPitchToFrequency( firstP );
		chord[1] 	= Note.convertPitchToFrequency( firstP + 3 ); 		// major third == 4 half notes up
		chord[2]	= Note.convertPitchToFrequency( firstP + 7 );	 	// fifth == 3 half notes up from mj3
		chord[3]	= Note.convertPitchToFrequency( firstP + 10 ); 		// major 7th = 4 half notes up from 5th
		
		return chord;
	}
	
	public static void main( String [] args ){
		double[] c = major7("F",4);
		for( int i=0; i<c.length; i++ ){
			System.out.println( c[i] );
		}
	}
}
