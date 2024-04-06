import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class problem2a3 {

    private static final int THREAD_COUNT = 8;
    private static final int MINUTES = 60;
    private static final int HOURS = 72;
    private final Lock lock = new ReentrantLock();
    private List<Integer> sensorReadings;
    private List<Boolean> sensorsReady = Collections.synchronizedList(new ArrayList<>(THREAD_COUNT));
    private static final Random random = new Random();

    public static void main(String[] args) {
        problem2a3 temperatureSensor = new problem2a3();
        temperatureSensor.startMeasurement();
    }

    private void startMeasurement() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            sensorsReady.add(false);
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executorService.submit(() -> measureTemperature(threadId));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (endTime - startTime) + "ms");
    }

    private void measureTemperature(int threadId) {
        for (int hour = 0; hour < HOURS; hour++) {
            // Initialize or reset the sensorReadings list at the start of each hour
            sensorReadings = Collections.synchronizedList(new ArrayList<>(Collections.nCopies(THREAD_COUNT * MINUTES, 0)));

            for (int minute = 0; minute < MINUTES; minute++) {
                int readingIndex = minute + (threadId * MINUTES);
                sensorsReady.set(threadId, false);
                sensorReadings.set(readingIndex, generateRandomNumber(-100, 70));
                sensorsReady.set(threadId, true);

                while (!allSensorsReady(threadId)) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if (threadId == 0) {
                lock.lock();
                try {
                    generateReport(hour);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private boolean allSensorsReady(int caller) {
        for (int i = 0; i < sensorsReady.size(); i++) {
            if (!sensorsReady.get(i) && caller != i) {
                return false;
            }
        }
        return true;
    }

    private void generateReport(int hour) {
        System.out.println("[Hour " + (hour + 1) + " report]");

        printLargestDifference();

        List<Integer> sortedReadings = new ArrayList<>(sensorReadings);
        Collections.sort(sortedReadings);

        List<Integer> reversedSortedReadings = new ArrayList<>(sortedReadings);
        Collections.reverse(reversedSortedReadings);

        printHighestTemperatures(reversedSortedReadings);
        printLowestTemperatures(sortedReadings);

        System.out.println();
    }

    private void printLargestDifference() {
        int step = 10;
        int startInterval = 0;
        int maxDifference = Integer.MIN_VALUE;

        for (int threadIndex = 0; threadIndex < THREAD_COUNT; threadIndex++) {
            int offset = threadIndex * MINUTES;

            for (int i = 0; i < MINUTES - step; i++) {
                List<Integer> subList = sensorReadings.subList(i + offset, i + offset + step);
                int max = Collections.max(subList);
                int min = Collections.min(subList);
                int diff = max - min;

                if (diff > maxDifference) {
                    maxDifference = diff;
                    startInterval = i + offset; // Adjusted for the reset readings
                }
            }
        }

        System.out.println("Largest temperature difference is " + maxDifference + "F starting at minute " + startInterval + " and ending at minute " + (startInterval + 10));  
    }
    
    private void printHighestTemperatures(List<Integer> readings) {
        Set<Integer> temperatures = new TreeSet<>(Collections.reverseOrder());

        for (int reading : readings) {
            temperatures.add(reading);
            if (temperatures.size() == 5) {
                break;
            }
        }

        System.out.print("Highest temperatures: ");
        for (int temperature : temperatures) {
            System.out.print(temperature + "F ");
        }
        System.out.println();
    }

    private void printLowestTemperatures(List<Integer> readings) {
        Set<Integer> temperatures = new TreeSet<>();

        for (int reading : readings) {
            temperatures.add(reading);
            if (temperatures.size() == 5) {
                break;
            }
        }

        System.out.print("Lowest temperatures: ");
        for (int temperature : temperatures) {
            System.out.print(temperature + "F ");
        }
        System.out.println();
    }

    static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
