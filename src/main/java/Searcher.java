import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Searcher {
  // returns pairs of filename, linked list of words
  static Map<String, List<String>> readFiles(String directory) throws IOException {
    Map<String, List<String>> dict = new HashMap<>();

    final File indexableDirectory = new File(directory);

    // Read all the text and build in-memory representation
    Files.list(indexableDirectory.toPath())
      .forEach(path -> {
        try {
          List<String> words = Files.lines(path)
            .flatMap(Pattern.compile("\\P{L}+")::splitAsStream)
            .collect(Collectors.toList());

          dict.put(path.toString().replaceFirst(directory + "/", ""), words);
        }
        catch (IOException e) {
          System.out.print(path);
          e.printStackTrace();
        }
      });

    System.out.println(dict.size() + " files read in directory " + directory);
    return dict;
  }

  // Compute score over 100
  static Double getScore(List<String> words, List<String> query) {
    Integer count = query.stream().parallel()
      .mapToInt(s -> words.stream().anyMatch(x -> x.equalsIgnoreCase(s)) ? 1 : 0)
      .sum();

    double score = 100 * count / query.size();
    return score;
  }

  static Map<String, Double> computeScores(Map<String, List<String>> files, List<String> query) {
    return files.entrySet().stream().parallel()
      .collect(Collectors.toMap(x -> x.getKey(), x -> getScore(x.getValue(), query)));
  }

  static Map<String, Double> sortByKey(Map<String, Double> scores) {
    Map<String, Double> result = new LinkedHashMap<>();

    scores.entrySet().stream().parallel()
      .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
      .limit(10)
      .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

    return result;
  }

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      throw new IllegalArgumentException("No directory given to index.");
    }

    Map<String, List<String>> files = readFiles(args[0]);

    try (Scanner keyboard = new Scanner(System.in)) {
      while (true) {
        System.out.print("search> ");
        final String line = keyboard.nextLine();

        if (line.equals(":quit")) {
          System.out.print("");
          return;
        }

        // Search indexed files for words in line
        final List<String> words = Arrays.stream(line.split(" "))
          .filter(x -> !x.equals(""))
          .collect(Collectors.toList());

        if (words.size() > 0) {
          Map<String, Double> scores = computeScores(files, words);

          if (!scores.values().stream().anyMatch(y -> y > 0))
            System.out.println("no matches found");
          else {
            sortByKey(scores).forEach((k, s) -> {
              if (s > 0)
                System.out.println(String.format(k + ": %1$.0f%%", s));
            });
          }
        }
      }
    }
  }
}
