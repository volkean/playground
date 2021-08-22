package vaks.com;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Parses chrome net log export file and prints host names in it.
 * 
 * On chrome browser go chrome://net-export/ page and take a capture, then use
 * this parser to list host names.
 * 
 */
public class NetLogParser {

	public static void main(String args[]) {
		ObjectMapper objectMapper = new ObjectMapper();
		Set<String> hosts = new TreeSet<String>();
		InputStream netLogFileAsStream = null;
		try {
			netLogFileAsStream = NetLogParser.class.getClassLoader().getResourceAsStream("chrome-net-export-log.json");
			JsonNode tree = objectMapper.readTree(netLogFileAsStream);
			List<JsonNode> nodes = tree.findValues("url");
			nodes.addAll(tree.findValues("origin"));
			for (JsonNode node : nodes) {
				URL url = null;
				try {
					url = new URL(node.asText());
					hosts.add(url.getHost());
				} catch (Exception e) {
					// simply ignore invalid URL
				}
			}
			// hosts.stream().forEach(t -> System.out.println(t));
			// dirty sort by domain
			List<String> sorted = hosts.stream()
					.sorted((url1, url2) -> new StringBuilder().append(url1).reverse().toString()
							.compareTo(new StringBuilder().append(url2).reverse().toString()))
					.collect(Collectors.toList());
			sorted.forEach(t -> System.out.println(t));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (netLogFileAsStream != null) {
					netLogFileAsStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
