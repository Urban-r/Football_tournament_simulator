public class Person {
    private String firstName;
    private String surname;
    private String team;
    Person(String firstName, String surname, String team){
        this.firstName = firstName;
        this.surname = surname;
        this.team = team;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSurname() {
        return surname;
    }
    public String getTeam() {
        return team;
    }

}

