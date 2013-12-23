package myjsyn.instruments;

public class OscChordMeasureData {
	public double spb; // seconds per beat 
	public double spn; // seconds per note ( if larger thenn spb, set to spb )
	public double[] chord; // contains chord note -frequencies to play
	public double[] amps; // contains amps + rythem pattern
	public int[] arpeggio; // contains arpeggio order of chord notres ( chord array indexes, f.e: [ 0,2,1,3 ] )
	public double[][] ampEnvs; // contains envelope data
	public int[] ampEnvsArpeggio; //contains arpeggio for ampEnvs
	
	public OscChordMeasureData( double spb, double spn, double[] chord, double[] amps ){
		this.spb = spb; this.spn = spn; this.chord = chord; this.amps = amps;
		makeDefaultAmpEnvs();
	}
	public OscChordMeasureData( double spb, double spn, double[] chord, double[] amps, int[] arpeggio ){
		this.spb = spb; this.spn = spn; this.chord = chord; this.amps = amps; this.arpeggio = arpeggio;
		makeDefaultAmpEnvs();
	}
	public OscChordMeasureData( double spb, double[] chord, double[] amps, int[] arpeggio, 
			double[][] ampEnvs, int[] ampEnvsArpeggio ) {
		this.spb = spb; this.chord = chord; this.amps = amps; this.arpeggio = arpeggio;
		this.ampEnvs = ampEnvs; this.ampEnvsArpeggio = ampEnvsArpeggio;
	}
	private void makeDefaultAmpEnvs(){
		// make default ampEnvs + arpeggio
		ampEnvs = new double[][]{{
			0.0, 0.0,
			spn*.1, 0.1,
			spn*.9, 0.0,
		}};
		ampEnvsArpeggio = new int[]{ 0 };
	}
}
