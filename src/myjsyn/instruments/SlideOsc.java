package myjsyn.instruments;

import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.Multiply;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitSource;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.softsynth.shared.time.TimeStamp;

public class SlideOsc extends Circuit implements UnitSource{
	private UnitOscillator osc;
	private VariableRateMonoReader ampEnvPlayer;
	private VariableRateMonoReader freqEnvPlayer; // sliding frequency envelopes
	
	public UnitOutputPort output;
	
	public SlideOsc( UnitOscillator osc ){
		this.osc = osc;
		add( osc );
		add( ampEnvPlayer = new VariableRateMonoReader() );
		add( freqEnvPlayer = new VariableRateMonoReader() );
		ampEnvPlayer.output.connect( osc.amplitude );
		freqEnvPlayer.output.connect( osc.frequency );
		output = osc.output;
	}
	public double scheduleMeasure( SlideOscMeasureData d, TimeStamp ts, int loops ){
		double t = 0.0;
		osc.frequency.set( d.notes[0] );
		for( int l=0; l<loops; l++ ){
			for( int ai=0; ai<d.amps.length; ai++ ){
				int ni = 0; // current note index
				int si = 0; // current slideoffset index
				int aei = 0; // current ampEnvelope index
				double amp = d.amps[ai]; // get ref the beat amp
				if( amp>0.0 ){
					// get note + slideoffset for this amp
					double note = d.notes[ni];
					double slideOffset = d.slideBeatOffsets[si];
					ni ++; if( ni==d.notes.length ){ ni = 0; }
					si ++; if( si==d.slideBeatOffsets.length ){ si = 0; }
					double nextNote = d.notes[ni];
					double[] ampData = d.ampEnvelopes[aei].clone();
					for( int aeii=1; aeii<ampData.length; aeii++ ){
						ampData[aeii] *= amp;
					}
					aei ++; if( aei==d.ampEnvelopes.length ){ aei = 0; }
					
					// create slide frequency envelope data
					double [] freqData = {
						slideOffset, note,
						d.spb, nextNote
					};
					// queue envelope data
					SegmentedEnvelope fEnv = new SegmentedEnvelope( freqData );
					SegmentedEnvelope aEnv = new SegmentedEnvelope( ampData );
					ampEnvPlayer.dataQueue.queue( aEnv, 0, aEnv.getNumFrames(), ts );
					freqEnvPlayer.dataQueue.queue( fEnv, 0, fEnv.getNumFrames(), ts );
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
}
