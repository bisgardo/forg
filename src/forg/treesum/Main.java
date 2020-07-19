package forg.treesum;

import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	
	@SuppressWarnings("UnstableApiUsage")
	public static void main(String... args) throws IOException {
		if (args.length != 1) {
			System.err.println("Takes exactly one argument: directory");
			System.exit(1);
		}
		
		Path dir = Paths.get(args[0]);
		if (!Files.isDirectory(dir)) {
			System.err.println("Not a directory: " + dir.toString());
			System.exit(1);
		}
		
		TreeSummer.run(dir, Hashing.murmur3_128(), Main::printEntry);
	}
	
	public static void printEntry(Path file, String checksum) {
		System.out.println(file.toString() + '\t' + checksum);
	}
}
