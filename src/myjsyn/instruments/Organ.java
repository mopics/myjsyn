package myjsyn.instruments;

import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.softsynth.shared.time.TimeStamp;

public class Organ extends Circuit implements UnitSource {
	// oscillators
	private UnitOscillator[] voices;
    private VariableRateMonoReader ampEnv;
    
    
	/* Declare ports. */
	public UnitOutputPort output;
	
	public Organ( UnitOscillator[] voices ){
		this.voices = voices;
		// create ampEnvelope
		ampEnv = new VariableRateMonoReader();
		ampEnv.dataQueue.clear();
		
		// ====== ADD & CONNECT Units====== //
		// add ampEnv ( Player )
		add( ampEnv );
		// create passthrough to outpt all oscillaters to a single circuit output
		PassThrough ps = new PassThrough();
		for( int i=0; i<voices.length; i++ ){
			voices[i].noteOff();
			// add osc to circuit
			add( voices[i] );
			// connect ampEnvPlayer to osc amplitude-port
			ampEnv.output.connect( voices[i].amplitude );
			// connect osc output to circuit's output
			voices[i].output.connect( 0, ps.input, 0 );
		}
		// link circuit's output to passthrough's output
		output = ps.output;
	}
	
	/**
	 * Schedules a measure based on MeasureData
	 * @param data
	 * @return
	 */
	public double scheduleMeasure( OrganMeasureData d, TimeStamp ts, int loops ){
		ampEnv.dataQueue.clear();
		// 
		if( d.spn>d.spb )
			d.spn = d.spb;
		for( int li=0; li<loops; li++ ){
			int cai = 0; // current arpeggio index
			for( int ai=0; ai<d.amps.length; ai++ ){
				
				double amp = d.amps[ai]; // get ref the beat amp
				if( d.arpeggio!=null ){
					if( amp > 0.0 ){
						voices[0].noteOn( d.chord[d.arpeggio[cai]], amp, ts );
						for( int i=1; i<voices.length; i++ ){
							voices[i].noteOn( 0.0, 0.0, ts );
						}
						cai ++;
						if( cai==4 ) { // chord can only have 4 tones 
							cai = 0;
						}
					}
				}
				else {
					// set chord 
					for( int i=0; i<voices.length; i++ ){
						voices[i].noteOn( d.chord[i], amp, ts );
					}
				}
				
				// set simple amp envelope data ( TODO: add env data option in MeasureData class?? )
				double[] data = {
					0.0, 0.0,			// dot 1
					d.spn*.1, 0.9*amp,	// dot 2
					d.spn*.9, 0.0,		// dot 3
				};
				SegmentedEnvelope aEnv = new SegmentedEnvelope( data );
				ampEnv.dataQueue.queue( aEnv, 0, aEnv.getNumFrames(), ts );
				
				// update timestamp
				ts = ts.makeRelative( d.spb );
			}
		}
		
		return d.spb * d.amps.length * loops;
	}
	@Override
	public UnitOutputPort getOutput() {
		// TODO Auto-generated method stub
		return output;
	}

	@Override
	public UnitGenerator getUnitGenerator() {
		// TODO Auto-generated method stub
		return null;
	}
}

