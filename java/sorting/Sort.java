import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.util.Collections.swap;

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

         // sort
         for (int i = 0; i < listToSort.size(); i++) {
            int minIndex = i;
            K minValue = listToSort.get(minIndex);
            for (int j = i+1; j < listToSort.size(); j++) {
               if (listToSort.get(j).compareTo(minValue) < 0) {
                  minIndex = j;
                  minValue = listToSort.get(j);
               }
            }
            if (minIndex > i) {
               swap(listToSort, i, minIndex);
            }
         }

         return listToSort;
      }
   }

   private class InsertionSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Insrt"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Insertion Sorting");

         // sort
         for (int i = 1; i < listToSort.size(); i++) {
            K currentValue = listToSort.get(i);
            for (int j = 0; j < i; j++) {
               if (currentValue.compareTo(listToSort.get(j)) < 0) {
                  moveItem(listToSort, i, j);
                  break;
               }
            }
         }

         return listToSort;
      }
   }
   
   private void moveItem(List<?> listToSort, int fromIndex, int toIndex) {
      if (fromIndex <= toIndex)
         throw new IllegalArgumentException(
            "moveItem: fromIndex must be > toIndex -- got fromIndex = " + fromIndex
            + " and toIndex = " + toIndex);
      
      for (int i = fromIndex; i > toIndex; i--) swap(listToSort, i, i-1);
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

         // sort
         heapify(listToSort);
         for (int endIndex = listToSort.size() - 1; endIndex > 0; ) {
            // Move top node of heap (max value) to end of list:
            swap(listToSort, 0, endIndex);

            // New heap is reduced in size by 1. Restore newly shortened heap:
            siftDown(listToSort, 0, --endIndex);
         }

         return listToSort;
      }

      private void heapify(List<K> list) {
         // start at parent of last element in list
         int endIndex = list.size() - 1;
         for (int startIndex = parentIndex(endIndex); startIndex >= 0; startIndex--) {
            siftDown(list, startIndex, endIndex);
         }
      }

      private void siftDown(List<K> list, int startIndex, int endIndex) {
         int rootIndex = startIndex;
         for (int leftChildIndex = leftChildIndex(rootIndex), rightChildIndex = leftChildIndex + 1;
                  leftChildIndex <= endIndex;
                  leftChildIndex = leftChildIndex(++rootIndex), rightChildIndex = leftChildIndex + 1) {
            int swapIndex = rootIndex;
            if (list.get(rootIndex).compareTo(list.get(leftChildIndex)) < 0)
               swapIndex = leftChildIndex;

            // See if right child (if any) is bigger than root or left child:
            if (rightChildIndex <= endIndex
                  && list.get(swapIndex).compareTo(list.get(rightChildIndex)) < 0)
               swapIndex = rightChildIndex;

            // Anything to swap?
            if (swapIndex != rootIndex) {
               swap(list, rootIndex, swapIndex);
            }
         }
      }

      private int parentIndex(int i) { return Math.floorDiv(i - 1, 2); }
      private int leftChildIndex(int i) { return (2 * i) + 1; }
      //private int rightChildIndex(int i) { return (2 * i) + 2; }
   }

   private class QuickSorter<K extends Comparable<K>> implements Sorter<K> {
      public String type() { return "Quick"; }

      public List<K> sort(List<K> listToSort) throws Exception {
         if (_verbose) System.out.println("Quick Sorting");

         // sort
         quickSort(listToSort, 0, listToSort.size() - 1);

         return listToSort;
      }

      private void quickSort(List<K> list, int startIndex, int endIndex) {
         if (startIndex >= endIndex) return;
         int pivotIndex = partition(list, startIndex, endIndex);
         quickSort(list, startIndex, pivotIndex - 1);
         quickSort(list, pivotIndex + 1, endIndex);
      }

      private int partition(List<K> list, int startIndex, int endIndex) {
         int pivotIndex = choosePivotIndex(list, startIndex, endIndex);
         K pivotValue = list.get(pivotIndex);

         // stash pivot value at end of list:
         swap(list, pivotIndex, endIndex);

         // sort rest of list based on pivot value:
         int swapIndex = startIndex;
         for (int i = startIndex; i < endIndex; i++) {
            if (list.get(i).compareTo(pivotValue) <= 0) {
               swap(list, swapIndex, i);
               swapIndex++;
            }
         }

         // swap pivot value back from end of list to last swapIndex:
         swap(list, swapIndex, endIndex);

         // return new pivot index:
         return swapIndex;
      }

      private int choosePivotIndex(List<K> list, int startIndex, int endIndex) {
         int middleIndex = (startIndex + endIndex) / 2;
         K vStart = list.get(startIndex);
         K vMiddle = list.get(middleIndex);
         K vEnd = list.get(endIndex);

         if      ((vStart.compareTo(vMiddle) <= 0 && vMiddle.compareTo(vEnd) <= 0)
                    ||
                  (vEnd.compareTo(vMiddle) <= 0 && vMiddle.compareTo(vStart) <= 0))
            return middleIndex;

         else if ((vMiddle.compareTo(vStart) <= 0 && vStart.compareTo(vEnd) <= 0)
                    ||
                  (vEnd.compareTo(vStart) <= 0 && vStart.compareTo(vMiddle) <= 0))
            return startIndex;

         else /* ((vStart.compareTo(vEnd) <= 0 && vEnd.compareTo(vMiddle) <= 0)
                    ||
                  (vMiddle.compareTo(vEnd) <= 0 && vEnd.compareTo(vStart) <= 0))*/
            return endIndex;
      }
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
            writeSortedList(sortedList, testListEntry.getKey(), sorter.type().substring(0,1), endTimeNanos - startTimeNanos);
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