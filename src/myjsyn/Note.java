package myjsyn;

import java.util.HashMap;

public class Note {
	
	public static final int C0 = 24; // lowest C midi-note
	public static final HashMap<String,Integer> NOTES = new HashMap<String,Integer>();
	static {
    	NOTES.put( "C", 0 );
		NOTES.put( "Db", 1 );
        NOTES.put( "D", 2 );
    	NOTES.put( "Eb",3 );
    	NOTES.put( "E",4);
    	NOTES.put( "F",5 );
    	NOTES.put( "Gb",6 );
    	NOTES.put( "G",7 );
    	NOTES.put( "Ab",8 );
    	NOTES.put( "A",9);
    	NOTES.put( "Bb",10 );
    	NOTES.put( "B",11 );
    };
    public static int convertNoteToPitch( String note , int octave ){
    	if( NOTES.get( note )==null ){ return -1; }
    	int octaveStart = (octave-1)*12 + C0;
    	return octaveStart + NOTES.get(note);
    }
	public static double convertNoteToFrequency( String note, int octave ){
		if( NOTES.get( note )==null ){ return -1.0; }
		int octaveStart = (octave-1)*12 + C0;
		return convertPitchToFrequency( octaveStart+NOTES.get(note) );
	}
	/**
     * Calculate frequency in Hertz based on MIDI pitch. Middle C is 60.0. You
     * can use fractional pitches so 60.5 would give you a pitch half way
     * between C and C#.
     */
	public static double convertPitchToFrequency( double pitch )
    {
        final double concertA = 440.0;
        return concertA * Math.pow( 2.0, ((pitch - 69) * (1.0 / 12.0)) );
    }
	public static void main( String [] args ){
		System.out.println( convertNoteToFrequency( "F", 5 ) );
	}
	
	
	public static double[] getNoteAscPattern( String rootNote, int[] pattern, int octave, int[] mode, int numNotes ){
		double[] notes = new double[ numNotes ];
		int rootPitch = convertNoteToPitch( rootNote, octave );
		
		int pi = 0;
		for( int ni=0; ni<numNotes; ni++ ) {
			if( pi==pattern.length ){ // if at end of pattern ascend pattern by one
				pi = 0;
				for( int pii=0; pii<pattern.length; pii++ ){
					pattern[pii] += 1;
				}
			}
			int modeIdx = pattern[pi];
			if( modeIdx >= mode.length ){// if modeIdx > then mode.length : find out how much bigger, reset and add octave ( 12 pitches ) to rootPitch
				modeIdx = modeIdx - mode.length;
				rootPitch += 12;
			}
			
			notes[ni] = convertPitchToFrequency( rootPitch + mode[ modeIdx ] );
			
			pi++;
		}
		return notes;
	}
	public static final int[] MAJOR 	= new int[] { 2,4,5,7,9,11 }; // Cmaj7
	public static final int[] DORIAN 	= new int[] { 2,3,5,7,9,10 }; // Dmin7
	public static final int[] PHRYGIAN 	= new int[] { 1,3,5,7,8,10 }; // Emin7
	public static final int[] LYDIAN 	= new int[] { 2,4,6,7,9,11 }; // Fmaj7 #4
	public static final int[] MIXOLYDIAN= new int[] { 2,4,5,7,9,10 }; // G7
	public static final int[] AEOLIAN 	= new int[] { 2,3,5,7,8,10 }; // Amin7
	public static final int[] LOCRIAN 	= new int[] { 1,3,5,6,8,10 }; // B¿
}
