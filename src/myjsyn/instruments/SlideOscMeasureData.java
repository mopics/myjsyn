package myjsyn.instruments;

public class SlideOscMeasureData extends AMeasureData {
	public double[] notes;
	public double[] slideBeatOffsets; // seconds after beat to start sliding
	public double[][] ampEnvelopes; 	// envelope data per beat, or per arpeggio-%-amp>0.0
	
	public SlideOscMeasureData( double spb, double[] amps, double[] notes ){
		super( spb, amps );
		this.notes = notes;
		createDefaultSlideEnvelope();
		createDefaultAmpEnvelopes();
	}
	public SlideOscMeasureData( double spb, double[] amps, double[] notes, double[] slideBeatOffsets ){
		super( spb, amps );
		this.notes = notes;
		this.slideBeatOffsets = slideBeatOffsets;
		createDefaultAmpEnvelopes();
	}
	public SlideOscMeasureData( double spb, double[] amps, double[] notes, double[] slideBeatOffsets, double[][] ampEnvelopes ){
		super( spb, amps );
		this.notes = notes;
		this.slideBeatOffsets = slideBeatOffsets;
		this.ampEnvelopes = ampEnvelopes;
	}
	
	private void createDefaultSlideEnvelope() {
		slideBeatOffsets = new double[]{ spb-spb/16 }; // start sliding half way of sbp
	}
	private void createDefaultAmpEnvelopes(){
		ampEnvelopes = new double[][]{ { 
			spb/4, 0.8,
			spb/4, 0.7,
			spb/4, 0.5,
			spb/4, 0.3
		} };
	}
}
