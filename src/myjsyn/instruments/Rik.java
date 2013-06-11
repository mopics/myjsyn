package myjsyn.instruments;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.UnitSource;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.shared.time.TimeStamp;

public class Rik extends Circuit implements UnitSource {
 	
 	private Random random = new Random();
    private FloatSample[] samples;
    private VariableRateMonoReader[] players;
    public static final int BASE = 0;
    public static final int SNARE = 1;
    public static final int HHAT = 2;
    
    /* Declare ports. */
	public UnitOutputPort output;
    
    public Rik( String[] files ) throws IOException{
    	// load samples
		samples = new FloatSample[files.length];
		players = new VariableRateMonoReader[files.length];
		PassThrough pt = new PassThrough();
		for( int i=0; i<files.length; i++ ){
			samples[i] =  SampleLoader.loadFloatSample( new File( files[i] ) );
			players[i] = new VariableRateMonoReader();
			players[i].rate.set( samples[i].getFrameRate() );
			// register
			add( players[i] );
			// connect
			players[i].output.connect( 0, pt.input, 0 );
		}
		output = pt.output;
	}
    
    // scheduling drum parts methods
    public double scheduleMeasure( DrumMeasureData d, TimeStamp ts, int loops ){
    	TimeStamp mts = ts;
    	for( int li=0; li<loops; li++ ){
    		
    		for( int pi=0; pi<d.pattern.length; pi++ ){
    			
    			mts = ts;
    			for( int pai=0; pai<d.pattern[pi].length; pai ++ ) {
    				double amp = d.pattern[pi][pai];
    				if( amp>0.0 ){
    					VariableRateMonoReader player = players[pi];
	    				player.amplitude.set( amp, mts.getTime() );
	        			FloatSample sample = samples[pi];
	        			
	        			player.dataQueue.queue( sample, 0, sample.getNumFrames(), mts );
	        			
    				}
    				mts = mts.makeRelative( d.spb );
    			}
    		}
    		ts = ts.makeRelative( d.spb*d.pattern[0].length );
    	}
    	
    	return d.spb * d.pattern[0].length * loops;
    }
    
    
	@Override
	public UnitOutputPort getOutput() {
		// TODO Auto-generated method stub
		return output;
	}
}
