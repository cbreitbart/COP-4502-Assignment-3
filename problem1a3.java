import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class problem1a3 {
    private static final int THREAD_COUNT = 4;
    private static final int NUM_GUESTS = 500000;

    private static final int ADD_TO_CHAIN = 0;
    private static final int WRITE_CARD = 1;
    private static final int SEARCH_FOR_PRESENT = 2;

    private ConcurrentLinkedList list = new ConcurrentLinkedList();
    private Set<Integer> cards = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Generates a shuffled set containing numbers from 0 to size - 1
    private Set<Integer> generateShuffledSet(int size) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
        // Shuffling the set
        Integer[] array = set.toArray(new Integer[0]);
        Collections.shuffle(Arrays.asList(array));
        return new HashSet<>(Arrays.asList(array));
    }

    private void completeTask(Set<Integer> giftBag) {
        while (cards.size() < NUM_GUESTS) {
            int task = ThreadLocalRandom.current().nextInt(ADD_TO_CHAIN, SEARCH_FOR_PRESENT + 1);

            switch (task) {
                case ADD_TO_CHAIN:
                    synchronized (giftBag) {
                        if (giftBag.isEmpty()) {
                            continue;
                        }
                        int num = giftBag.iterator().next();
                        giftBag.remove(num);
                        list.insert(num);
                    }
                    break;
                case WRITE_CARD:
                    if (!list.empty()) {
                        int guest = list.removeHead();
                        if (guest != Integer.MIN_VALUE) {
                            synchronized (cards) {
                                cards.add(guest);
                            }
                        }
                    }
                    break;
                case SEARCH_FOR_PRESENT:
                    int randomGuest = ThreadLocalRandom.current().nextInt(NUM_GUESTS);
                    boolean found = list.contains(randomGuest);
                    break;
            }
        }
    }

    public void startPreparation() {
        System.out.println("Generating " + NUM_GUESTS + " presents:");
        Set<Integer> giftBag = generateShuffledSet(NUM_GUESTS);
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> completeTask(giftBag));
            threads[i].start();
        }

        System.out.println("Utilizing " + THREAD_COUNT + " threads:");
        long start = System.currentTimeMillis();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Finished in " + (end - start)/1000 + " seconds");
    }

    public static void main(String[] args) {
        new problem1a3().startPreparation();
    }
}
