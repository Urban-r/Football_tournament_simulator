import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static Squad[] squads = new Squad[32];

    public static void main(String[] args){
        ArrayList<Manager> managers = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();

        // Load Managers
        try (Scanner scanner = new Scanner(new File("Managers.csv"))) {
            scanner.nextLine(); // Skip header row
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split("\\,");
                Manager manager = new Manager(data[0], data[1], data[2], data[3],
                        Double.parseDouble(data[4]), Double.parseDouble(data[5]),
                        Double.parseDouble(data[6]), Double.parseDouble(data[7]));
                managers.add(manager);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Load Players
        try (Scanner scanner = new Scanner(new File("Players.csv"))) {
            scanner.nextLine(); // Skip header row
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split("\\,");
                Player player = new Player(data[0], data[1], data[2], data[3],
                        Double.parseDouble(data[4]), Double.parseDouble(data[5]),
                        Double.parseDouble(data[6]), Double.parseDouble(data[7]),
                        Double.parseDouble(data[8]), Double.parseDouble(data[9]),
                        Double.parseDouble(data[10]), Double.parseDouble(data[11]),
                        Double.parseDouble(data[12]), Double.parseDouble(data[13]));
                players.add(player);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Create Squads
        for (Manager manager : managers) {
            // Create a new Squad with the manager
            Squad squad = new Squad(manager.getTeam(), manager);

            // Add players to the squad
            for (Player player : players) {
                if (player.getTeam().equals(manager.getTeam())) {
                    squad.addPlayer(player);
                }
            }
            // Add the squad to the squads array
            squads[managers.indexOf(manager)] = squad;
        }
        runTournament();
    }

    public static Team getTeam(Squad s){
        // Create a new team
        Team t = new Team(s.getTeamName(), s.getManager());
        String formation = s.getManager().getFavouredFormation();
        String[] positions = formation.split("-");

        int numDefenders = Integer.parseInt(positions[0]);
        int numMidfielders = Integer.parseInt(positions[1]);
        int numForwards = Integer.parseInt(positions[2]);

        // Create a list of all players in the squad
        List<Player> allPlayers = new ArrayList<>();
        int squadSize = s.getNumPlayers();
        for (int i = 0; i < squadSize; i++) {
            allPlayers.add(s.getPlayer(i));
        }

        // Comparator to sort players based on average attributes
        Comparator<Player> playerComparator = Comparator.comparingDouble(Main::calculateAverageAttributes).reversed();

        // Selecting players based on the formation
        List<Player> goalkeepers = allPlayers.stream()
                .filter(p -> p.getPosition().equalsIgnoreCase("Goal Keeper"))
                .sorted(playerComparator)
                .limit(1)
                .collect(Collectors.toList());

        List<Player> defenders = allPlayers.stream()
                .filter(p -> p.getPosition().equalsIgnoreCase("Defender"))
                .sorted(playerComparator)
                .limit(numDefenders)
                .collect(Collectors.toList());

        List<Player> midfielders = allPlayers.stream()
                .filter(p -> p.getPosition().equalsIgnoreCase("Midfielder"))
                .sorted(playerComparator)
                .limit(numMidfielders)
                .collect(Collectors.toList());

        List<Player> forwards = allPlayers.stream()
                .filter(p -> p.getPosition().equalsIgnoreCase("Forward"))
                .sorted(playerComparator)
                .limit(numForwards)
                .collect(Collectors.toList());

        // Adding players to the team
        goalkeepers.forEach(t::addPlayer);
        defenders.forEach(t::addPlayer);
        midfielders.forEach(t::addPlayer);
        forwards.forEach(t::addPlayer);

        return t;

    }
    private static double calculateAverageAttributes(Player player) {
        // Calculate average of all attributes
        double sum = player.getFitness() + player.getPassingAccuracy() + player.getShotAccuracy() +
                player.getShotFrequency() + player.getDefensiveness() + player.getAggression() +
                player.getPositioning() + player.getDribbling() + player.getChanceCreation() +
                player.getOffsideAdherence();

        return sum / 10; // Since there are 10 attributes
    }

        public static void runTournament () {
            // Group stage
            List<Team> groupWinners = new ArrayList<>();
            for (int i = 0; i < squads.length; i += 4) {
                // Simulate matches in each group and determine top two teams
                List<Team> groupTeams = Arrays.asList(
                        getTeam(squads[i]), getTeam(squads[i+1]), getTeam(squads[i+2]), getTeam(squads[i+3])
                );

                List<Team> topTeams = simulateGroupStage(groupTeams);
                groupWinners.addAll(topTeams);
                System.out.println("Group " + (i/4 + 1) + " winners: " + topTeams.get(0).getTeamName() + ", " + topTeams.get(1).getTeamName());
            }

            // Knockout stage
            while (groupWinners.size() > 1) {
                List<Team> nextRoundTeams = new ArrayList<>();
                for (int i = 0; i < groupWinners.size(); i += 2) {
                    // Simulate matches in each round and determine winners
                    Team winner = simulateMatch(groupWinners.get(i), groupWinners.get(i+1));
                    nextRoundTeams.add(winner);
                    System.out.println("Winner of " + groupWinners.get(i).getTeamName() + " vs " + groupWinners.get(i+1).getTeamName() + ": " + winner.getTeamName());
                }
                groupWinners = nextRoundTeams;
            }

            // Final winner
            Team winner = groupWinners.get(0);
            System.out.println("Tournament Winner: " + winner.getTeamName());
        }

        private static List<Team> simulateGroupStage(List<Team> teams) {
        // Simulate matches between teams in a group
        Map<Team, Integer> pointsTable = new HashMap<>();

        for (Team team : teams) {
            pointsTable.put(team, 0);
        }

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team winner = simulateMatch(teams.get(i), teams.get(j));
                pointsTable.put(winner, pointsTable.getOrDefault(winner, 0) + 3);  // 3 points for a win
            }
        }

        // Sort teams based on points
        return pointsTable.entrySet().stream()
                .sorted(Map.Entry.<Team, Integer>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static Team simulateMatch(Team team1, Team team2) {
        // Simulate a match between two teams
        double team1Strength = calculateTeamStrength(team1);
        double team2Strength = calculateTeamStrength(team2);

        if (team1Strength > team2Strength) {
            return team1;
        } else if (team2Strength > team1Strength) {
            return team2;
        } else {
            return Math.random() > 0.5 ? team1 : team2;  // Random winner in case of a tie
        }
    }

    private static double calculateTeamStrength(Team team) {
        // Calculate team strength based on player attributes
        // Example: Average of all player attribute averages
        List<Player> players = new ArrayList<>();
        int squadSize = team.getNumPlayers();
        for (int i = 0; i < squadSize; i++) { // Add all players to a list
            players.add(team.getPlayer(i));
        }

        return players.stream()
                .mapToDouble(Main::calculateAverageAttributes)
                .average()
                .orElse(0.0);
    }
}
