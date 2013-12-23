package myjsyn.instruments;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.UnitSource;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.SawtoothOscillatorDPW;
import com.jsyn.data.SegmentedEnvelope;
import com.softsynth.shared.time.TimeStamp;

public class MonoWasp extends Circuit implements UnitSource {
	
	// Declare units and ports.
    private SawtoothOscillator saw;
    private VariableRateMonoReader envPlay;
    private Multiply mult;
    private FilterLowPass lowPass;
    
    public UnitOutputPort output;
    
    public MonoWasp() {
    	// Create units
        add( saw  = new SawtoothOscillator() );
        add( envPlay = new VariableRateMonoReader() );
        add( mult = new Multiply() );
        add( lowPass = new FilterLowPass() );
        // Connect units
        saw.output.connect( lowPass.input ); 			// saw-out 		-> lowPass-in
        saw.frequency.set( 440.0 );						// saw-freq 	:  can be set by measure data : notes
        saw.amplitude.set( 1.0 );						// saw-amp		:  can be set by measure data : amps
        envPlay.output.connect( mult.inputB ); 			// envPlay-out 	-> mult-inputB
        mult.inputA.set( 550.0 );							// mult.inputA 	:  can be set by measure data : lpFreqs value
        envPlay.output.connect( lowPass.amplitude ); 	// envPlay-out 	-> lowPass-amp
        mult.output.connect( lowPass.frequency);		// mult-out 	-> lowPass-cutoff-freq
        lowPass.Q.set( 2.0 );				// lowPass-Q 	:  can be set by measuredata  : lpQs values 0 - 10
        
        // set circuits output
        output = lowPass.output;
    }
    public double scheduleMeasure( MonoWaspMeasureData d, TimeStamp ts, int loops ){
    	//TODO: scheduleMeasure using MonoWaspMeasureData
    	for( int l=0; l<loops; l++ ){
    		int cai  = 0; // current arpeggio index
    		int ecai = 0; // current envelope arpeggio index
    		
    		for( int ai=0; ai<d.amps.length; ai++ ){
    			double amp = d.amps[ai]; // get ref the beat amp
    			
    			if( amp > 0.0 ){
	    			// set current arpeggio note/frequency + amp to saw
	    			saw.noteOn( d.notes[d.arpeggio[cai]], amp, ts );
	    			// update note arpeggio
					cai ++;
					if( cai==d.arpeggio.length ) { // reset on arpeggio end 
						cai = 0;
					}
					// queue envelope data
					SegmentedEnvelope aEnv = new SegmentedEnvelope( d.envelopes[d.envelopesArpeggio[ecai]] );
					envPlay.dataQueue.queue( aEnv, 0, aEnv.getNumFrames(), ts );
					// update envelopeArpeggio
					ecai ++;
					if( ecai==d.envelopesArpeggio.length ){
						ecai = 0;
					}
					
    			}
    			// update timestamp
				ts = ts.makeRelative( d.spb );
    		}
    	}
    	return d.spb * d.amps.length * loops;
    }
    
	@Override
	public UnitOutputPort getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

}
