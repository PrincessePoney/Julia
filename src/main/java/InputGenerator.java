public class InputGenerator {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out
					.println("usage : java -cp target/julia-0.0.1-SNAPSHOT.jar InputGenerator <size>");
			System.exit(0);
		}
		int N = Integer.parseInt(args[0]);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.println(i + " " + j);
			}
		}
	}
}
