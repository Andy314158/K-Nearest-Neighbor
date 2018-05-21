
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class KNN {
	
	private final int k = 3;
	private ArrayList<Flower> testingList = new ArrayList<Flower>();
	private ArrayList<Flower> trainingList = new ArrayList<Flower>();
	private ArrayList<String> predictionList = new ArrayList<String>();

	
	private void Knn(String training, String test) {
        double correct=0;//used to give an accuracy measure at the end
		testingList = load(new File(test));
		trainingList = load(new File(training));
		

		for (int i = 0; i < testingList.size(); i++) {
			Flower[] neighbours = getNeighbours((Flower) testingList.get(i), k);
			String result = getResponses(neighbours);
			predictionList.add(result);
			if(result.equals(((Flower) testingList.get(i)).name))
				correct++;
			System.out.println("predicted: " + result + " | actual: " + ((Flower) testingList.get(i)).name);
		}
		System.out.print("Accuracy: " + 100*correct/testingList.size() + "%");

	}

	/**
	 * Takes in an index which determines what range it is returning.
	 * @param i
	 * @return
	 */
	private double calculateRange(int i) {
		double max = 0;
		double min = 1000;
		for (Flower flower : trainingList) {
			if (max < flower.measure[i])
				max = flower.measure[i];
			if (min > flower.measure[i])
				min = flower.measure[i];
		}
		return max - min;
	}

	
	private ArrayList<Flower> load(File file) {
		ArrayList<Flower> list = new ArrayList<Flower>();
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				double x1 = sc.nextDouble();
				double x2 = sc.nextDouble();
				double x3 = sc.nextDouble();
				double x4 = sc.nextDouble();
				String name = sc.next();
				Flower f = new Flower(x1, x2, x3, x4, name);
				list.add(f);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * Compares the distance between the first and second flower, each euclidean value for each 
	 * value in the flower is divided by the range of the value across the whole set. 
	 * @param first
	 * @param second
	 * @param size
	 * @return
	 */
	private double EuclideanDistance(Flower first, Flower second, int size) {
		double dist = 0;
		for (int i = 0; i < size; i++) {
			dist += Math.pow((first.measure[i] - second.measure[i]), 2) / calculateRange(i);
		}
		return Math.sqrt(dist);
	}
	

	private Flower[] getNeighbours(Flower testInstance, int k) {
		List<FlowerDouble> distances = new ArrayList<>();
		double dist;
		int length = testInstance.measure.length - 1;

		for (int i = 0; i < trainingList.size(); i++) {
			dist = EuclideanDistance(testInstance, (Flower) trainingList.get(i), length);
			distances.add(new FlowerDouble((Flower) trainingList.get(i), dist));
		}
		Collections.sort(distances);

		Flower[] neighbours = new Flower[k];

		for (int i = 0; i < k; i++) {
			neighbours[i] = distances.get(i).flower;
		}

		return neighbours;
	}

	/**
	 * iterates through the neighbors list which is of size k, returns the majority neighbor
	 * @param neighbours
	 * @return
	 */
	private String getResponses(Flower[] neighbours) { 
		
		int setosa = 0;
		int versicolor = 0;
		int virginica = 0;

		for (int i = 0; i < neighbours.length; i++) {
			if (neighbours[i].name.equals("Iris-setosa"))
				setosa++;
			if (neighbours[i].name.equals("Iris-virginica"))
				virginica++;
			if (neighbours[i].name.equals("Iris-versicolor"))
				versicolor++;
		}

		if (setosa > versicolor && setosa > virginica) {
			return "Iris-setosa";
		} else if (versicolor > setosa && versicolor > virginica) {
			return "Iris-versicolor";
		}
		return "Iris-virginica";
	}

	
	class FlowerDouble implements Comparable<FlowerDouble> {
		Flower flower;
		double num;

		FlowerDouble(Flower flower, double num) {
			this.flower = flower;
			this.num = num;
		}

		@Override
		public int compareTo(FlowerDouble o) {
			if (num <= o.num)
				return -1;
			else
				return 1;
		}

	}

	public static void main(String[] args) {
		new KNN().Knn(args[0], args[1]);
	}

}
