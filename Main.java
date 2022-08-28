import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws NumberFormatException, ParseException, NoSuchFieldException, SecurityException {
        // reading the file
        LinkedHashMap<Integer, Movie> movieList = FileOperations.turnIntoMap("mymoviedb.csv");

        // in most applications we will not need in which row the movie is located; therefore, for simplicity we created list of movies
        List<Movie> movies = movieList.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

        // declaring some variables
        int input, cnt;
        String answer = ""; // for yes-no questions
        Field[] allFields = Movie.class.getDeclaredFields(); 
        Scanner sc = new Scanner(System.in);

        Screen.StartScreen(); // some visuals

        // we create a loop in case the user wants to do the process over and over again
        while(true) {
            String dest = ""; // this is for initializing the location of destination file in each iteration
            Screen.MainMenu();

            System.out.println("-> Please enter a choice:");
            System.out.println("\n\t1. List");
            System.out.println("\t2. Sort");
            System.out.println("\t3. Search");
            System.out.println("\t4. List column names");
            System.out.println("\t5. Filter");
            System.out.println("\t6. Exit");
            System.out.print("\nYour choice [1-6]: ");

            cnt = 0;

            // in case user enters invalid input
            while(true) { 
                if(sc.hasNextInt()) { 
                    input = sc.nextInt(); // user chooses one of the options
                } else { // if the input is not integer
                    while(true) {
                        if (sc.hasNextInt()) {
                            input = sc.nextInt();
                            break;
                        }

                        System.out.println("\n-> Wrong Entry! Only numbers in range [1-6]! Try again: "); //giving chances to the user

                        sc.next();
                    }
                }
    
                if(input < 1 || input > 6) {
                    System.out.println("\n-> Wrong Entry! Number must be in range [1-6]! Try again: "); // giving chances to the user
                } else break;

            }
            
            
            System.out.print("\033[H\033[2J"); // for clarity of console, new screen is opened
            switch(input) {
                case 1:
                    Screen.ListMenu();
                    System.out.println("a. All the fields");
                    System.out.println("b. Selected fields");
                    System.out.println("c. Based on the range of rows");
                    System.out.println("d. Go to Main Menu");

                    System.out.print("\n-> Please enter a choice [a-d]: ");

                    String choiceForList = sc.next();
                    char choice = choiceForList.charAt(0); 
                    int ascIIchoice = choice;

                    while(ascIIchoice < 97 || ascIIchoice > 100 || choiceForList.length() > 1){ // if the user does not enter input from a-d interval
                        System.out.print("\n-> Wrong Entry! Only letters from a to d! Try again: ");

                        choiceForList = sc.next();
                        choice = choiceForList.charAt(0);
                        ascIIchoice = choice;
                    }
                    
                    switch(choice) {
                        case 'a': 
                            System.out.println("\n-> Listing all the fields...");

                            LinkedHashMap<Field, List<Object>> res = FileOperations.listFields(movies, allFields); // Map is used in order to keep track of rows

                            Set<Field> keys = res.keySet();
                            Iterator<Field> it = keys.iterator();
                            
                            while (it.hasNext()) { 
                                Field key = it.next();
                                List<Object> value = res.get(key);
                                
                                System.out.println("\n-> The list of the field \"" + key.getName() + "\":");
                                System.out.println("\n\t" + value.toString());
                                System.out.println("\n" + value.size() + " entities were listed!" + "\n");
                            }

                            break;
                        case 'b':
                            while(true) {
                                System.out.print("\033[H\033[2J"); // for each case screen will be cleared
                                List<Field> selectedFields = new LinkedList<>(); // creating a linked list (for keeping order) of selected fields
                                String field = "field";
    
                                while(true) {
                                    System.out.println("-> Please enter one field that will be listed:");
                                    // showing the user available options
                                    System.out.println("\n\t> Release_Date\n\t> Title\n\t> Overview\n\t> Popularity\n\t> Vote_Count\n\t> Vote_Average\n\t> Original_Language\n\t> Genre\n\t> Poster_Url\n");
                                    sc.nextLine();
                                    System.out.print("Your choice: ");
                                    field = sc.nextLine(); 

                                    while(field.contains(" ")) {
                                        System.out.println("\n-> Only one field should be selected! Try again: ");
                                        field = sc.nextLine();
                                    }
    
                                    for (Field f : allFields) 
                                        if(f.getName().equals(field)) selectedFields.add(f); // adding to the list of selected fields
    
                                    while(true) {
                                        System.out.println("\n-> Do you want to add any other field?");
                                        System.out.print("\nYour choice [yes/no]: "); // output options are given in the brackets
                                        answer = sc.next();

                                        if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                        else break;
                                    }
    
                                    if(answer.equals("no")) break;  
                                    else System.out.println();
                                }
    
                                // turning the list to array (some problems occured due to size, that is why we could not create array from the beginning)
                                Field[] selectedFieldsArray = new Field[selectedFields.size()];
    
                                for(int i = 0; i < selectedFields.size(); i++)                   
                                    selectedFieldsArray[i] = selectedFields.get(i);
                                
                                System.out.println("\n-> Listing selected fields...");
                                res = FileOperations.listFields(movies, selectedFieldsArray);
    
                                keys = res.keySet();
                                it = keys.iterator();
                                
                                while (it.hasNext()) { 
                                    Field key = it.next();
                                    List<Object> value = res.get(key);
                                    
                                    System.out.println("\n-> The list of the field \"" + key.getName() + "\":");
                                    System.out.println("\n\t" + value.toString());
                                    System.out.println("\n" + value.size() + " entities were listed!" + "\n");
                                }
    
                                if(res.size() == 0) {
                                    System.out.print("\n-> Unfortunately, there is no printed entity. ");

                                    while(true) {
                                        // in case due to wrong name or other situations we give another chance
                                        if (res.size() == 0) System.out.println("Would you want to try again? [yes/no] ");
                                        answer = sc.next();

                                        if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                        else break;
                                    }

                                    if(answer.equals("no")) break;
                                } else break;
                            }

                            break;
                        case 'c': 
                            System.out.print("\033[H\033[2J");

                            System.out.println("-> Please enter the range: ");
                            System.out.print("\t[from - to]: ");
                            int min = 0, max = 0;

                            while(true) {
                                if(sc.hasNextInt()) {
                                    min = sc.nextInt();
                                    max = sc.nextInt();
                                } else {
                                    System.out.print("\n-> Please enter an integer: ");
                                    sc.next();
                                }

                                if(min < 2 || max < 2 || min > 9828 || max > 9828) { 
                                    System.out.print("\n[" + min + "," + max + "] interval does not correspond to database. Please enter from [2, 9828] interval: ");
                                } else break;
                            }

                            Pair p = new Pair(min, max);
                            
                            LinkedHashMap<Integer, Movie> listedEntities = FileOperations.listWithRange(p, movieList);

                            Set<Integer> rows = listedEntities.keySet();
                            Iterator<Integer> i = rows.iterator();
                            
                            while (i.hasNext()) { 
                                Integer row = i.next();
                                Movie value = listedEntities.get(row);
                                
                                System.out.println("\n" + value.toString());
                            }

                            System.out.println("\n" + listedEntities.size() + " entities were listed!");
                            break;
                        case 'd':
                            cnt++; // for directly starting the loop again without some statements below
                            break;
                    }
                    break;
                case 2:
                    String f;
                    List<Movie> res;
                    Iterator<Movie> itr;

                    while(true) {
                        Screen.SortMenu();

                        System.out.println("-> Please enter the field that will be sorted or type 'exit' to go to Main Menu: ");
                        System.out.println("\n\t> Release_Date\n\t> Title\n\t> Overview\n\t> Popularity\n\t> Vote_Count\n\t> Vote_Average\n\t> Original_Language\n\t> Genre\n\t> Poster_Url\n");
                        System.out.print("Your choice: ");

                        f = sc.next();

                        if(f.equals("exit")){
                            cnt++;
                            break;
                        }

                        System.out.print("\n-> Please enter the order [ASC/DESC]: ");
                        String order = sc.next();

                        while(! (order.equals("ASC") || order.equals("DESC"))) {
                            System.out.println("\n-> \"" + order + "\" is not valid order. Please enter either \"ASC\" or \"DESC\"");
                            order = sc.next();
                        }

                        System.out.print("\033[H\033[2J");
                        System.out.println("\n-> Sorting the list according to \"" + f + "\" by " + order + " order...");

                        res = FileOperations.sortByField(movies, f, order);

                        if (res.size() == 0) {
                            System.out.print("\n-> Unfortunately, there is no printed entity. ");

                            while(true) {
                                // in case due to wrong name or other situations we give another chance
                                if (res.size() == 0) System.out.println("Would you want to try again? [yes/no] ");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("no")) {
                                cnt++;
                                break;
                            } 
                        } else { 
                            movies = res; // for not losing this result if other iterations will done 

                            if(movies.size() != 0) System.out.println("-> Your list is ready!\n");
    
                            itr = movies.iterator();
    
                            while(itr.hasNext()) {
                                Movie m = itr.next();
    
                                System.out.println(m + "\n");
                            }
                            System.out.println(movies.size() + " entities were listed!");
                            break;
                        }

                    }

                    if(cnt != 0) break;

                    while(true) {
                        dest = "";

                        if (movies.size() != 0) {
                            while(true) {
                                System.out.print("\n-> Do you want to put the export file in the current folder? [yes/no] ");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("no")) {
                                System.out.println("\n-> Please enter the location of folder that you want to put the file: "); 
                                
                                sc.nextLine();
                                dest = sc.nextLine();
                            } else sc.nextLine();

                            System.out.println("\n-> Please enter the name for \".csv\" file [name]: ");
                            String fileName = sc.nextLine();

                            dest += fileName + ".csv";

                            while(true) {
                                System.out.println("\n-> Are you sure you want to export your file to " + dest + (answer.equals("yes") ? " in current folder" : "") + "? [yes/no]");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("yes")) break;
                        }
                    }

                    FileOperations.writeToCSV(movies, dest);

                    System.out.println("\n-> Finished! Your file is located in the " + dest);

                    break;
                case 3:
                    Object inputt;

                    while(true) {
                        Screen.SearchMenu();
                        System.out.println("-> Please enter the field that will be searched or type 'exit' to go to Main Menu: ");
                        System.out.println("\n\t> Release_Date\n\t> Title\n\t> Overview\n\t> Popularity\n\t> Vote_Count\n\t> Vote_Average\n\t> Original_Language\n\t> Genre\n\t> Poster_Url\n");
                        System.out.print("Your choice: ");

                        f = sc.next();

                        if(f.equals("exit")){
                            cnt++;
                            break;
                        }

                        if(f.equals("Genre")) { 
                            System.out.println("\n-> Here is the complete list of all genres:\n");
                            System.out.println( "\t> Drama\n\t> Action\n\t> Comedy\n\t> History\n\t> War\n\t> Science Fiction\n\t> Horror\n\t> Thriller\n\t> Music\n\t> Mystery\n\t> Crime\n\t> Family\n\t> Adventure\n\t> Fantasy\n\t> Romance\n\t> Animation\n\t> TV Movie\n\t> Documentary");;
                        }
                        
                        System.out.print("\n-> Search " + f + ": ");
                        sc.nextLine();
                        inputt = sc.nextLine();

                        System.out.println("\n-> Searching field name...");

                        res = FileOperations.searchByField(movies, f, inputt);

                        if (res.size() == 0) {
                            System.out.print("\n-> Unfortunately, there is no printed entity. ");

                            while(true) {
                                // in case due to wrong name or other situations we give another chance
                                if (res.size() == 0) System.out.println("Would you want to try again? [yes/no] ");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("no")) {
                                cnt++;
                                break;
                            } 
                        } else {
                            movies = res; // for not losing this result if other iterations will done

                            itr = movies.iterator();
    
                            while(itr.hasNext()) {
                                Movie m = itr.next();
    
                                System.out.println("\n" + m);
                            }
                            System.out.println(movies.size() + " entities were listed!");
                            break;
                        } 

                    }

                    if(cnt != 0) break;
                    
                    while(true) {
                        dest = "";

                        if (movies.size() != 0) {
                            while(true) {
                                System.out.print("\n-> Do you want to put the export file in the current folder? [yes/no] ");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("no")) {
                                System.out.println("\n-> Please enter the location of folder that you want to put the file: "); 
                                
                                sc.nextLine();
                                dest = sc.nextLine();
                            } else sc.nextLine();

                            System.out.println("\n-> Please enter the name for \".csv\" file [name]: ");
                            String fileName = sc.nextLine();

                            dest += fileName + ".csv";

                            while(true) {
                                System.out.println("\n-> Are you sure you want to export your file to " + dest + (answer.equals("yes") ? " in current folder" : "") + "? [yes/no]");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("yes")) break;
                        }
                    }

                    FileOperations.writeToCSV(movies, dest);

                    System.out.println("\n-> Finished! Your file is located in the " + dest);

                    break;
                case 4:
                    Screen.ColumnName();
                    System.out.println("-> Listing column names...\n");

                    for(int i = 1; i < allFields.length; i++) {
                        System.out.println("\t> " + allFields[i].getName());
                    }

                    break;
                case 5:
                    while(true) {
                        Screen.FilterMenu();
                        System.out.println("-> Please enter the field that will be filtered or type 'exit' to go to Main Menu: ");
                        System.out.println("\n\t> Release_Date\n\t> Title\n\t> Overview\n\t> Popularity\n\t> Vote_Count\n\t> Vote_Average\n\t> Original_Language\n\t> Genre\n\t> Poster_Url\n");
                        System.out.print("Your choice: ");
                        sc.nextLine();
                        f = sc.nextLine();

                        if(f.equals("exit")){
                            cnt++;
                            break;
                        }

                        if(f.equals("Genre")) {
                            System.out.println("\n-> Here is the complete list of genres:");
                            System.out.println("\t> Drama\n\t> Action\n\t> Comedy\n\t> History\n\t> War\n\t> Science Fiction\n\t> Horror\n\t> Thriller\n\t> Music" +
                                                "\n\t> Mystery\n\t> Crime\n\t> Family\n\t> Adventure\n\t> Fantasy\n\t> Romance\n\t> Animation\n\t> TV Movie\n\t> Documentary\n");
                        }
                        
                        System.out.println("\n-> According to which rule do you want to filter? ");

                        for(Field field : allFields) {
                            if(field.getName().equals(f)) {
                                if(field.getType().toString().contains("String")) {
                                    System.out.println("\n\t> startsWith" + "\n\t> endsWith" + "\n\t> contains" + "\n\t> null");
                                    if(f.equals("Genre")) System.out.println("\t> in any given genre(s)");
                                } else {
                                    System.out.println("\n\t> equal" + "\n\t> greater than" + "\n\t> less than" + "\n\t> greater and equal to" +
                                                        "\n\t> less and equal to" + "\n\t> between" + "\n\t> null");
                                    if(f.equals("Release_Date")) System.out.println("\t> in a specific year" + "\n\t> in a specific month" +
                                                                                                "\n\t> in a specific day");
                                }
                            } 
                        }

                        System.out.print("\nYour choice: ");
                        String rule = sc.nextLine();

                        System.out.print("\n-> Now enter the value: ");
                        inputt = sc.nextLine();

                        System.out.println("\nFiltering \"" + f + "\"s...");

                        res = FileOperations.filterFields(movies, f, rule, inputt);

                        if (res.size() == 0) {
                            System.out.print("\n-> Unfortunately, there is no printed entity. ");

                            while(true) {
                                // in case due to wrong name or other situations we give another chance
                                if (res.size() == 0) System.out.println("Would you want to try again? [yes/no] ");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(");
                                else break;
                            }

                            if(answer.equals("no")) {
                                cnt++;
                                break;
                            } 
                        } else {
                            movies = res; // for not losing this result if other iterations will done

                            itr = movies.iterator();
    
                            while(itr.hasNext()) {
                                Movie m = itr.next();
    
                                System.out.println(m + "\n");
                            }
                            System.out.println(movies.size() + " entities were listed!");
                            break;
                        }
                         
                    }

                    if(cnt != 0) break;
                    
                    while(true) {
                        dest = "";

                        if (movies.size() != 0) {
                            while(true) {
                                System.out.print("\n-> Do you want to put the export file in the current folder? [yes/no] ");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("no")) {
                                System.out.println("\n-> Please enter the location of folder that you want to put the file: "); 
                                
                                sc.nextLine();
                                dest = sc.nextLine();
                            } else sc.nextLine();

                            System.out.println("\n-> Please enter the name for \".csv\" file [name]: ");
                            String fileName = sc.nextLine();

                            dest += fileName + ".csv";

                            while(true) {
                                System.out.println("\n-> Are you sure you want to export your file to " + dest + (answer.equals("yes") ? " in current folder" : "") + "? [yes/no]");
                                answer = sc.next();

                                if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                                else break;
                            }

                            if(answer.equals("yes")) break;
                        }
                    }

                    FileOperations.writeToCSV(movies, dest);

                    System.out.println("\n-> Finished! Your file is located in the " + dest);
                    break;
                default:
                    Screen.EndScreen();
                    System.exit(0);
            }

            if(cnt == 0) {
                while(true) {
                    System.out.println("\n-> I hope you enjoyed while analyzing the movies this database has! Do you want to continue your journey here? [yes/no]");
                    answer = sc.next();

                    if(!(answer.equals("yes") || answer.equals("no"))) System.out.println("\n-> Please enter either \"yes\" or \"no\". \"" + answer + "\" is not understandable :-(\n");
                    else break;
                }
    
                if(answer.equals("no")) {
                    Screen.EndScreen();
                    break;
                }
            }
        }
        
        sc.close();
    }
    
}