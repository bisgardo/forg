package forg.duplings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.HashMultimap;

class Dupling {
	public final String checksum;
	public final String[] filenames;
	
	public Dupling(String checksum, String[] filenames) {
		this.checksum = Objects.requireNonNull(checksum);
		this.filenames = Objects.requireNonNull(filenames);
	}
}

public class Duplings {
	
	public static void main(String... args) throws IOException {
		if (args.length != 0) {
			System.err.println("Usage: duplings");
			System.exit(1);
		}
		
		List<Dupling> dups = resolveSortedDuplicates();
		for (Dupling d : dups) {
			System.out.println(String.join("\t", d.filenames));
		}
	}
	
	private static List<Dupling> resolveSortedDuplicates() throws IOException {
		// Read mappings from stdin.
		HashMultimap<String, String> map = HashMultimap.create();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = input.readLine()) != null) {
			String[] parts = line.split("\t");
			if (parts.length != 2) {
				System.err.println("Malformed input line: " + line);
				System.exit(2);
			}
			
			String filename = parts[0];
			String checksum = parts[1];
			map.put(checksum, filename);
		}
		
		// Extract duplings and sort.
		List<Dupling> duplings = new ArrayList<>(map.size());
		for (Map.Entry<String, Collection<String>> e : map.asMap().entrySet()) {
			String checksum = e.getKey();
			Collection<String> filenames = e.getValue();
			int filenamesCount = filenames.size();
			if (filenamesCount <= 1) {
				continue;
			}
			
			String[] filenamesArray = filenames.toArray(new String[filenamesCount]);
			Arrays.sort(filenamesArray);
			
			duplings.add(new Dupling(checksum, filenamesArray));
		}
		duplings.sort(
				Comparator.comparingInt((Dupling d) -> d.filenames.length)
						.thenComparing(d -> d.filenames[0])
		);
		
		return duplings;
	}
}
