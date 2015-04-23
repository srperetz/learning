import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class Exercise1 {
   private static final int EST_BYTES_PER_LINE = 60;

   private Path _path = null;
   private long _numLines;

   private Exercise1(Path path, int numLines) {
      _path = path;
      _numLines = numLines;
   }

   private void processFile() {
      try (BufferedReader reader = Files.newBufferedReader(_path)) {
         int estNumLines = (int)(Files.size(_path) / EST_BYTES_PER_LINE);

         List<String> lines = new ArrayList<String>(estNumLines);

         for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            lines.add(line);
         }

         reader.close();

         Collections.shuffle(lines);

         int linesPrinted = 0;
         for (int i = 0; linesPrinted < _numLines && i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().length() > 0) {
               System.out.println(line);
               linesPrinted++;
            }
         }
      } catch (IOException ioex) {
         System.out.format("Unable to open file %s.%n", _path);
         System.exit(-1);
      }
   }

   public static void main(String[] args) {
      if (args.length < 2) {
         usage();
      }

      try {
         Exercise1 s = new Exercise1(Paths.get(args[0]), Integer.parseUnsignedInt(args[1]));
         s.processFile();
      }
      catch (NumberFormatException nfex) {
         System.out.format("Invalid argument %s. Number of lines must be a non-negative integer value.%n", args[1]);
         System.exit(-1);
      }
   }
   public static void usage() {
      System.out.println("Usage: java Exercise1 <path> <number_of_lines>");
      System.exit(-1);
   }
}