package songs;

import java.io.File;
import java.io.IOException;

import myjsyn.Chord;
import myjsyn.Note;
import myjsyn.instruments.DrumMeasureData;
import myjsyn.instruments.MonoWasp;
import myjsyn.instruments.MonoWaspMeasureData;
import myjsyn.instruments.OscChordPlayer;
import myjsyn.instruments.OscChordMeasureData;
import myjsyn.instruments.Drum;
import myjsyn.instruments.SlideOsc;
import myjsyn.instruments.SlideOscMeasureData;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.util.WaveRecorder;
import com.softsynth.shared.time.TimeStamp;

public class LoveBizarre {
	//
	private Synthesizer synth;
	
	// instruments/circuits
    private LineOut lineOut;
    private OscChordPlayer organ;
    private Drum rik;
    private MonoWasp wasp;
    private SlideOsc winer;
    
    // time stuff
    private static final double bpm = 170.0;  // beat per minute
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
        
        // setup recorder
        File wav = new File("LoveBizarre_synth.wav" );
    	WaveRecorder rec = new WaveRecorder( synth, wav );
        
        // Setup instruments:
    	// Winer
    	synth.add( winer = new SlideOsc( new SquareOscillator() ) );
    	//winer.output.connect( 0, lineOut.input, 0 );
    	//winer.output.connect( 0, lineOut.input, 1 );
    	winer.output.connect( 0, rec.getInput(), 0 );
    	winer.output.connect( 0, rec.getInput(), 1 );
        // Wasp
        synth.add( wasp = new MonoWasp( ) );
        wasp.output.connect( 0, lineOut.input, 0 );
        wasp.output.connect( 0, lineOut.input, 1 );
        wasp.output.connect( 0, rec.getInput(), 0 );
        wasp.output.connect( 0, rec.getInput(), 1 );
        // Organ.
        synth.add( organ  = new OscChordPlayer( new UnitOscillator[]{
        		new SawtoothOscillator(),
        		new SquareOscillator(),
        		new SawtoothOscillator(),
        		new SineOscillator()
        }) );
        organ.output.connect( 0, lineOut.input, 0 );
        organ.output.connect( 0, lineOut.input, 1 );
        organ.output.connect( 0, rec.getInput(), 0 );
        organ.output.connect( 0, rec.getInput(), 1 );
        // Rik
        synth.add( rik = new Drum( new String[]{
        	"samples/LM1/LM1_Kick.wav",
        	"samples/LM1/LM1_Snare.wav",
        	"samples/LM1/LM1_Hat_Open.wav",
        	"samples/LM1/LM1_Hat_Cl.wav"
        }) );
        rik.output.connect( 0, lineOut.input, 0 );
        rik.output.connect( 0, lineOut.input, 1 );
        rik.output.connect( 0, rec.getInput(), 0 );
        rik.output.connect( 0, rec.getInput(), 1 );
        
        //  START !
        if( RECORD )
        	rec.start();
        synth.start();
        lineOut.start();
       
