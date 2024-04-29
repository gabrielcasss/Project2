import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Iterator;
import java.io.*;
import java.util.*;
/**
 * This is a forestry simulation program that allows the user to manage multiple forests.
 * @author: Gabriel Castro
 * Enjoy!
 */

/**
 * Represents a Tree with species, planting year, height and growth rate.
 */
class Tree implements Serializable {
   // Global variables
    private TreeSpecies species;
    private int plantingYear;
    private double height;
    private double growthRate;


    /**
     * Constructs a new Tree with the given parameters.
     * @param species the species of the tree
     * @param plantingYear the year the tree was planted
     * @param height the height of the tree
     * @param growthRate the growth rate of the tree
     */
    public Tree(TreeSpecies species, int plantingYear, double height, double growthRate) {
        this.species = species;
        this.plantingYear = plantingYear;
        this.height = height;
        this.growthRate = growthRate;
    }

    public TreeSpecies getSpecies() {
        return species;
    }

    /**
     * Simulates the growth of the tree over one year.
     */
    public int getPlantingYear() {
        return plantingYear;
    }

    /**
     * Calculates the height of the tree.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Calculates the growth of the tree over one year.
     */
    public double getGrowthRate() {
        return growthRate;
    }

    /**
     * Simulates the growth of the tree over one year.
     */
    public void grow() {
        height *= (1 + growthRate / 100);
    }

    /**
     * Returns a string representation of the tree.
     *
     * @return a string representation of the tree
     */
    @Override
    public String toString() {
        return String.format("%d %s   %.1f'  %.1f%%", plantingYear, species, height, growthRate);
    }
}

class Forest implements Serializable {
    String name;
    ArrayList<Tree> trees;

    /**
     * Constructs a new Forest with the given name.
     *
     * @param name the name of the forest
     */
    public Forest(String name) {
        this.name = name;
        this.trees = new ArrayList<>();
    }

    /**
     * Plants a new tree in the forest.
     */
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    /**
     * Removes a tree from the forest by index.
     *
     * @return
     */
    public List<Tree[]> removeTree(int index) {
        if (index >= 0 && index < trees.size()) {
            trees.remove(index);
        }
        return null;
    }

    /**
     * Simulates the growth of all trees in the forest over one year.
     */
    public void growTrees() {
        for (Tree tree : trees) {
            tree.grow();
        }
    }
public void reapTrees(double heightThreshold) {
    Iterator<Tree> iterator = trees.iterator();
    while (iterator.hasNext()) {
        Tree tree = iterator.next();
        if (tree.getHeight() > heightThreshold) {
            iterator.remove();
        }
    }
}

        /**
         * calculates the average growth of all trees in the forest over one year.
         */
        public double calculateAverageHeight() {
            if (trees.isEmpty()) {
                return 0;
            }

            double totalHeight = 0;
            for (Tree tree : trees) {
                totalHeight += tree.getHeight();
            }

            return totalHeight / trees.size();
        }

        /**
         * Prints the forest to the console.
         */
        public void printForest() {
            System.out.println("Forest name: " + name);
            if (trees.isEmpty()) {
                System.out.println("The forest is empty.");
            } else {
                for (int i = 0; i < trees.size(); i++) {
                    Tree tree = trees.get(i);
                    System.out.printf("     %d %-7s %d %7.2f' %5.1f%%\n", i, tree.getSpecies(), tree.getPlantingYear(), tree.getHeight(), tree.getGrowthRate());                }
                System.out.println("There are " + trees.size() + " trees, with an average height of " + String.format("%.2f", calculateAverageHeight()));
            }
        }
    }// end of Forest class

/**
 * Simulates a forestry operation with multiple forests.
*/
public class ForestrySim {
        public static final double MIN_HEIGHT = 10.0;
        public static final double MAX_HEIGHT = 20.0;
        public static final double MIN_GROWTH_RATE = 0.1;
        public static final double MAX_GROWTH_RATE = 0.2;
    private static Scanner keyboard = new Scanner(System.in);

