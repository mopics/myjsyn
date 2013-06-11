package myjsyn;

import java.io.File;
import java.io.IOException;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.data.ShortSample;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.shared.time.TimeStamp;

public class SampleTest {
	// synth stuff
    private Synthesizer synth;
    private LineOut lineOut;
    
    // file stuff
    private File file;
    private FloatSample bass;
    private FloatSample snare;
    private FloatSample hat;
    private VariableRateMonoReader bassPlayer;
    private VariableRateMonoReader snarePlayer;
    private VariableRateMonoReader hatPlayer;
    
    public void go() throws IOException {
    	// initialise instance variables
        synth = JSyn.createSynthesizer();
        synth.add( lineOut = new LineOut() );
    	
        // load samples
        bass = SampleLoader.loadFloatSample( new File("/Users/peter/Documents/workspace-adt/samples/drum/rikBass01.aiff") );
		snare = SampleLoader.loadFloatSample( new File("/Users/peter/Documents/workspace-adt/samples/drum/rikSnare02.aiff") );
		hat = SampleLoader.loadFloatSample( new File("/Users/peter/Documents/workspace-adt/samples/drum/rikHat01.aiff") );
        // create sample players
		bassPlayer = new VariableRateMonoReader();
		snarePlayer = new VariableRateMonoReader();
		hatPlayer = new VariableRateMonoReader();
		
		// add players to main synth
		synth.add( bassPlayer );
		synth.add( snarePlayer );
		synth.add( hatPlayer );
		
		// connect players to output
		bassPlayer.output.connect( 0, lineOut.input, 0 );
		snarePlayer.output.connect( 0, lineOut.input, 0 );
		hatPlayer.output.connect( 0, lineOut.input, 0 );
		
		// set players rates
		bassPlayer.rate.set( bass.getFrameRate() );
		snarePlayer.rate.set( snare.getFrameRate() );
		hatPlayer.rate.set( hat.getFrameRate() );
		
		double spb = .3;
		double totalTime = 0.0;
		TimeStamp firstBeat = new TimeStamp( synth.getCurrentTime()+.5 );
		
		for( int i=0; i<10; i++ ){
			bassPlayer.dataQueue.queue( bass, 0, bass.getNumFrames(), firstBeat.makeRelative( totalTime ) );
			bassPlayer.dataQueue.queue( bass, 0, bass.getNumFrames(), firstBeat.makeRelative( (2*spb)+totalTime ) );
			
			snarePlayer.dataQueue.queue( snare, 0, snare.getNumFrames(), firstBeat.makeRelative( (1*spb)+totalTime ) );
			snarePlayer.dataQueue.queue( snare, 0, snare.getNumFrames(), firstBeat.makeRelative( (3*spb)+totalTime ) );
			
			hatPlayer.dataQueue.queue(hat, 0, hat.getNumFrames(), firstBeat.makeRelative( totalTime ) );
			hatPlayer.dataQueue.queue(hat, 0, hat.getNumFrames(), firstBeat.makeRelative( (1*spb)+totalTime ) );
			hatPlayer.dataQueue.queue(hat, 0, hat.getNumFrames(), firstBeat.makeRelative( (2*spb)+totalTime ) );
			hatPlayer.dataQueue.queue(hat, 0, hat.getNumFrames(), firstBeat.makeRelative( (3*spb)+totalTime ) );
			
			totalTime += 4*spb;
		}
		
		// Start synthesizer using default stereo output at 44100 Hz.
        synth.start();
        
        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        lineOut.start();
        
        try
        {
        	// Wait until the sample has finished playing.
        	synth.sleepFor( totalTime );
        } 
        catch( InterruptedException e )
        {
            e.printStackTrace();
        }
         System.out.println( "Stop playing. -------------------" );
	     // Stop everything.
	     synth.stop();
    	
    }
    
    public static void main( String[] args ){
        try {
			new SampleTest().go();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
