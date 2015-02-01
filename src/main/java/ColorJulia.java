import java.awt.Color;

public class ColorJulia {

	// return number of iterations to check z is in the Julia set of c
	static int julia(Complex c, Complex z, int ITERS) {
		for (int t = 0; t < ITERS; t++) {
			if (z.abs() > 2.0)
				return t;
			z = z.times(z).plus(c);
		}
		return ITERS - 1;
	}

	public static void main(String[] args) {
		double real = Double.parseDouble("-0.7589"); // a
		double imag = Double.parseDouble("-0.0753"); // b
		Complex c = new Complex(real, imag); // c = a + ib
		double xmin = -2.0;
		double ymin = -2.0;
		double width = 4.0;
		double height = 4.0;

		int N = 600;
		int ITERS = 256;

		// read in color map
		Color[] colors = new Color[ITERS];
		for (int t = 0; t < ITERS; t++) {
			 int r = (255-t) % 256;
			 int g = (255-t) % 256;
			 int b = (255-t) % 256;
			 colors[t] = new Color(r, g, b);
		}
		Picture pic = new Picture(N, N);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				
				// ça c'est l'algo à faire map reducé
				
				double x = xmin + i * width / N;
				double y = ymin + j * height / N;
				Complex z = new Complex(x, y);
				int t = julia(c, z, ITERS);
				
				pic.set(i, j, colors[t]);
				
				
			}
		}
		pic.show();
	}

}