        /**
         * The main method of the ForestrySim class.
         *
         * @param args command-line arguments
         */
    public static void main(String[] args) {
        ArrayList<Forest> forests = new ArrayList<>(); // Initialize the ArrayList
        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");

        // Load forests from CSV files
        for (String forestName : args) {
            Forest forest = loadForestFromFile(forestName);
            if (forest != null) {
                forests.add(forest);
            }
        }
        //set the current forest index to 0
        int currentForestIndex = 0; // Close the scanner
        char choice;
        System.out.println("Initializing from " + args[currentForestIndex]);

        // Main menu loop
        do {
            //error handling for no forest index
            if (currentForestIndex >= forests.size()) {
                System.out.println("No more forests available. Exiting program.");
                return;
            }

            //menu options
            System.out.print("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (N)ext, e(X)it : ");
            choice = keyboard.next().charAt(0);
            keyboard.nextLine(); // Consume newline character

            switch (Character.toUpperCase(choice)) {
                case 'P':
                    forests.get(currentForestIndex).printForest();
                    break;
                case 'A':
                    // Add a new randomly generated tree
                    addRandomTree(forests.get(currentForestIndex));
                    break;
                case 'C':
                    // Cut down a tree by index
                    cutDownTree(forests.get(currentForestIndex));
                    break;
                case 'G':
                    // Simulate a year's growth
                    forests.get(currentForestIndex).growTrees();
                    break;
                case 'R':
                    // Reap the forest of trees over a specified height
                    reapForest(forests.get(currentForestIndex));
                    break;
                case 'S':
                    // Save the forest (serialized) to a .db file
                    saveForest(forests.get(currentForestIndex));
                    break;
                case 'L':
                    // Load a forest from a .db file
                    System.out.print("Enter the name of the forest to load: ");
                    String forestName = keyboard.nextLine();
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(forestName + ".db"))) {
                        Forest loadedForest = (Forest) ois.readObject();
                        forests.add(loadedForest);
                        System.out.println("Forest loaded successfully.");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error occurred while loading the forest: " + e.getMessage());
                    }
                    break;

                case 'N':
                    System.out.println("Moving to the next forest");
                    int nextForestIndex = (currentForestIndex + 1) % args.length; // calculate the index of the next forest
                    while (true) {
                        if (nextForestIndex >= args.length) {
                            System.out.println("No more forests to load. Returning to the first forest.");
                            nextForestIndex = 0; // reset to the first forest if we've tried all forests
                        }
                        System.out.println("Initializing from " + args[nextForestIndex]);
                        try {
                            Forest nextForest = loadForestFromFile(args[nextForestIndex]);
                            forests.add(nextForest);
                            currentForestIndex = forests.size() - 1; // update currentForestIndex to the last index of the list
                            System.out.println();
                            break; // break the loop if the forest is loaded successfully
                        } catch (Exception e) {
                            System.out.println("Error opening/reading " + args[nextForestIndex] + ".csv");
                            nextForestIndex = (nextForestIndex + 1) % args.length; // move to the next forest
                            if (nextForestIndex == currentForestIndex) { // if we've tried all forests and none could be loaded
                                System.out.println("No valid forests could be loaded. Exiting program.");
                                return;
                            }
                        }
                    }
                    break;
                case 'X':
                    // Exit the program
                    return;

                default:
                    System.out.println("Invalid menu option, try again");
            }
        } while (Character.toUpperCase(choice) != 'X');
    }// end of main method
    /**
     * Adds a random tree to the forest.
     *
     * @param forest the forest to which the tree will be added
     */
    private static void addRandomTree(Forest forest) {
        Random random = new Random();
        TreeSpecies species = TreeSpecies.values()[random.nextInt(TreeSpecies.values().length)];
        int plantingYear = Calendar.getInstance().get(Calendar.YEAR) - random.nextInt(10);
        double height = random.nextDouble() * MIN_HEIGHT + MAX_HEIGHT; // Random height between 10 and 20
        double growthRate = MIN_GROWTH_RATE + random.nextDouble() + MAX_GROWTH_RATE; // Random growth rate between 10 and 20
        Tree newTree = new Tree(species, plantingYear, height, growthRate);
        forest.addTree(newTree);
    }

    /**
     * Cuts down a tree in the forest.
     *
     * @param forest the forest from which the tree will be cut down
     */
    private static void cutDownTree(Forest forest) {
        System.out.print("Tree number to cut down: ");
        if (keyboard.hasNextInt()) {
            int index = keyboard.nextInt();
            keyboard.nextLine(); // Consume newline character
            if (index >= 0 && index < forest.trees.size()) { // Check against actual size
                forest.removeTree(index);
            } else {
                System.out.println("Tree number " + index + " does not exist");
            }
        } else {
            System.out.println("That is not an integer");
            keyboard.nextLine(); // Consume invalid input
        }
    }

    /**
     * Reaps trees from the forest.
     *
     * @param forest the forest from which the trees will be reaped
     */
    private static void reapForest(Forest forest) {
        System.out.print("Height threshold to reap trees: ");
        if (keyboard.hasNextDouble()) {
            double heightThreshold = keyboard.nextDouble();
            keyboard.nextLine(); // Consume newline character
            forest.reapTrees(heightThreshold);
            System.out.println("Trees above " + heightThreshold + " feet reaped.");
        } else {
            System.out.println("Invalid input for height threshold.");
            keyboard.nextLine(); // Consume invalid input
        }
    }

    /**
     * Loads a forest from a CSV file.
     *
     * @param forestName the name of the forest to load
     * @return the forest loaded from the file
     */
    private static Forest loadForestFromFile(String forestName) {
        Forest forest = new Forest(forestName);
        String line = "";
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader("src/" + forestName + ".csv"))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] treeData = line.split(csvSplitBy);

                TreeSpecies species = TreeSpecies.valueOf(treeData[0]);
                int plantingYear = Integer.parseInt(treeData[1]);
                double height = Double.parseDouble(treeData[2]);
                double growthRate = Double.parseDouble(treeData[3]);

                Tree newTree = new Tree(species, plantingYear, height, growthRate);
                forest.addTree(newTree);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error occurred while parsing the data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error occurred while converting tree species: " + e.getMessage());
        }

        return forest;
    }
    /**
     * Saves a forest to a .db file.
     *
     * @param forest the forest to save
     */
    private static void saveForest(Forest forest) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(forest.name + ".db"))) {
            oos.writeObject(forest);
            System.out.println("Forest saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving the forest: " + e.getMessage());
        }
    }
} // end of ForestrySim class

