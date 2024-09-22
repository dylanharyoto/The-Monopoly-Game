package game;

import player.Player;
import square.Property;
import square.Square;

import java.util.*;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;


public class Gameboard {
    private ArrayList<Player> players;
    private Square[] squares;
    private int round;
    private int current_player_index;
    private Scanner scanner;
    private int go_square_position;
    public Gameboard() {
        players = new ArrayList<>();
        squares = new Square[20];
        round = 0;
        current_player_index = -1;
        scanner = new Scanner(System.in);
        go_square_position = -1;
    }

    public void start_game(){
        while(round < 100 && players.size() != 1){
            Player current_player = next_player();
            int start_posistion = current_player.getCurrentPosition();
            if (current_player.getInJailDuration() > 0){
                while(true){
                    try {
                        System.out.println("Are you willing to pay 150 fine to get out(1 or 2)?");
                        System.out.print("1. yes   2. no\nYour answer>>");
                        int choice = scanner.nextInt();
                        if(choice == 1){
                            current_player.decreaseMoney(150);
                            current_player.setInJailDuration(0);
                        }
                        break;
                    }
                    catch (InputMismatchException e){
                        System.out.println("The answer is not a valid number");
                    }
                    catch (NumberFormatException e){
                        System.out.println("The answer has to be a number");
                    }
                }
            }

            current_player.move();
            int end_position = current_player.getCurrentPosition();
            get_square_at(end_position).takeEffect(current_player);
            if(current_player.getMoney() < 0){
                System.out.println(current_player.getName() + " Out!");
                players.remove(current_player);
                current_player_index = -1;
            }
        }
        end_game();
    }
    public void end_game(){
        if(players.size() == 1)
            System.out.println("Game Finished! The winner is " + players.get(0).getName());
        else{
            System.out.print("Game Finished! The winners are ");
            for (int i = 0; i < players.size(); i++){
                System.out.print(players.get(i).getName());
                if(i != players.size() - 1)
                    System.out.print(", ");
            }
            System.out.println(".");
        }
    }
    public Square get_square_at(int position){
        if(position < 20 && position >= 0)
            return this.squares[position];
    }
    public Player next_player(){
        current_player_index = ++current_player_index%players.size();
        return players.get(current_player_index);

    }
    public void save_game(String filename){
        Map<String, Object> json_object = new HashMap<>();

        Map<String, Object> all_players_json_object = new HashMap<>();
        for(Player player : players){
            List<Integer> property_positions = new ArrayList<>();
            for(Property property : player.getProperties()){
                property_positions.add(property.getPosition());
            }
            Map<String, Object> player_json_object = new HashMap<>();
            player_json_object.put("name", player.getName());
            player_json_object.put("money", player.getMoney());
            player_json_object.put("inJailDuration", player.getInJailDuration());
            player_json_object.put("currentPosition", player.getCurrentPosition());
            player_json_object.put("property", property_positions);
            all_players_json_object.put(player.getName(), player_json_object);
        }
        Map<String, Object> all_squares_json_object = new HashMap<>();

        json_object.put("round", round);
        json_object.put("current_player_index", current_player_index);
        json_object.put("players", all_players_json_object);
        json_object.put("squares", all_squares_json_object);

        try (FileWriter fileWriter = new FileWriter(filename)) {
            String json = map_to_json(json_object);
            fileWriter.write(json);
            System.out.println("JSON data has been written to " + filename);
        } catch (IOException e) {
            System.out.println("An IO Error Occurred");
        }

    }
    public void load_game(String filename){
        StringBuilder json = new StringBuilder();
        String jsonString = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            jsonString = json.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String map_to_json(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof Map) {
                json.append(map_to_json((Map<String, Object>) entry.getValue()));
            } else {
                json.append("\"").append(entry.getValue()).append("\"");
            }
            json.append(",");
        }
        json.deleteCharAt(json.length() - 1); // Remove the trailing comma
        json.append("}");
        return json.toString();
    }
        
}
