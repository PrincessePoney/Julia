import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputViewer {
	private static int N;
	private static String PATH_FILE = "output/part-r-00000";

	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out
					.println("usage : java -cp target/julia-0.0.1-SNAPSHOT.jar OutputViewer <size> [path]");
			System.out.println("default path is " + PATH_FILE);
			System.exit(0);
		}
		N = Integer.parseInt(args[0]);
		if (args.length > 1) {
			PATH_FILE = args[1];
		}
		Path path = Paths.get(PATH_FILE);
		Picture pic = new Picture(N, N);
		try {
			for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
				String[] inputs = line.split(" ");
				String[] rightInputs = inputs[1].split("\t");
				Integer x = Integer.parseInt(inputs[0]);
				Integer y = Integer.parseInt(rightInputs[0]);
				Integer color = Integer.parseInt(rightInputs[1]);
				if (x < N && y < N) {
					pic.set(x, y, new Color(color));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		pic.show();
	}
}
