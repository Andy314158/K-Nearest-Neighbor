
public class Flower {
	double[] measure;

	{
		measure = new double[4];
	}

	String name;

	Flower(double slength, double swidth, double plength, double pwidth, String name) {
		this.measure[0] = slength;
		this.measure[1] = swidth;
		this.measure[2] = plength;
		this.measure[3] = pwidth;
		this.name = name;
	}
 
}