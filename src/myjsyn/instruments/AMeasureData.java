package myjsyn.instruments;

public class AMeasureData {
	public double spb; // seconds per beat 
	public double[] amps; // amplitudes per beat
	
	public AMeasureData( double spb, double[] amps ){
		this.spb = spb; this.amps = amps;
	}
}
