import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.BufferedWriter;

int cardCompare(String card1, String card2) {
    String values = "123456789TJQK";
    String suits = "HCDS";

    int value1 = values.indexOf(card1.charAt(0));
    int value2 = values.indexOf(card2.charAt(0));

    int suit1 = suits.indexOf(card1.charAt(1));
    int suit2 = suits.indexOf(card2.charAt(1));

    if (value1 != value2) {
        return Integer.compare(value1, value2);
    } else {
        return Integer.compare(suit1, suit2);
    }
}

ArrayList<String> bubbleSort(ArrayList<String> array) {
    ArrayList<String> sortedArray = new ArrayList<>(array);

    for (int i = 0; i < sortedArray.size() - 1; i++) {
        for (int j = 0; j < sortedArray.size() - 1 - i; j++) {
            if (cardCompare(sortedArray.get(j), sortedArray.get(j + 1)) > 0) {
                String temp = sortedArray.get(j);
                sortedArray.set(j, sortedArray.get(j + 1));
                sortedArray.set(j + 1, temp);
            }
        }
    }
    return sortedArray;
}

ArrayList<String> mergeSort(ArrayList<String> array) {
    if (array.size() <= 1) {
        return array;
    }

    int mid = array.size() / 2;
    ArrayList<String> left = new ArrayList<>(array.subList(0, mid));
    ArrayList<String> right = new ArrayList<>(array.subList(mid, array.size()));

    return merge(mergeSort(left), mergeSort(right));
}

ArrayList<String> merge(ArrayList<String> left, ArrayList<String> right) {
    ArrayList<String> merged = new ArrayList<>();
    int i = 0, j = 0;

    while (i < left.size() && j < right.size()) {
        if (cardCompare(left.get(i), right.get(j)) <= 0) {
            merged.add(left.get(i));
            i++;
        } else {
            merged.add(right.get(j));
            j++;
        }
    }

    while (i < left.size()) {
        merged.add(left.get(i++));
    }
    while (j < right.size()) {
        merged.add(right.get(j++));
    }

    return merged;
}

long measureBubbleSort(String filename) throws IOException {
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(filename));
    ArrayList<String> cards = new ArrayList<>(lines);

    long startTime = System.nanoTime();
    bubbleSort(cards);
    long endTime = System.nanoTime();

    return (endTime - startTime) / 1_000_000;
}

long measureMergeSort(String filename) throws IOException {
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(filename));
    ArrayList<String> cards = new ArrayList<>(lines);

    long startTime = System.nanoTime();
    mergeSort(cards);
    long endTime = System.nanoTime();

    return (endTime - startTime) / 1_000_000;
}

void sortComparison(String[] filenames) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("sortComparison.csv"), StandardOpenOption.CREATE)) {
        writer.write("card,10,100,10000\n");
        writer.write("bubbleSort");
        long bubbleTime10 = 0, bubbleTime100 = 0, bubbleTime10000 = 0;
        long mergeTime10 = 0, mergeTime100 = 0, mergeTime10000 = 0;
        for (String filename : filenames) {
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(filename));
            int numOfCards = lines.size();
            if (numOfCards == 10) {
                bubbleTime10 = measureBubbleSort(filename);
                mergeTime10 = measureMergeSort(filename);
            } else if (numOfCards == 100) {
                bubbleTime100 = measureBubbleSort(filename);
                mergeTime100 = measureMergeSort(filename);
            } else if (numOfCards == 10000) {
                bubbleTime10000 = measureBubbleSort(filename);
                mergeTime10000 = measureMergeSort(filename);
            }
        }
        writer.write("," + bubbleTime10 + "," + bubbleTime100 + "," + bubbleTime10000 + "\n");
        writer.write("mergeSort");
        writer.write("," + mergeTime10 + "," + mergeTime100 + "," + mergeTime10000 + "\n");
    }
}

String[] files = {
    "coursework2/coursework2_files/sort10.txt", 
    "coursework2/coursework2_files/sort100.txt", 
    "coursework2/coursework2_files/sort10000.txt"
};

try {
    sortComparison(files);
} catch (IOException e) {
    e.printStackTrace();
}
