package forg.treesum;

import com.google.common.hash.HashFunction;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiConsumer;

@SuppressWarnings("UnstableApiUsage")
public class TreeSummer {
	
	public static Path run(Path dir, HashFunction checksummer, BiConsumer<Path, String> reporter) throws IOException {
		return Files.walkFileTree(dir, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String checksum = checksumOf(file, checksummer);
				
				// Report checksum.
				reporter.accept(file, checksum);
				
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
				throw e;
			}
		});
	}
	
	private static String checksumOf(Path file, HashFunction h) throws IOException {
		ByteSource bs = com.google.common.io.Files.asByteSource(file.toFile());
		return bs.hash(h).toString();
	}
}
