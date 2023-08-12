import java.util.*;
import java.util.concurrent.*;

public class Main {

   public static void main(String[] args) throws InterruptedException, ExecutionException {
      String[] texts = new String[25];
      for (int i = 0; i < texts.length; i++) {
         texts[i] = generateText("aab", 30_000);
      }

      long startTs = System.currentTimeMillis(); // start time

      //my code
      final ExecutorService threadPool = Executors.newFixedThreadPool(texts.length);
      List<Future> futures = new ArrayList<>();
      for (int i = 0; i < texts.length; i++) {
         int finalI = i;
         final Callable logic = () -> getMaxSize(texts[finalI]);
         final Future task = threadPool.submit(logic);
         futures.add(task);
      }

      int max = 0;
      for (Future f : futures) {
         if ((int) f.get() > max) {
            max = (int) f.get();
         }
      }
      System.out.println("Результат (макс. интервал): " + max);
      threadPool.shutdown();

      long endTs = System.currentTimeMillis(); // end time
      System.out.println("Time: " + (endTs - startTs) + " ms");
   }

   public static int getMaxSize(String text) {
      int maxSize = 0;
      for (int i = 0; i < text.length(); i++) {
         for (int j = 0; j < text.length(); j++) {
            if (i >= j) {
               continue;
            }
            boolean bFound = false;
            for (int k = i; k < j; k++) {
               if (text.charAt(k) == 'b') {
                  bFound = true;
                  break;
               }
            }
            if (!bFound && maxSize < j - i) {
               maxSize = j - i;
            }
         }
      }
      return maxSize;
   }

   public static String generateText(String letters, int length) {
      Random random = new Random();
      StringBuilder text = new StringBuilder();
      for (int i = 0; i < length; i++) {
         text.append(letters.charAt(random.nextInt(letters.length())));
      }
      return text.toString();
   }
}