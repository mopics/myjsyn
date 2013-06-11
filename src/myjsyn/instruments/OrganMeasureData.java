package myjsyn.instruments;

public class OrganMeasureData {
	public double spb; // seconds per beat 
	public double spn; // seconds per note ( if larger thenn spb, set to spb )
	public double[] chord; // contains chord note -frequencies to play
	public double[] amps; // contains amps + rythem pattern
	public int[] arpeggio; // contains arpeggio order of chord notres ( chord array indexes, f.e: [ 0,2,1,3 ] )
	
	public OrganMeasureData( double spb, double spn, double[] chord, double[] amps ){
		this.spb = spb; this.spn = spn; this.chord = chord; this.amps = amps;
	}
	public OrganMeasureData( double spb, double spn, double[] chord, double[] amps, int[] arpeggio ){
		this.spb = spb; this.spn = spn; this.chord = chord; this.amps = amps; this.arpeggio = arpeggio;
	}
}
