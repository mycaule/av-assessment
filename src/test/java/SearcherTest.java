import junit.framework.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.io.IOException;
import java.util.*;

public class SearcherTest extends TestCase {
  public SearcherTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(SearcherTest.class);
  }

  public void testReadFiles() throws IOException {
    Map<String, List<String>> files = Searcher.readFiles("songs");
    assertThat(files.size()).isEqualTo(4);
    assertThat(files.get("I will survive.txt").size()).isEqualTo(373);
    assertThat(files.get("September.txt").size()).isEqualTo(287);
    assertThat(files.get("Sir Duke.txt").size()).isEqualTo(278);
    assertThat(files.get("Upside down.txt").size()).isEqualTo(332);
  }

  public void testGetScore() {
    assertThat(Searcher.getScore(Arrays.asList("a", "b", "c"), Arrays.asList("d"))).isEqualTo(0.0);
    assertThat(Searcher.getScore(Arrays.asList("a", "b", "c"), Arrays.asList("a", "d"))).isEqualTo(50.0);
    assertThat(Searcher.getScore(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c", "d"))).isEqualTo(75.0);
    assertThat(Searcher.getScore(Arrays.asList("a", "b", "c"), Arrays.asList("a"))).isEqualTo(100.0);
    assertThat(Searcher.getScore(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b"))).isEqualTo(100.0);
    assertThat(Searcher.getScore(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c"))).isEqualTo(100.0);
  }

  public void testComputeScores() {
    Map<String, List<String>> files = new HashMap<>();
    files.put("01", Arrays.asList("a", "b", "c", "d"));
    files.put("02", Arrays.asList("e", "b", "c", "d"));
    files.put("03", Arrays.asList("e", "f", "c", "d"));
    files.put("04", Arrays.asList("e", "f", "g", "d"));

    List<String> query = Arrays.asList("a", "b", "c");
    Map<String, Double> scores = Searcher.computeScores(files, query);

    assertThat(scores.get("01")).isEqualTo(100.0);
    assertThat(scores.get("02")).isEqualTo(66.0);
    assertThat(scores.get("03")).isEqualTo(33.0);
    assertThat(scores.get("04")).isEqualTo(0.0);
  }

  public void testSortByKey() {
    Map<String, Double> scores = new HashMap<>();
    scores.put("01", 1.0);
    scores.put("02", 7.0);
    scores.put("03", 2.0);
    scores.put("04", 8.0);
    scores.put("05", 3.0);
    scores.put("06", 9.0);
    scores.put("07", 4.0);
    scores.put("08", 10.0);
    scores.put("09", 5.0);
    scores.put("10", 11.0);
    scores.put("11", 6.0);
    scores.put("12", 12.0);

    Map<String, Double> sortedScores = Searcher.sortByKey(scores);
    Object[] objs = sortedScores.entrySet().toArray();
    assertThat(String.valueOf(objs[0])).isEqualTo("12=12.0");
    assertThat(String.valueOf(objs[1])).isEqualTo("10=11.0");
    assertThat(String.valueOf(objs[2])).isEqualTo("08=10.0");
    assertThat(String.valueOf(objs[9])).isEqualTo("05=3.0");
    assertThat(sortedScores.size()).isEqualTo(10);
    assertThat(scores.size()).isGreaterThan(10);
  }
}
