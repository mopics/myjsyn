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
}
