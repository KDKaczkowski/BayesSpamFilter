

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Example extends DataLoader {
    private final static String DATA_PATH = "/resources";
    static Scanner in = new Scanner(System.in);


    public static void main(String[] args) throws IOException {
        Example example = new Example();
        example.example(0.8);

    }


    public static int getInteger(){

        int val;
        while (true){
            try{
                val = in.nextInt();
            } catch(InputMismatchException e){
                in.next();
                System.out.println("Wprowadzono zle dane, spróbuj jeszcze raz.");
                continue;
            }
            return val;
        }
    }


    public void example(final double value) throws IOException {
        SpamFilter spamFilter = new SpamFilter();
        BigDecimal hamsSuccess = new BigDecimal(0);
        BigDecimal hamsFails = new BigDecimal(0);
        BigDecimal spamsSuccess = new BigDecimal(0);
        BigDecimal spamsFails = new BigDecimal(0);
        BigDecimal mulSpamSuccess = new BigDecimal(0);
        BigDecimal mulHamSuccess = new BigDecimal(0);
        int howManySpamFiles = 0;
        int howManyHamFiles = 0;
        BigDecimal hamsPercentage = new BigDecimal(0);
        BigDecimal spamsPercentage = new BigDecimal(0);
        hamsPercentage.setScale(2, RoundingMode.HALF_UP);
        spamsPercentage.setScale(2, RoundingMode.HALF_UP);
        System.out.println("Enter how many Spam files you want to use to learn algorithm");
        howManySpamFiles = getInteger();
        System.out.println("Enter how many Ham files you want to use to learn algorithm");
        howManyHamFiles = getInteger();
        File file = new File(DATA_PATH);
        List<String> hams = loadPartOfData(file, "/ham", howManyHamFiles);
        List<String> spams = loadPartOfData(file, "/spam", howManySpamFiles);
        System.out.println(" hams size:" + hams.size() + "  SPam size: " + spams.size());
        List<String> hamsToCheck = loadData(file, "/ham");
        List<String> spamsToCheck = loadData(file, "/spam");
        try {

            Collections.shuffle(hams);
            Collections.shuffle(spams);

            for (int i = 0; i < hams.size() * value; ++i) {
                spamFilter.learn(hams.get(i), false);
            }
            for (int i = 0; i < spams.size() * value; ++i) {
                spamFilter.learn(spams.get(i), true);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        spamFilter.endLearning();

        for (int i = (int) (hamsToCheck.size() * value); i < hamsToCheck.size(); ++i) {
            if (spamFilter.isSpam(hamsToCheck.get(i)))
                hamsFails = hamsFails.add( new BigDecimal(1) );
            else
                hamsSuccess = hamsSuccess.add( new BigDecimal(1) );
        }
        int c = 0;
        for (int i = (int) (spamsToCheck.size() * value); i < spamsToCheck.size(); ++i, ++c) {
            if (!spamFilter.isSpam(spamsToCheck.get(i)))
                spamsFails = spamsFails.add( new BigDecimal(1) );
            else
                spamsSuccess = spamsSuccess.add( new BigDecimal(1) );
        }


        System.out.println("HAMS: Succes: " + hamsSuccess + " Fails: " + hamsFails);
        System.out.println("Spams: Succes: " + spamsSuccess + " Fails: " + spamsFails);
        mulHamSuccess = hamsSuccess.multiply( new BigDecimal(100));
        mulSpamSuccess = spamsSuccess.multiply(new BigDecimal(100));
        hamsPercentage = mulHamSuccess.divide(hamsSuccess.add(hamsFails), 2, RoundingMode.HALF_UP);
        spamsPercentage = mulSpamSuccess.divide(spamsSuccess.add(spamsFails), 2, RoundingMode.HALF_UP);

        System.out.println("Hams succes Percentage: " + hamsPercentage+ " %");
        System.out.println("Spams succes Percentage: " + spamsPercentage+ " %");

    }
}

