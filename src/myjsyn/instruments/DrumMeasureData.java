package myjsyn.instruments;

public class DrumMeasureData {
	public double spb; // seconds per beat 
	public double[][] pattern; // contains amps + rythem pattern
	
	public DrumMeasureData( double spb, double[][] pattern ){
		this.spb = spb; this.pattern = pattern;
	}
}
