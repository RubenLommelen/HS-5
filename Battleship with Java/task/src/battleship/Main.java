package battleship;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            char[][] boardA = new char[10][10];
            char[][] fogBoardA = new char[10][10];
            fill2DArray(boardA, '~');
            fill2DArray(fogBoardA, '~');

            char[][] boardB = new char[10][10];
            char[][] fogBoardB = new char[10][10];
            fill2DArray(boardB, '~');
            fill2DArray(fogBoardB, '~');

            List<Integer> shipsLength = new ArrayList<>();
            shipsLength.add(5);
            shipsLength.add(4);
            shipsLength.add(3);
            shipsLength.add(3);
            shipsLength.add(2);

            List<String> shipsNames = new ArrayList<>();
            shipsNames.add("Aircraft Carrier");
            shipsNames.add("Battleship");
            shipsNames.add("Submarine");
            shipsNames.add("Cruiser");
            shipsNames.add("Destroyer");

            List<Ship> shipsA = new ArrayList<>();
            List<Ship> shipsB = new ArrayList<>();


            System.out.println("Player 1, place your ships on the game field");
            printBoard(boardA);
            setupBoard(shipsLength, shipsNames, sc, boardA, shipsA);

            nextPlayerText(sc);

            System.out.println("Player 2, place your ships to the game field");
            printBoard(boardB);
            setupBoard(shipsLength, shipsNames, sc, boardB, shipsB);

            nextPlayerText(sc);

            int turn = 0;
            boolean playing = true;

            while (playing) {

                if (turn % 2 == 0) {
                    printBoard(fogBoardB);
                    System.out.println("---------------------");
                    printBoard(boardA);
                    System.out.println("Player 1, it's your turn:");
                } else {
                    printBoard(fogBoardA);
                    System.out.println("---------------------");
                    printBoard(boardB);
                    System.out.println("Player 2, it's your turn:");
                }

                String shotInput = sc.nextLine();
                boolean hit = false;
                boolean sunk = false;

                try {
                    validateCoordinate(shotInput);
                } catch (Exception e) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    continue;
                }

                Location shotLocation = new Location(shotInput);

                // switch between player boards
                List<Ship> enemyShips;
                char[][] enemyBoard;
                char[][] enemyFogBoard;

                if (turn % 2 == 0) {
                    enemyShips = shipsB;
                    enemyFogBoard = fogBoardB;
                    enemyBoard = boardB;
                } else {
                    enemyShips = shipsA;
                    enemyFogBoard = fogBoardA;
                    enemyBoard = boardA;
                }

                for (Ship ship : enemyShips) {
                    for (Location location : ship.getLocations()) {
                        if (shotLocation.equals(location)) {
                            hit = true;
                            ship.hit();

                            if (ship.isSunk()) {
                                sunk = true;
                            }

                            break;
                        }
                    }
                    if (hit) break;
                }

                if (hit) {
                    enemyFogBoard[shotLocation.getX()][shotLocation.getY()] = 'X';
                    enemyBoard[shotLocation.getX()][shotLocation.getY()] = 'X';

                    printBoard(fogBoardA);

                    if (enemyShips.stream()
                            .filter(ship -> !ship.isSunk())
                            .toList().isEmpty()) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                        playing = false;
                    } else if (sunk) {
                        System.out.println("You sank a ship!");
                        nextPlayerText(sc);
                    } else {
                        System.out.println("You hit a ship!");
                        nextPlayerText(sc);
                    }
                } else {
                    enemyFogBoard[shotLocation.getX()][shotLocation.getY()] = 'M';
                    enemyBoard[shotLocation.getX()][shotLocation.getY()] = 'M';

                    System.out.println("You missed!");
                    nextPlayerText(sc);
                }

                turn++; // switch between players
            }

        } catch (Exception ex) {
            System.out.println("Error!");
        }
    }

    private static void nextPlayerText(Scanner sc) {
        System.out.println("Press Enter and pass the move to another player\n...");
        sc.nextLine();
    }

    private static void setupBoard(List<Integer> shipsLength, List<String> shipsNames, Scanner sc, char[][] board, List<Ship> ships) {
        for (int i = 0; i < shipsLength.size(); i++) {
            boolean correctInput = false;
            String[] coordinates;
            Location start, end;
            Ship ship;

            System.out.println("Enter the coordinates of the " + shipsNames.get(i) + " (" + shipsLength.get(i) + " cells):");

            while (!correctInput) {
                String answer = sc.nextLine();

                coordinates = answer.split(" ");

                if (coordinates.length > 2) {
                    throw new IllegalArgumentException();
                }

                validateCoordinate(coordinates[0]);
                validateCoordinate(coordinates[1]);

                start = new Location(coordinates[0]);
                end = new Location(coordinates[1]);

                try {
                    if (!start.hasDirectLine(end)) {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error! Wrong ship location! Try again:");
                    continue;
                }

                try {
                    ship = new Ship(start, end);

                    if (ship.getLength() != shipsLength.get(i)) {
                        throw new IllegalArgumentException();
                    }

                } catch (Exception ex) {
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                    continue;
                }


                try {

                    for (Location location : ship.getLocations()) {

                        for (int y = -1; y <= 1; y++) {
                            for (int x = -1; x <= 1; x++) {

                                if ((location.getX() + x >= 1 && location.getY() + y >= 1)
                                        && (location.getX() + x < 10 && location.getY() + y < 10)) {

                                    if (board[location.getX() + x][location.getY() + y] == 'O') {
                                        throw new IllegalArgumentException();
                                    }
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    continue;
                }

                correctInput = true;
                ships.add(ship);
                placeShip(ship, board);
                printBoard(board);
            }
        }
    }

    private static void placeShip(Ship ship, char[][] board) {
        for (Location location : ship.getLocations()) {
            board[location.getX()][location.getY()] = 'O';
        }
    }

    private static void validateCoordinate(String coordinate) {

        String row = coordinate.substring(0, 1);
        String column = coordinate.substring(1);

        char columnChar = row.toCharArray()[0];

        if ((columnChar - 'A') < 0 || (columnChar - 'A') >= 10) {
            throw new IllegalArgumentException();
        }

        if (Integer.parseInt(column) < 1 || Integer.parseInt(column) > 10) {
            throw new IllegalArgumentException();
        }
    }

    private static void printBoard(char[][] board) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");

        for (int y = 0; y < board.length; y++) {

            for (int x = 0; x < board[y].length; x++) {

                if (x == 0) {
                    char row = (char) (y + 'A');
                    System.out.print(row + " ");
                }
                System.out.print(board[x][y]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private static void fill2DArray(char[][] array, char value) {
        for (char[] chars : array) {
            Arrays.fill(chars, value);
        }
    }
}
