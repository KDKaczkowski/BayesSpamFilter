
import java.util.*;

public class SpamFilter {
    private Map<String, Word>  map;

    private int hamMessages;
    private int spamMessages;



    public SpamFilter(){
        this.map = new TreeMap<>();
        hamMessages = 0;
        spamMessages = 0;
    }

    public void clear()
    {
        map.clear();
        hamMessages = 0;
        spamMessages = 0;

    }

    public boolean isSpam( String message)
    {
        return calculateSpamProbability(probability(spamMessages,hamMessages), message.split(" |\n|\n|\n")) > 0;
    }

    public boolean isHam( String message)
    {
        return calculateHamProbability(probability(hamMessages, spamMessages), message.split(" |\n|\n|\n")) > 0;
    }


    public void learn(String mail, boolean isSpam)
    {
        for (String wordString : filter(mail.split(" |\n|\n|\n")))
        {
            Word wordClass = new Word(wordString);

            if (!Objects.isNull(map.putIfAbsent(wordString, wordClass))) {
                wordClass = map.get(wordString);
            }
            if (isSpam) {
                wordClass.incrementSpam();
            } else {
                wordClass.incrementHam();
            }
        }
        if (isSpam)
        {


                ++spamMessages;
        } else
            {
                ++hamMessages;
        }


    }
    public void endLearning()
    {
        map.forEach((key,value)-> {
            value.setProbabilityOfHam(probabilityOfHamWord(value));
            value.setProbabilityOfSpam(probabilityOfSpamWord(value));
        });
    }


    public ArrayList<String> filter(String[] words)
    {

        ArrayList<String> resultWords = new ArrayList<String>();
        for (String word: words)
        {
            if(word.length()==1)
            {
                if(!Character.isLetter(word.charAt(0))) continue; //zostaja pojedyncze litery, usuwa wszystkie znaki specjalne
            }
            boolean hasNumber = false;
            for (char character: word.toCharArray() //usuwa wszytskie liczby
                 ) {
                if(!Character.isLetter(character))
                {
                    hasNumber = true;
                    break;
                }

            }
            if(hasNumber) continue;
            resultWords.add(word);
        }
        return resultWords;
    }

    public double probabilityOfSpamWord(Word word) {
        return probability(word.getSpam(), spamMessages);
    }

    public double probabilityOfHamWord(Word word) {
        return probability(word.getHam(), hamMessages);
    }


    private double calculateHamProbability(double initValue, String[] words) {

        double sum = 0;
        for (String word: words) {
            Word wordClass = map.get(word);
            if(wordClass == null) continue;
            sum += Math.log10(wordClass.getProbabilityOfHam() / wordClass.getProbabilityOfSpam());
        }
        sum+= Math.log10(initValue);
        return sum;

    }

    private double calculateSpamProbability(double initValue, String[] words) {


        double sum = 0;
        for (String word: words) {
            Word wordClass = map.get(word);
            if(wordClass == null) continue;
            sum += Math.log10(wordClass.getProbabilityOfSpam() / wordClass.getProbabilityOfHam());
        }
        sum+= Math.log10(initValue);
        return sum;

    }


    private double probability(double numerator, double denominator) {
        if(denominator == 0) return 0;
        if(numerator/denominator >1.0) return 1;
        return numerator/denominator;

    }






}
