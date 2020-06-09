

public class Word {

    private String word;
    private int ham;
    private int spam;
    private double probabilityOfHam;
    private double probabilityOfSpam;

    public Word(String word)
    {
        this.word=word;
        ham=0;
        spam=0;
        probabilityOfHam=0.0;
        probabilityOfSpam=0.0;
    }
    public void incrementSpam(){ spam++;}
    public void incrementHam(){ham++;}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getHam() {
        return ham;
    }

    public void setHam(int ham) {
        this.ham = ham;
    }

    public int getSpam() {
        return spam;
    }

    public void setSpam(int spam) {
        this.spam = spam;
    }

    public double getProbabilityOfHam() {
        return probabilityOfHam;
    }

    public void setProbabilityOfHam(double probabilityOfHam) {
        this.probabilityOfHam = probabilityOfHam;
    }

    public double getProbabilityOfSpam() {
        return probabilityOfSpam;
    }

    public void setProbabilityOfSpam(double probabilityOfSpam) {
        this.probabilityOfSpam = probabilityOfSpam;
    }
}
