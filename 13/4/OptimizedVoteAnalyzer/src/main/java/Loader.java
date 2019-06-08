public class Loader {

  public static void main(String[] args) throws Exception {
//    String fileName = "res/data-18M.xml";
    String fileName = "res/data-1572M.xml";
    VoteAnalyzer analyzer = new VoteAnalyzer(fileName);
    long start = System.currentTimeMillis();
    analyzer.startAnalyze();
    System.out.println((System.currentTimeMillis() - start) + " ms");
  }
}