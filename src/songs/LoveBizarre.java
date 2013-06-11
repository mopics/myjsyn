package songs;

import java.io.IOException;

import myjsyn.Chord;
import myjsyn.Note;
import myjsyn.instruments.DrumMeasureData;
import myjsyn.instruments.MonoWasp;
import myjsyn.instruments.MonoWaspMeasureData;
import myjsyn.instruments.Organ;
import myjsyn.instruments.OrganMeasureData;
import myjsyn.instruments.Rik;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.UnitOscillator;
import com.softsynth.shared.time.TimeStamp;

public class LoveBizarre {
	//
	private Synthesizer synth;
	
	// instruments/circuits
    private LineOut lineOut;
    private Organ organ;
    private Rik rik;
    private MonoWasp wasp;
    
    // time stuff
    private static final double bpm = 200.0;  // beat per minute
    private static final double spb = 60.0/bpm; // seconds per beat
    private static final double spm = spb*5;  // seconds per measure
    
    // chords
    private static final double[] Fmaj7 = Chord.major7("F", 4 );
    private static final double[] Cmaj7 = Chord.major7("C", 4 );
    private static final double[] Gmaj7 = Chord.major7("G", 4 );
    private static final double[] D7    = Chord.dominant7("D", 4 );
    private static final double[] Amin7 = Chord.minor7("A", 4 );
    private static final double[] Amin7_05 = Chord.minor7("A", 5 );
    private static final double[] Dmin7 = Chord.minor7("D", 4 );
    private static final double[] Emin7 = Chord.minor7("E", 4 );
    private static final double[] G7	   = Chord.dominant7("G", 3 );
    
    private void go() throws IOException
    {
    	// Create main synth
        synth = JSyn.createSynthesizer();
        synth.add( lineOut = new LineOut() );
        
        // Setup instruments:
        // Wasp
        synth.add( wasp = new MonoWasp( ) );
        wasp.output.connect( 0, lineOut.input, 1 );
        // Organ.
        synth.add( organ  = new Organ( new UnitOscillator[]{
        		new SawtoothOscillator(),
        		new SquareOscillator(),
        		new SawtoothOscillator(),
        		new SineOscillator()
        }) );
        organ.output.connect( 0, lineOut.input, 0 );
        organ.output.connect( 0, lineOut.input, 1 );
        // Rik
        synth.add( rik = new Rik( new String[]{
        	"samples/drum/rikBass01.aiff",
        	"samples/drum/rikSnare02.aiff",
        	"samples/drum/rikSnareDB01.aiff",
        	"samples/drum/rikHat01.aiff"
        }) );
        rik.output.connect( 0, lineOut.input, 0 );
        rik.output.connect( 0, lineOut.input, 1 );
        
        //  START !
        synth.start();
        lineOut.start();
       
        // Get synthesizer time in seconds.
		double startTime = synth.getCurrentTime();
		double endTime = createSong( new TimeStamp( startTime+0.5 ) ) + 2.0;
		
        try
        {
            // Sleep untill song as ended:
            synth.sleepUntil( endTime );
        } 
        catch( InterruptedException e )
        {
            e.printStackTrace();
        }
         System.out.println( "Stop playing. -------------------" );
	     // Stop everything.
	     synth.stop();
    }
    
