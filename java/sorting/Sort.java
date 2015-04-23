import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class Sort {
   private interface Sorter {
      List<String> sort(List<String> listToSort) throws Exception;
      String type();
   }

   private class BubbleSorter implements Sorter {
      public String type() { return "B"; };

      public List<String> sort(List<String> listToSort) throws Exception {
         if (_verbose) System.out.println("Bubble Sorting");

         // sort
         boolean sorted = false;
         while (!sorted) {
            sorted = true;
            for (int i = 0; i < listToSort.size() - 1; i++) {
               if (listToSort.get(i).compareTo(listToSort.get(i+1)) > 0) {
                  String temp = listToSort.get(i);
                  listToSort.set(i, listToSort.get(i+1));
                  listToSort.set(i+1, temp);
                  sorted = false;
               }
            }
         }

         return listToSort;
      }
   }

   private class SelectionSorter implements Sorter {
      public String type() { return "S"; };

      public List<String> sort(List<String> listToSort) throws Exception {
         if (_verbose) System.out.println("Selection Sorting");

         List<String> sortedList = new LinkedList<>(listToSort);

         // sort
         for (int i = 0; i < sortedList.size(); i++) {
            int minIndex = i;
            String minValue = sortedList.get(minIndex);
            for (int j = i; j < sortedList.size(); j++) {
               if (sortedList.get(j).compareTo(minValue) < 0) {
                  minIndex = j;
                  minValue = sortedList.get(j);
               }
            }
            if (minIndex > i) {
               sortedList.remove(minIndex);
               sortedList.add(i, minValue);
            }
         }

         return sortedList;
      }
   }

   private class InsertionSorter implements Sorter {
      public String type() { return "I"; };

      public List<String> sort(List<String> listToSort) throws Exception {
         if (_verbose) System.out.println("Insertion Sorting");

         List<String> sortedList = new LinkedList<>(listToSort);

         // sort
         for (int i = 1; i < sortedList.size(); i++) {
            String currentValue = sortedList.get(i);
            for (int j = 0; j < i; j++) {
               if (currentValue.compareTo(sortedList.get(j)) < 0) {
                  sortedList.remove(i);
                  sortedList.add(j, currentValue);
                  break;
               }
            }
         }

         return sortedList;
      }
   }

   private class MergeSorter implements Sorter {
      public String type() { return "M"; };

      public List<String> sort(List<String> listToSort) throws Exception {
         if (_verbose) System.out.println("Merge Sorting");

         // sort
         return mergeSort(listToSort);
      }

      private List<String> mergeSort(List<String> subList) {
         if (subList.size() < 2) return subList;

         List<String> left =
            mergeSort(new ArrayList<>(subList.subList(0,                  subList.size() / 2)));
         List<String> right =
            mergeSort(new ArrayList<>(subList.subList(subList.size() / 2, subList.size()    )));

         return merge(left, right);
      }

      private List<String> merge(List<String> left, List<String> right) {
         List<String> merged = new ArrayList<>(left.size() + right.size());

         int lSize = left.size();
         int rSize = right.size();
         for (int l = 0, r = 0; l < lSize || r < rSize; ) {
            if (l < lSize && r < rSize) {
               if (left.get(l).compareTo(right.get(r)) <= 0) merged.add(left.get(l++));
               else merged.add(right.get(r++));
            } else if (l < lSize) { // and r == rSize
               merged.addAll(left.subList(l, lSize));
               break;
            } else { // (i < rSize) and i == lSize
               merged.addAll(right.subList(r, rSize));
               break;
            }
         }

         return merged;
      }
   }

   private class HeapSorter implements Sorter {
      public String type() { return "H"; };

      public List<String> sort(List<String> listToSort) throws Exception {
         if (_verbose) System.out.println("Heap Sorting");

         List<String> sortedList = listToSort;

         // sort

         return sortedList;
      }
   }

   private class QuickSorter implements Sorter {
      public String type() { return "Q"; };

      public List<String> sort(List<String> listToSort) throws Exception {
         if (_verbose) System.out.println("Quick Sorting");

         List<String> sortedList = listToSort;

         // sort

         return sortedList;
      }
   }

   private static final int EST_BYTES_PER_LINE = 20;

   private Sorter _sorter = null;
   private Path _testDir = null;
   private Path _outputDir = null;
   private Map<Path, List<String>> _listsToSort = null;
   private boolean _verbose = false;

   private Map<Path, List<String>> loadTestLists() throws IOException {

      Map<Path, List<String>> listsToSort = new HashMap<>();

      try (DirectoryStream<Path> testDirStream = Files.newDirectoryStream(_testDir)) {

         for (Path testFile : testDirStream) {

            if (Files.isRegularFile(testFile)) {
               try (BufferedReader reader = Files.newBufferedReader(testFile)) {
                  int estNumLines = (int)(Files.size(testFile) / EST_BYTES_PER_LINE);

                  List<String> testList = new ArrayList<>(estNumLines);
                  for (String testString = reader.readLine(); testString != null; testString = reader.readLine()) {
                     testList.add(testString.trim());
                  }

                  listsToSort.put(testFile, testList);

                  reader.close();
               }
            }
         }

         testDirStream.close();
      }

      return listsToSort;
   }

   private void writeSortedList(List<String> sortedList, Path testFile, String sortType, long timeNanos) {
      System.out.format("%s: %s: %.3fms%n", sortType, testFile, timeNanos/1000000.0f);
      if (_verbose) System.out.format("%s%n%n", sortedList);

      try (PrintWriter writer =
            new PrintWriter(
               Files.newOutputStream(
                  getOutputFile(testFile, sortType),
                  StandardOpenOption.CREATE,
                  StandardOpenOption.TRUNCATE_EXISTING))) {

         sortedList.stream().forEach(s -> writer.println(s));

         writer.close();
      } catch (IOException ioex) {
         System.out.println("Unexpected error writing output file!");
         System.out.println(ioex);
      }
   }
   private Path getOutputFile(Path testFile, String sortType) {
      return _outputDir.resolve(Paths.get(sortType + "." + testFile.getFileName().toString()));
   }

   private Sort(String algoSpecifier, Path testDir, Path outputDir) throws Exception {
      _testDir = testDir;
      _outputDir = outputDir;

      if (!Files.exists(_testDir)) throw new Exception("Specified test directory " + _testDir + " not found!");
      if (!Files.isDirectory(_testDir)) throw new Exception("Specified test directory " + _testDir + " is not a directory!");

      if (!Files.exists(_outputDir)) throw new Exception("Specified output directory " + _outputDir + " not found!");
      if (!Files.isDirectory(_outputDir)) throw new Exception("Specified output directory " + _outputDir + " is not a directory!");

      _listsToSort = loadTestLists();

      switch (algoSpecifier) {
         case "-b": {
            _sorter = new BubbleSorter();
            break;
         }
         case "-s": {
            _sorter = new SelectionSorter();
            break;
         }
         case "-i": {
            _sorter = new InsertionSorter();
            break;
         }
         case "-m": {
            _sorter = new MergeSorter();
            break;
         }
         case "-h": {
            _sorter = new HeapSorter();
            break;
         }
         case "-q": {
            _sorter = new QuickSorter();
            break;
         }
         case "--all": {
            break;
         }
         default: {
            throw new Exception("Invalid argument: " + algoSpecifier);
         }
      }
   }

   private void runTest() throws Exception {
      for (Map.Entry<Path, List<String>> testListEntry : _listsToSort.entrySet()) {
         List<String> sortedList;
         long startTimeNanos, endTimeNanos;

         if (_sorter != null) {
            startTimeNanos = System.nanoTime();
            sortedList = _sorter.sort(testListEntry.getValue());
            endTimeNanos = System.nanoTime();
            writeSortedList(sortedList, testListEntry.getKey(), _sorter.type(), endTimeNanos - startTimeNanos);
         } else { // --all case
            Sorter sorters[] = {
               new BubbleSorter(),
               new SelectionSorter(),
               new InsertionSorter(),
               new MergeSorter(),
               new HeapSorter(),
               new QuickSorter()
            };

            for (Sorter sorter : Arrays.asList(sorters)) {
               startTimeNanos = System.nanoTime();
               sortedList = sorter.sort(testListEntry.getValue());
               endTimeNanos = System.nanoTime();
               writeSortedList(sortedList, testListEntry.getKey(), sorter.type(), endTimeNanos - startTimeNanos);
            }
         }
      }
   }

   public static void main(String[] args) {
      if (args.length < 3) {
         usage();
      }

      try {
         Sort sortTester = new Sort(args[0].toLowerCase(), Paths.get(args[1]), Paths.get(args[2]));
         sortTester._verbose = (args.length == 4 && args[3].equalsIgnoreCase("--verbose"));
         sortTester.runTest();
      } catch (Exception ex) {
         System.out.println(ex.getMessage());
         usage();
      }
   }
   public static void usage() {
      System.out.println("Usage: java Sort <algorithm> <test_directory> <output_directory> [--verbose]");
      System.out.println("   where:");
      System.out.println("      <algorithm> is one of:");
      System.out.println("         -b - bubble sort");
      System.out.println("         -s - selection sort");
      System.out.println("         -i - insertion sort");
      System.out.println("         -m - merge sort");
      System.out.println("         -h - heap sort");
      System.out.println("         -q - quick sort");
      System.out.println("         --ALL - run all of the above algorithms");
      System.out.println("      <test_directory> is the path of the directory containing test files with data to be sorted.");
      System.out.println("      <output_directory> is the path of the directory to which sorted output files will be written.");
      System.exit(-1);
   }
}