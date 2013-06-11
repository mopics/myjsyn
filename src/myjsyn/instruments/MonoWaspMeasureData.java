package myjsyn.instruments;

public class MonoWaspMeasureData {
	public double spb; 		// seconds per beat, or per arpeggio-%-amp>0.0
	public double[] notes; 	// if length smaller then amps.length go in arpeggio mode
	public double[] amps;   // amplitude per beat ( amps.length == number of beats in measure )
	public double[] lpFreqs;// lowpass frequency per beat 	0.0 - 2000.0
	public double[] lpQs;	// lowpass Q per beat 			0.0-10.0
	public int[] arpeggio;  // arpeggio order of notes ( only used if notes.length < amps.length )
	
	public double[][] envelopes; 	// envelope data per beat, or per arpeggio-%-amp>0.0
	public int[] envelopesArpeggio; // arpeggio order of envelopes ( only used if envelopes.length < amps.length )
	
	
	public MonoWaspMeasureData( double spb, double[] amps, double[] notes ){
		this.spb = spb;
		this.amps = amps;
		this.notes = notes;
		// make default arpeggio
		this.arpeggio = new int[notes.length];
		for( int i=0; i<notes.length; i++ ){
			arpeggio[i] = i;
		}
		// make default envelope data
		this.envelopes = new double[][]{ {
			0.03569336872710687, 1.8158995815899581, 
            0.20137254829147777, 0.4476987447698745, 
            0.15203536048332827, 0.23430962343096234, 
            0.013830509411172281, 0.05439330543933055, 
            0.02869234510673449, 0.08368200836820083, 
            0.019337277666689773, 0.012552301255230125, 
		} };
		this.envelopesArpeggio = new int[] { 0 };
	}
	public MonoWaspMeasureData( double spb, double[] amps, double[] notes, int[] arpeggio ){
		this.spb = spb;
		this.amps = amps;
		this.notes = notes;
		this.arpeggio = arpeggio;
		// make default envelope data
		this.envelopes = new double[][]{ {
			0.03569336872710687, 1.8158995815899581, 
            0.20137254829147777, 0.4476987447698745, 
            0.15203536048332827, 0.23430962343096234, 
            0.013830509411172281, 0.05439330543933055, 
            0.02869234510673449, 0.08368200836820083, 
            0.019337277666689773, 0.012552301255230125, 
		} };
		this.envelopesArpeggio = new int[] { 0 };
	}
	public MonoWaspMeasureData( double spb, double[] amps, double[] notes, double[][] envelopes ){
		this.spb = spb;
		this.amps = amps;
		this.notes = notes;
		// make default arpeggio
		this.arpeggio = new int[notes.length];
		for( int i=0; i<notes.length; i++ ){
			arpeggio[i] = i;
		}
		this.envelopes = envelopes;
		// make default envelope-arpeggio
		this.envelopesArpeggio = new int[envelopes.length];
		for( int i=0; i<envelopes.length; i++ ){
			envelopesArpeggio[i] = i;
		}
	}
	public MonoWaspMeasureData( double spb, double[] amps, double[] notes, int[] arpeggio,
			double[][] envelopes, int[] envelopesArpeggio ){
		this.spb = spb;
		this.amps = amps;
		this.notes = notes;
		this.arpeggio = arpeggio;
		this.envelopes = envelopes;
		this.envelopesArpeggio = envelopesArpeggio;
	}
}
