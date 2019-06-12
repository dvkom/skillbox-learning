import numberGenerators.Generatable;
import numberGenerators.original.OriginalNumberGenerator;
import numberGenerators.producerConsumer.producerConsumerNumberGenerator;

public class Loader {
  static volatile long start;

  public static void main(String[] args)  {
    start = System.nanoTime();
    Generatable original = new OriginalNumberGenerator();
    original.generate();
    System.out.println("Original finished " + (System.nanoTime() - start)/1_000_000 + " ms");

//    start = System.nanoTime();
//    Generatable optimized = new OptimizedNumberGenerator();
//    optimized.generate();
//    System.out.println("Optimized finished " + (System.nanoTime() - start)/1_000_000 + " ms");
//
//    start = System.nanoTime();
//    Generatable notVeryOptimized = new producerConsumerNumberGenerator();
//    notVeryOptimized.generate();
//    System.out.println("notVeryOptimized finished " + (System.nanoTime() - start)/1_000_000 + " ms");
  }
}

