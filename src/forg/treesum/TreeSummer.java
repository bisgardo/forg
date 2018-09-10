package forg.treesum;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;

public class TreeSummer {
	
	public static void main(String... args) throws IOException {
		if (args.length != 1) {
			System.err.println("Takes exactly one argument: directory");
			System.exit(1);
		}
		
		HashFunction hasher = Hashing.murmur3_128();
		
		Path dir = Paths.get(args[0]);
		if (!Files.isDirectory(dir)) {
			System.err.println("Not a directory: " + dir.toString());
			System.exit(1);
		}
		
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String checksum = checksumOf(file, hasher);
				
				// Report checksum.
				printEntry(file, checksum);
				
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
				throw e;
			}
		});
	}
	
	private static String checksumOf(Path file, HashFunction hasher) throws IOException {
		ByteSource bs = com.google.common.io.Files.asByteSource(file.toFile());
		return bs.hash(hasher).toString();
	}
	
	private static void printEntry(Path file, String checksum) {
		System.out.println(file.toString() + '\t' + checksum);
	}
}
