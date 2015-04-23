import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class Sort {
   private interface Sorter<K extends Comparable<K>> {
      List<K> sort(List<K> listToSort) throws Exception;
      String type();
   }

   private class BubbleSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Bubbl"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Bubble Sorting");

         // sort
         boolean sorted = false;
         while (!sorted) {
            sorted = true;
            for (int i = 0; i < listToSort.size() - 1; i++) {
               if (listToSort.get(i).compareTo(listToSort.get(i+1)) > 0) {
                  swap(listToSort, i, i+1);
                  sorted = false;
               }
            }
         }

         return listToSort;
      }
   }

   private class SelectionSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Selec"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Selection Sorting");

         List<K> sortedList = new LinkedList<>(listToSort);

         // sort
         for (int i = 0; i < sortedList.size(); i++) {
            int minIndex = i;
            K minValue = sortedList.get(minIndex);
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

   private class InsertionSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Insrt"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Insertion Sorting");

         List<K> sortedList = new LinkedList<>(listToSort);

         // sort
         for (int i = 1; i < sortedList.size(); i++) {
            K currentValue = sortedList.get(i);
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

   private class MergeSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Merge"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Merge Sorting");

         // sort
         return mergeSort(listToSort);
      }

      private List<K> mergeSort(List<K> subList) {
         if (subList.size() < 2) return subList;

         List<K> left =
            mergeSort(new ArrayList<>(subList.subList(0,                  subList.size() / 2)));
         List<K> right =
            mergeSort(new ArrayList<>(subList.subList(subList.size() / 2, subList.size()    )));

         return merge(left, right);
      }

      private List<K> merge(List<K> left, List<K> right) {
         List<K> merged = new ArrayList<>(left.size() + right.size());

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

   private class HeapSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Heaps"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Heap Sorting");

         List<K> sortedList = listToSort;

         // sort

         return sortedList;
      }

      private int parentIndex(int i) { return Math.floorDiv(i - 1, 2); }
      private int leftChildIndex(int i) { return (2 * i) + 1; }
      private int rightChildIndex(int i) { return (2 * i) + 2; }
   }

   private class QuickSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Quick"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Quick Sorting");

         List<K> sortedList = listToSort;

         // sort

         return sortedList;
      }
   }

   static <K> void swap(List<K> l, int i, int j) {
      K temp = l.get(i);
      l.set(i, l.get(j));
      l.set(j, temp);
   }

   private static final int EST_BYTES_PER_LINE = 20;

   private List<Sorter<String>> _sorters = new ArrayList<Sorter<String>>(6);
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
               }
            }
         }
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

      } catch (IOException ioex) {
         System.out.println("Unexpected error writing output file!");
         System.out.println(ioex);
      }
   }
   private Path getOutputFile(Path testFile, String sortType) {
      return _outputDir.resolve(Paths.get(sortType + "." + testFile.getFileName().toString()));
   }

   private Sort(List<String> algoSpecifier, Path testDir, Path outputDir) throws Exception {
      _testDir = testDir;
      _outputDir = outputDir;

      if (!Files.exists(_testDir)) throw new Exception("Specified test directory " + _testDir + " not found!");
      if (!Files.isDirectory(_testDir)) throw new Exception("Specified test directory " + _testDir + " is not a directory!");

      if (!Files.exists(_outputDir)) throw new Exception("Specified output directory " + _outputDir + " not found!");
      if (!Files.isDirectory(_outputDir)) throw new Exception("Specified output directory " + _outputDir + " is not a directory!");

      _listsToSort = loadTestLists();

      for (String spec : algoSpecifier) {
         switch (spec) {
            case "-b": {
               _sorters.add(new BubbleSorter<String>());
               break;
            }
            case "-s": {
               _sorters.add(new SelectionSorter<String>());
               break;
            }
            case "-i": {
               _sorters.add(new InsertionSorter<String>());
               break;
            }
            case "-m": {
               _sorters.add(new MergeSorter<String>());
               break;
            }
            case "-h": {
               _sorters.add(new HeapSorter<String>());
               break;
            }
            case "-q": {
               _sorters.add(new QuickSorter<String>());
               break;
            }
            case "--all": {
               _sorters.clear();
               _sorters.add(new BubbleSorter<String>());
               _sorters.add(new SelectionSorter<String>());
               _sorters.add(new InsertionSorter<String>());
               _sorters.add(new MergeSorter<String>());
               _sorters.add(new HeapSorter<String>());
               _sorters.add(new QuickSorter<String>());
               return;
            }
            default: {
               throw new Exception("Invalid argument: " + algoSpecifier);
            }
         }
      }
   }

   private void runTest() throws Exception {
      for (Map.Entry<Path, List<String>> testListEntry : _listsToSort.entrySet()) {
         List<String> sortedList;
         long startTimeNanos, endTimeNanos;

         for (Sorter<String> sorter : _sorters) {
            startTimeNanos = System.nanoTime();
            sortedList = sorter.sort(testListEntry.getValue());
            endTimeNanos = System.nanoTime();
            writeSortedList(sortedList, testListEntry.getKey(), sorter.type(), endTimeNanos - startTimeNanos);
         }
      }
   }

   public static void main(String[] args) {
      if (args.length < 3) {
         usage();
      }

      try {
         List<String> algoSpec = new ArrayList<String>(Arrays.asList(args).subList(2, args.length));
         boolean verbose = algoSpec.remove("--verbose");
         Sort sortTester = new Sort(algoSpec, Paths.get(args[0]), Paths.get(args[1]));
         sortTester._verbose = verbose;
         sortTester.runTest();
      } catch (Exception ex) {
         System.out.println(ex.getMessage());
         usage();
      }
   }
   public static void usage() {
      System.out.println("Usage: java Sort <test_directory> <output_directory> <algorithm>[ <algorithm>...]  [--verbose]");
      System.out.println("   where:");
      System.out.println("      <test_directory> is the path of the directory containing test files with data to be sorted.");
      System.out.println("      <output_directory> is the path of the directory to which sorted output files will be written.");
      System.out.println("      <algorithm> is one or more of:");
      System.out.println("         -b - bubble sort");
      System.out.println("         -s - selection sort");
      System.out.println("         -i - insertion sort");
      System.out.println("         -m - merge sort");
      System.out.println("         -h - heap sort");
      System.out.println("         -q - quick sort");
      System.out.println("       or:");
      System.out.println("         --all - run all of the above algorithms");
      System.exit(-1);
   }
}