    private double createSong( TimeStamp ts ) {
    	double t = 0.0;
    	// schedule the parts together:
    	for( int l = 0; l < 3; l ++ ) {
    		
    		// Fmaj7
    		wasp.scheduleMeasure( WASP_F_01		, ts.makeRelative(t), 4 );
    		if( l==1  )
    			organ.scheduleMeasure( ORGAN_G,  ts.makeRelative(t), 4 );
    		rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
    		t += spm * 4;
	    	// Amin7
	    	wasp.scheduleMeasure( WASP_A_02		, ts.makeRelative(t), 4 );
	    	if( l==1 )
	    		organ.scheduleMeasure( ORGAN_A,  ts.makeRelative(t), 4 );
	    	rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// Fmaj7
	    	wasp.scheduleMeasure( WASP_F_01, 	  ts.makeRelative(t), 4 );
	    	if( l==1 ) {
	    		organ.scheduleMeasure( ORGAN_G_01,  ts.makeRelative(t), 1 );
	    		organ.scheduleMeasure( ORGAN_G_02,  ts.makeRelative(t+spm), 1 );
	    		organ.scheduleMeasure( ORGAN_G_03,  ts.makeRelative(t+spm*2), 1 );
	    		organ.scheduleMeasure( ORGAN_G_04,  ts.makeRelative(t+spm*3), 1 );
	    	}
	    	rik.scheduleMeasure( DRUM_DBHATS, 	  ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// Amin7
	    	wasp.scheduleMeasure( WASP_A_02		, ts.makeRelative(t), 4 );
	    	if( l==1 )
	    		organ.scheduleMeasure( ORGAN_A,  ts.makeRelative(t), 4 );
	    	rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// D7
	    	wasp.scheduleMeasure( WASP_D_M4		, ts.makeRelative(t), 1 );
	    	if( l==1 )
	    		organ.scheduleMeasure( ORGAN_D	, ts.makeRelative(t), 4 );
	    	rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// Amin7
	    	wasp.scheduleMeasure( WASP_A_01, 	  ts.makeRelative(t), 4 );
	    	if( l==1 )
	    		organ.scheduleMeasure( ORGAN_A,  ts.makeRelative(t), 4 );
	    	rik.scheduleMeasure( DRUM_DBHATS, 	  ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// D7
	    	wasp.scheduleMeasure( WASP_D_M4		, ts.makeRelative(t), 1 );
	    	if( l==1 ) {
		    	organ.scheduleMeasure( ORGAN_D_01,  ts.makeRelative(t), 1 );
		    	organ.scheduleMeasure( ORGAN_D_02,  ts.makeRelative(t+spm), 1 );
		    	organ.scheduleMeasure( ORGAN_D_01,  ts.makeRelative(t+spm*2), 1 );
		    	organ.scheduleMeasure( ORGAN_D_03,  ts.makeRelative(t+spm*3), 1 );
	    	}
	    	rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// Amin7
	    	wasp.scheduleMeasure( WASP_A_01		, ts.makeRelative(t), 4 );
	    	if( l==1 ) {
		    	organ.scheduleMeasure( ORGAN_A_01,  ts.makeRelative(t), 1 );
		    	organ.scheduleMeasure( ORGAN_A_02,  ts.makeRelative(t+spm), 1 );
		    	organ.scheduleMeasure( ORGAN_A_03,  ts.makeRelative(t+spm*2), 1 );
		    	organ.scheduleMeasure( ORGAN_A_04,  ts.makeRelative(t+spm*3), 1 );
	    	}
	    	rik.scheduleMeasure( DRUM_DBHATS	, ts.makeRelative(t), 4 );
	    	t += spm * 4;
    	}
    	return t;
    }
    public static void main( String[] args ){
        try {
			new LoveBizarre().go();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    // ==================================================================== DRUMS :
    // Drum patterns : // use the same order as Rik constructor!!!
	private static final double[][] vijfkwart = new double[][]{
		//  1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
		  { 3.00, 0.00, 0.00, 0.00, 0.00, 0.00, 3.00, 0.00, 0.00, 0.00 }, // base
		  { 0.00, 0.00, 0.00, 2.50, 0.00, 0.00, 0.00, 0.00, 2.00, 0.00 }, // snare
		  { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }, // snareDB
		  { 2.00, 0.00, 2.00, 0.00, 2.00, 0.00, 2.00, 0.00, 2.00, 0.00 }  // hhat
	};
	private static final double[][] vijfkwartDBHats = new double[][]{
		//  1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
		  { 3.00, 0.00, 0.00, 0.00, 0.00, 0.00, 3.00, 0.00, 0.00, 0.00 }, // base
		  { 0.00, 0.00, 0.00, 2.50, 0.00, 0.00, 0.00, 0.00, 2.00, 0.00 }, // snare
		  { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }, // snareDB
		  { 2.00, 0.60, 2.10, 0.70, 2.20, 0.80, 2.30, 0.90, 2.40, 1.00 }  // hhat
	};
	private static final DrumMeasureData DRUM_DBHATS  = new DrumMeasureData( spb/2, vijfkwartDBHats );
	private static final DrumMeasureData DRUM_DEFAULT = new DrumMeasureData( spb/2, vijfkwart );
    
	// ==================================================================== ORGAN :
	// organ measures:
	private static final double[] ORGAN_AMPS_01 = new double[]{
		//	1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
			0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10
	};
	private static final double[] ORGAN_AMPS_01_4 = new double[]{ // 16th notes
		//	1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
			0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10
	};
	private static final double[] ORGAN_AMPS_01_8 = new double[]{ // 32th notes ??
		//	1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
			0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10,
			0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10
	};
	private static final double[] ORGAN_AMPS_02 = new double[]{
		//	1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
			0.10, 0.00, 0.00, 0.10, 0.00, 0.00, 0.10, 0.00, 0.00, 0.00
	};
	private static final int[] ORGAN_ARP_01 = new int[]{ 0,3,2,1 };
	private static final int[] ORGAN_ARP_02 = new int[]{ 3,2,1,0 };
	private static final int[] ORGAN_ARP_03 = new int[]{ 3,1,2,0 };
	private static final int[] ORGAN_ARP_04 = new int[]{ 0,2,1,3 };
	private static final OrganMeasureData ORGAN_G = 	new OrganMeasureData( spb/2, spb/2, Gmaj7, ORGAN_AMPS_01, ORGAN_ARP_01 );
	private static final OrganMeasureData ORGAN_G_01 = 	new OrganMeasureData( spb/4, spb/4, Gmaj7, ORGAN_AMPS_01_4, ORGAN_ARP_02 );
	private static final OrganMeasureData ORGAN_G_02 = 	new OrganMeasureData( spb/4, spb/4, Gmaj7, ORGAN_AMPS_01_4, ORGAN_ARP_02 );
	private static final OrganMeasureData ORGAN_G_03 = 	new OrganMeasureData( spb/4, spb/4, Gmaj7, ORGAN_AMPS_01_4, ORGAN_ARP_03 );
	private static final OrganMeasureData ORGAN_G_04 = 	new OrganMeasureData( spb/4, spb/4, Gmaj7, ORGAN_AMPS_01_4, ORGAN_ARP_04 );
	private static final OrganMeasureData ORGAN_A = 	new OrganMeasureData( spb/2, spb/2, Amin7, ORGAN_AMPS_02, ORGAN_ARP_01 );
	private static final OrganMeasureData ORGAN_A_01 = 	new OrganMeasureData( spb/8, spb/8, Amin7,    ORGAN_AMPS_01_8, ORGAN_ARP_01 );
	private static final OrganMeasureData ORGAN_A_02 = 	new OrganMeasureData( spb/8, spb/8, Amin7,    ORGAN_AMPS_01_8, ORGAN_ARP_02 );
	private static final OrganMeasureData ORGAN_A_03 = 	new OrganMeasureData( spb/8, spb/8, Amin7,    ORGAN_AMPS_01_8, ORGAN_ARP_03 );
	private static final OrganMeasureData ORGAN_A_04 = 	new OrganMeasureData( spb/8, spb/8, Amin7_05, ORGAN_AMPS_01_8, ORGAN_ARP_04 );
	private static final OrganMeasureData ORGAN_D = 	new OrganMeasureData( spb/2, spb/2, D7,    ORGAN_AMPS_02 );
	private static final OrganMeasureData ORGAN_D_01 = 	new OrganMeasureData( spb/8, spb/8, D7,    ORGAN_AMPS_01_8, ORGAN_ARP_01 );
	private static final OrganMeasureData ORGAN_D_02 = 	new OrganMeasureData( spb/8, spb/8, D7,    ORGAN_AMPS_01_8, ORGAN_ARP_02 );
	private static final OrganMeasureData ORGAN_D_03 = 	new OrganMeasureData( spb/8, spb/8, D7,    ORGAN_AMPS_01_8, ORGAN_ARP_03 );
	private static final OrganMeasureData ORGAN_D_04 = 	new OrganMeasureData( spb/8, spb/8, D7,    ORGAN_AMPS_01_8, ORGAN_ARP_04 );
	
	// ==================================================================== WASP :
    // Wasp Measure components
	// notes
    private static final double [] WASP_NOTES_F = { Note.convertNoteToFrequency("G", 1 ) };
    private static final double [] WASP_NOTES_A = { Note.convertNoteToFrequency("A", 1 ) };
    private static final double [] WASP_NOTES_D = { Note.convertNoteToFrequency("D", 1 ) };
    // amps
    private static final double [] WASP_AMP_M = { 0.3 };
    private static final double [] WASP_AMPS_1_15 =
    //  1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
    {	0.50, 0.50, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00  };
    private static final double [] WASP_AMPS_1_15_4_45 = 
    //  1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
    {	0.50, 0.50, 0.00, 0.00, 0.00, 0.00, 0.60, 0.30, 0.00, 0.00  };
    private static final double [] WASP_AMPS_1_15_2_25_4_45 =
    //  1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
    {	0.50, 0.50, 0.00, 0.50, 0.40, 0.00, 0.60, 0.30, 0.00, 0.00  };
    // envelopes 
    private static final double[] WASP_ENV_4M = { // 4 measures long envelope
    	spb*4, 0.5,
    	spb*4, 2.0,
    	spb*4, 2.0,
    	spb*4, 2.5,
    	spb*4, 0.5,
    };
    private static final double[] WASP_ENV_SPB = {
    	spb/5, 0.9,
    	spb/5, 1.8,
    	spb/5, 1.5,
    	spb/5, 1.3,
    	spb/5, 0.0
    };
    private static final double[] WASP_ENV_SPB_H = {
    	spb/10, 0.9,
    	spb/10, 0.8,
    	spb/10, 0.5,
    	spb/10, 0.3,
    	spb/10, 0.0
    };
    // Wasp measures :
    private static final MonoWaspMeasureData WASP_F_01 = new MonoWaspMeasureData( spb/2, 	WASP_AMPS_1_15_4_45, WASP_NOTES_F, 	new double[][]{ WASP_ENV_SPB_H } );
    private static final MonoWaspMeasureData WASP_A_01 = new MonoWaspMeasureData( spb/2, 	WASP_AMPS_1_15_4_45, WASP_NOTES_A, 	new double[][]{ WASP_ENV_SPB_H } );
    private static final MonoWaspMeasureData WASP_A_02 = new MonoWaspMeasureData( spb/2,	WASP_AMPS_1_15, WASP_NOTES_A, 		new double[][]{ WASP_ENV_SPB_H } );
    private static final MonoWaspMeasureData WASP_D_M4 = new MonoWaspMeasureData( spb/2,	WASP_AMPS_1_15_4_45, WASP_NOTES_D, 	new double[][]{ WASP_ENV_SPB_H } );
    
}
