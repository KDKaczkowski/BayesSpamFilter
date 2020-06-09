
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class CrossValid extends DataLoader {

    public static void main(String[] args) {
        System.out.println("średnia strata: " + 100*IntStream.range(0, 2).mapToDouble(e -> {
            try {
                return new CrossValid().run(5);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return 0;
        }).average().orElse(0) + "%");
    }

    public double run(final int k) throws IOException {
        File file = new File("/resources");
        List<String> ham = loadData(file, "/ham");
        List<String> spam = loadData(file, "/spam");
        Collections.shuffle(spam);
        Collections.shuffle(ham);
        List<Subset> subsets = divide(k, spam, ham);

        return calculate(subsets);
    }

    private List<Subset> divide(final int k, List<String> spam, List<String> ham) {
        List<Subset> list = new LinkedList<>();
        int hamIndex1, hamIndex2, spamIndex1, spamIndex2;
        for (int i = 0; i < k; ++i) {
            hamIndex1 = i * ham.size() / k;
            hamIndex2 = (i + 1) * ham.size() / k;
            spamIndex1 = i * spam.size() / k;
            spamIndex2 = (i + 1) * spam.size() / k;
            list.add(new Subset(ham.subList( hamIndex1 , hamIndex2),
                    spam.subList( spamIndex1 , spamIndex2)));
        }
        return list;
    }

    private double calculate(final List<Subset> list) {
        return list.stream().mapToDouble(i -> {
            final SpamFilter spamFilter = new SpamFilter();
            list.stream().filter(j -> i != j).forEach(j -> {
                j.ham.forEach(mail -> spamFilter.learn(mail, false));
                j.spam.forEach(mail -> spamFilter.learn(mail, true));
            });
            spamFilter.endLearning();
            final int counter = i.spam.stream().mapToInt(spam -> spamFilter.isSpam(spam) ? 0 : 1).sum() +
                    i.ham.stream().mapToInt(ham -> spamFilter.isSpam(ham) ? 1 : 0).sum();
            spamFilter.clear();
            return counter / (double) (i.ham.size() + i.spam.size());
        }).average().orElse(Double.POSITIVE_INFINITY);
    }

    private static class Subset {
        private List<String> ham;
        private List<String> spam;
        public Subset(List<String> subList, List<String> subList1) {
            this.ham = subList;
            this.spam = subList1;
        }
    }
}