        // Get synthesizer time in seconds.
		double startTime = synth.getCurrentTime();
		double endTime = createSong( new TimeStamp( startTime+(spb) ) ) + 2.0;
		
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
	     if( RECORD ) {
		     rec.stop();
		     rec.close();
	     }
    }
    
    private double createSong( TimeStamp ts ) {
    	double t = 0.0;
    	double[] notes = Note.getNoteAscPattern( "D", new int[]{ 0,0,0,0 }, 2, Note.MIXOLYDIAN, 10 );
    	int foo = 0;
    	foo = 1;
    	// intro:
    	// Fmaj7
    	/*wasp.scheduleMeasure( WASP_F_01		, ts.makeRelative(t), 4 );
		//rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
		t += spm * 4;
    	// Amin7
		wasp.scheduleMeasure( WASP_A_02		, ts.makeRelative(t), 4 );
    	rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
    	t += spm * 4;*/
    	// schedule the parts together:
    	for( int l = 1; l < 16; l ++ ) {
    		// Fmaj7
    		/*wasp.scheduleMeasure( WASP_F_01		, ts.makeRelative(t), 4 );
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
	    		organ.scheduleMeasure( ORGAN_G,  ts.makeRelative(t), 4 );
	    	}
	    	rik.scheduleMeasure( DRUM_DBHATS, 	  ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// Amin7
	    	wasp.scheduleMeasure( WASP_A_02		, ts.makeRelative(t), 4 );
	    	if( l==1 )
	    		organ.scheduleMeasure( ORGAN_A,  ts.makeRelative(t), 4 );
	    	rik.scheduleMeasure( DRUM_DEFAULT	, ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	*/
	    	// =========== Couplet ============== //
	    	// D7
	    	winer.scheduleMeasure( WINE_D_01,     ts.makeRelative(t), 4 );
			wasp.scheduleMeasure( WASP_D_01		, ts.makeRelative(t), 4 );
	    	if( l%2==1 )
	    		organ.scheduleMeasure( ORGAN_D	, ts.makeRelative(t), 4 );
	    	rik.scheduleMeasure( DRUM_DBHATS	, ts.makeRelative(t), 4 );
	    	t += spm * 4;
	    	// Amin7
	    	wasp.scheduleMeasure( WASP_A_01, 	  ts.makeRelative(t), 4 );
	    	if( l%2==1 )
	    		organ.scheduleMeasure( ORGAN_A,  ts.makeRelative(t), 4 );
	    	rik.scheduleMeasure( DRUM_DBHATS, 	  ts.makeRelative(t), 4 );
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
		  { 1.00, 0.00, 0.00, 0.00, 0.00, 0.00, 1.50, 0.00, 0.00, 0.00 }, // base
		  { 0.00, 0.00, 0.00, 1.00, 0.00, 0.00, 0.00, 0.00, 1.00, 0.00 }, // snare
		  { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }, // hat open
		  { 0.50, 0.55, 0.10, 0.05, 0.50, 0.05, 0.50, 0.05, 0.50, 0.05 }  // hat closed
	};
	private static final double[][] vijfkwartDBHats = new double[][]{
		//  1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
		  { 1.00, 0.00, 0.00, 0.00, 0.00, 0.00, 1.50, 0.00, 0.00, 0.00 }, // base
		  { 0.00, 0.00, 0.00, 1.00, 0.00, 0.00, 0.00, 0.00, 1.00, 0.00 }, // snare
		  { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 }, // hat open
		  { 0.10, 0.50, 0.10, 0.50, 0.10, 0.50, 0.10, 0.50, 0.10, 0.50 }  // hat closed
	};
	private static final DrumMeasureData DRUM_DBHATS  = new DrumMeasureData( spb/2, vijfkwartDBHats );
	private static final DrumMeasureData DRUM_DEFAULT = new DrumMeasureData( spb/2, vijfkwart );
    
	// ==================================================================== ORGAN :
	// organ measures:
	private static final double[] ORGAN_AMPS_01 = new double[]{
		//	1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
			1.10, 1.10, 0.40, 0.20, 0.10, 0.05, 1.10, 1.10, 0.40, 0.20
	};
	private static final double[] ORGAN_AMPS_02 = new double[]{
		//	1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
			0.10, 0.01, 0.01, 0.10, 0.01, 0.01, 0.10, 0.01, 0.00, 0.01
	};
	private static final double[][] ORGAN_AMP_ENVS_01 = new double[][]{
		{
			0.0, 0.0,
			spb/4*.1, 0.06,
			spb/4*.9, 0.0,
		}
	};
	private static final int[] ORGAN_ARP_01 = new int[]{ 0,3,2,1 };
	private static final int[] ORGAN_ARP_02 = new int[]{ 3,2,1,0 };
	private static final int[] ORGAN_ARP_03 = new int[]{ 3,1,2,0 };
	private static final int[] ORGAN_ARP_04 = new int[]{ 0,2,1,3 };
	private static final OscChordMeasureData ORGAN_G 	= 	new OscChordMeasureData( spb/2, Gmaj7, ORGAN_AMPS_01, ORGAN_ARP_01, ORGAN_AMP_ENVS_01, new int[]{0} );
	private static final OscChordMeasureData ORGAN_A 	= 	new OscChordMeasureData( spb/2, Amin7, ORGAN_AMPS_01, ORGAN_ARP_01, ORGAN_AMP_ENVS_01, new int[]{0} );
	private static final OscChordMeasureData ORGAN_D 	= 	new OscChordMeasureData( spb/2, D7,    ORGAN_AMPS_02, ORGAN_ARP_01, ORGAN_AMP_ENVS_01, new int[]{0} );
	
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
    {	0.50, 0.50, 0.00, 0.00, 0.00, 0.00, 0.30, 0.20, 0.00, 0.00  };
    private static final double [] WASP_AMPS_1_15_2_25_4_45 =
    //  1.....ú.....2.....ú.....3.....ú.....4.....ú.....5.....ú.....
    {	0.50, 0.50, 0.00, 0.50, 0.40, 0.00, 0.40, 0.30, 0.00, 0.00  };
    // envelopes 
    private static final double[] WASP_ENV_4M = { // 4 measures long envelope
    	spb*4, 0.5,
    	spb*4, 1.0,
    	spb*4, 1.0,
    	spb*4, 0.9,
    	spb*4, 0.0,
    };
    private static final double[] WASP_ENV_SPB = {
    	spb/5, 0.4,
    	spb/5, 0.8,
    	spb/5, 0.5,
    	spb/5, 0.3,
    	spb/5, 0.0
    };
    private static final double[] WASP_ENV_SPB_H = {
    	spb/10, 0.4,
    	spb/10, 0.8,
    	spb/10, 0.5,
    	spb/10, 0.3,
    	spb/10, 0.0
    };
    // Wasp measures :
    private static final MonoWaspMeasureData WASP_F_01 = new MonoWaspMeasureData( spb/2, 	WASP_AMPS_1_15_4_45, WASP_NOTES_F, 	new double[][]{ WASP_ENV_SPB_H } );
    private static final MonoWaspMeasureData WASP_A_01 = new MonoWaspMeasureData( spb/2, 	WASP_AMPS_1_15_4_45, WASP_NOTES_A, 	new double[][]{ WASP_ENV_SPB_H } );
    private static final MonoWaspMeasureData WASP_A_02 = new MonoWaspMeasureData( spb/2,	WASP_AMPS_1_15, WASP_NOTES_A, 		new double[][]{ WASP_ENV_SPB_H } );
    private static final MonoWaspMeasureData WASP_D_01 = new MonoWaspMeasureData( spb/2, 	WASP_AMPS_1_15, WASP_NOTES_D,		new double[][]{ WASP_ENV_SPB_H } );
    private static final MonoWaspMeasureData WASP_D_M4 = new MonoWaspMeasureData( spb/2,	WASP_AMPS_1_15_4_45, WASP_NOTES_D, 	new double[][]{ WASP_ENV_SPB_H } );
    
    // Winer measures :
    private static final double SOA = .3;
    private static final SlideOscMeasureData WINE_D_01 = new SlideOscMeasureData( spb/2, 
    		new double[]{ SOA,SOA,SOA,SOA,SOA,SOA,SOA,SOA,SOA,SOA }, 
    		Note.getNoteAscPattern( "D", new int[]{ 0,4,2,1 }, 3, Note.MIXOLYDIAN, 10 ) );
    private static final boolean RECORD = false;
}
