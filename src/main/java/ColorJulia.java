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

		int N = 3000;
		int ITERS = 10000;

		Picture pic = new Picture(N, N);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {

				double x = xmin + i * width / N;
				double y = ymin + j * height / N;
				Complex z = new Complex(x, y);
				int t = julia(c, z, ITERS);

				pic.set(i,
						j,
						new Color(Color.HSBtoRGB(0.625f,
								1.0f - clamp(t / 256.0f, 0f, 1.0f),
								clamp(t / 256.0f, 0f, 1.0f))));

			}
		}
		pic.show();
	}

	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

}