public class Player extends Person{
    private String position;
    private double fitness;
    private double passingAccuracy;
    private double shotAccuracy;
    private double shotFrequency;
    private double defensiveness;
    private double aggression;
    private double positioning;
    private double dribbling;
    private double chanceCreation;
    private double offsideAdherence;

    Player(String firstName, String surname, String team, String position, double fitness, double passingAccuracy, double shotAccuracy, double shotFrequency, double defensiveness, double aggression, double positioning, double dribbling, double chanceCreation, double offsideAdherence){
        super(firstName, surname, team);
        this.position = position;
        this.fitness = fitness;
        this.passingAccuracy = passingAccuracy;
        this.shotAccuracy = shotAccuracy;
        this.shotFrequency = shotFrequency;
        this.defensiveness = defensiveness;
        this.aggression = aggression;
        this.positioning = positioning;
        this.dribbling = dribbling;
        this.chanceCreation = chanceCreation;
        this.offsideAdherence = offsideAdherence;
    }
    public String getPosition() {
        return position;
    }
    public double getFitness() {
        return fitness;
    }
    public double getPassingAccuracy() {
        return passingAccuracy;
    }
    public double getShotAccuracy() {
        return shotAccuracy;
    }
    public double getShotFrequency() {
        return shotFrequency;
    }
    public double getDefensiveness() {
        return defensiveness;
    }
    public double getAggression() {
        return aggression;
    }
    public double getPositioning() {
        return positioning;
    }
    public double getDribbling() {
        return dribbling;
    }
    public double getChanceCreation() {
        return chanceCreation;
    }
    public double getOffsideAdherence() {
        return offsideAdherence;
    }

}
