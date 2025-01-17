public class Manager extends Person{
    private String favouredFormation;
    private double respect;
    private double ability;
    private double knowledge;
    private double belief;

    Manager(String firstName, String surname, String team, String favouredFormation, double respect, double ability, double knowledge, double belief){
        super(firstName, surname, team);
        this.favouredFormation = favouredFormation;
        this.respect = respect;
        this.ability = ability;
        this.knowledge = knowledge;
        this.belief = belief;
    }
    public String getFavouredFormation() {
        return favouredFormation;
    }
    public double getRespect() {
        return respect;
    }
    public double getAbility() {
        return ability;
    }
    public double getKnowledge() {
        return knowledge;
    }
    public double getBelief() {
        return belief;
    }

}
