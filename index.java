import java.util.*;

public class RecommendationSystem {

    // User-item ratings
    static Map<String, Map<String, Integer>> userRatings = new HashMap<>();

    public static void main(String[] args) {
        // Sample data
        Map<String, Integer> aliceRatings = new HashMap<>();
        aliceRatings.put("Item1", 5);
        aliceRatings.put("Item2", 3);
        aliceRatings.put("Item3", 2);
        userRatings.put("Alice", aliceRatings);

        Map<String, Integer> bobRatings = new HashMap<>();
        bobRatings.put("Item1", 4);
        bobRatings.put("Item3", 5);
        bobRatings.put("Item4", 1);
        userRatings.put("Bob", bobRatings);

        Map<String, Integer> charlieRatings = new HashMap<>();
        charlieRatings.put("Item2", 5);
        charlieRatings.put("Item3", 3);
        charlieRatings.put("Item4", 4);
        userRatings.put("Charlie", charlieRatings);

        String targetUser = "Alice";
        List<String> recommendations = recommendItems(targetUser, 2);
        System.out.println("Recommendations for " + targetUser + ": " + recommendations);
    }

    // Recommend items using collaborative filtering
    public static List<String> recommendItems(String user, int topN) {
        Map<String, Double> scores = new HashMap<>();
        Map<String, Integer> targetRatings = userRatings.get(user);

        for (String otherUser : userRatings.keySet()) {
            if (otherUser.equals(user)) continue;
            Map<String, Integer> otherRatings = userRatings.get(otherUser);

            double similarity = cosineSimilarity(targetRatings, otherRatings);

            for (String item : otherRatings.keySet()) {
                if (!targetRatings.containsKey(item)) {
                    scores.put(item, scores.getOrDefault(item, 0.0) + similarity * otherRatings.get(item));
                }
            }
        }

        // Sort items by score descending
        List<String> recommendedItems = new ArrayList<>(scores.keySet());
        recommendedItems.sort((i1, i2) -> Double.compare(scores.get(i2), scores.get(i1)));

        return recommendedItems.subList(0, Math.min(topN, recommendedItems.size()));
    }

    // Cosine similarity between two users
    public static double cosineSimilarity(Map<String, Integer> ratings1, Map<String, Integer> ratings2) {
        Set<String> commonItems = new HashSet<>(ratings1.keySet());
        commonItems.retainAll(ratings2.keySet());

        if (commonItems.isEmpty()) return 0.0;

        double dot = 0, norm1 = 0, norm2 = 0;
        for (String item : commonItems) {
            dot += ratings1.get(item) * ratings2.get(item);
        }

        for (int rating : ratings1.values()) norm1 += rating * rating;
        for (int rating : ratings2.values()) norm2 += rating * rating;

        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}

