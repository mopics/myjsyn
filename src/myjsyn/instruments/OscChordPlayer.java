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

public class OscChordPlayer extends Circuit implements UnitSource {
	// oscillators
	private UnitOscillator[] voices;
    private VariableRateMonoReader ampEnv;
    
    
	/* Declare ports. */
	public UnitOutputPort output;
	
	public OscChordPlayer( UnitOscillator[] voices ){
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
	public double scheduleMeasure( OscChordMeasureData d, TimeStamp ts, int loops ){
		ampEnv.dataQueue.clear();
		// 
		if( d.spn>d.spb )
			d.spn = d.spb;
		for( int li=0; li<loops; li++ ){
			int cai = 0; // current arpeggio index
			int eai = 0; // current ampEnvArpeggio index
			for( int ai=0; ai<d.amps.length; ai++ ){
				
				double amp = d.amps[ai]; // get ref the beat amp
				
				if( amp > 0.0 ){
					if( d.arpeggio!=null ){
						voices[0].frequency.set( d.chord[d.arpeggio[cai]], ts.getTime() );
						// play other chord notes 
						for( int i=1; i<voices.length; i++ ){
							voices[i].frequency.set( d.chord[i], ts.getTime() );
						}
						cai ++;
						if( cai==4 ) { // chord can only have 4 tones 
							cai = 0;
						}
					}
					else {
						// set chord 
						for( int i=0; i<voices.length; i++ ){
							voices[i].frequency.set( d.chord[i], ts.getTime() );
							voices[i].amplitude.set( amp, ts.getTime() );
						}
					}
					
					// get ampEnv
					double[] data = d.ampEnvs[ eai ].clone();
					eai ++;
					if( eai==d.ampEnvsArpeggio.length ) eai = 0;
					for( int i=1; i<data.length; i+=2 ){
						data[i] *= amp;
					}
					SegmentedEnvelope aEnv = new SegmentedEnvelope( data );
					ampEnv.dataQueue.queue( aEnv, 0, aEnv.getNumFrames(), ts );
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
		return output;
	}

	@Override
	public UnitGenerator getUnitGenerator() {
		// TODO Auto-generated method stub
		return null;
	}
}

