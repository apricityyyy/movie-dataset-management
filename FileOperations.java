import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileOperations {

    public static LinkedHashMap<Integer, Movie> turnIntoMap(String fileName) throws NumberFormatException, ParseException {
        // creating a list of movies that will be returned
        LinkedHashMap<Integer, Movie> movieList = new LinkedHashMap<>();

        try(FileReader fr = new FileReader(fileName); 
            BufferedReader br = new BufferedReader(fr);) {
            String line;
            int cnt = 0;

            while((line = br.readLine() ) != null) {
                List<String> tokens = splitWithParser(line); // a function which splits a line by regex ","; however, ignores the commas inside quotes
            

                if(tokens.size() == 9) cnt++; // cnt represents rows, and a row is complete only if the size of tokens is 9.


                // Some lines seem separated in input file. We wrote a fixing code that will merge those separated lines.               
                while(tokens.size() != 9) {
                    line += "\n" + br.readLine();

                    tokens = splitWithParser(line);

                    cnt++;
                }

                
                // now that the tokens are ready, it is time to assign them to the fields of each movie and add them to the general list of movies.
                if(cnt > 1 /*for ignoring the first line*/) {
                    Movie m = new Movie(tokens.get(0), tokens.get(1), tokens.get(2), Double.parseDouble(tokens.get(3)),
                                        Integer.parseInt(tokens.get(4)), Double.parseDouble(tokens.get(5)), 
                                        tokens.get(6),tokens.get(7), tokens.get(8));

                    movieList.put(cnt, m);
                }


            }

        } catch(IOException ioe) {
            System.out.println("An error occured: " + ioe.getMessage());
        }

        return movieList;
    }

    /***************************************************************************************
    *    Title: Ignoring Commas in Quotes When Splitting a Comma-separated String
    *    Author: baeldung
    *    Date: 2021
    *    Availability: https://www.baeldung.com/java-split-string-commas
    *
    ***************************************************************************************/
    public static List<String> splitWithParser(String input) {
        List<String> tokens = new ArrayList<String>();
        int startPosition = 0;
        boolean isInQuotes = false;

        for (int currentPosition = 0; currentPosition < input.length(); currentPosition++) {
            if (input.charAt(currentPosition) == '\"') {
                isInQuotes = !isInQuotes;
            }
            else if (input.charAt(currentPosition) == ',' && !isInQuotes) {
                tokens.add(input.substring(startPosition, currentPosition));
                startPosition = currentPosition + 1;
            }
        }
        
        String lastToken = input.substring(startPosition);
        if (lastToken.equals(",")) {
            tokens.add("");
        } else {
            tokens.add(lastToken);
        }

        return tokens;
    } 
    
    public static LinkedHashMap<Field, List<Object>> listFields(List<Movie> movies, Field[] fields) {
        LinkedHashMap<Field, List<Object>> res = new LinkedHashMap<>();
        List<Object> list_of_field;

        Field[] movie_fields = Movie.class.getDeclaredFields();

        for(int i = 1; i <= 9; i++) {
            for(Field field : fields) {
                String name = field.getName();
                
                if(movie_fields[i].equals(field)) {
                    if(movie_fields[i].getType().toString().contains("String")) {
                        list_of_field = movies.stream().limit(100).map(fl -> {
                            try {
                                Field fieldd = Movie.class.getDeclaredField(name);
                                fieldd.setAccessible(true);

                                return (String) fieldd.get(fl);
                            } catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 

                            return null;
                        }).collect(Collectors.toList());

                        res.put(field, list_of_field);
                    } else if(movie_fields[i].getType().toString().contains("Double")) {
                        list_of_field = movies.stream().limit(100).map(fl -> {
                            try {
                                Field fieldd = Movie.class.getDeclaredField(name);
                                fieldd.setAccessible(true);

                                return (Double) fieldd.get(fl);
                            } catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 

                            return null;
                        }).collect(Collectors.toList());

                        res.put(field, list_of_field);
                    } else if(movie_fields[i].getType().toString().contains("Integer")) {
                        list_of_field = movies.stream().limit(100).map(fl -> {
                            try {
                                Field fieldd = Movie.class.getDeclaredField(name);
                                fieldd.setAccessible(true);

                                return (Integer) fieldd.get(fl);
                            } catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 

                            return null;
                        }).collect(Collectors.toList());

                        res.put(field, list_of_field);
                    } else {
                        list_of_field = movies.stream().limit(100).map(fl -> {
                            try {
                                Field fieldd = Movie.class.getDeclaredField(name);
                                fieldd.setAccessible(true);

                                return ((LocalDate) fieldd.get(fl)).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                            } catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 

                            return null;
                        }).collect(Collectors.toList());

                        res.put(field, list_of_field);
                    }
                }
            }
        }
        
        return res;
    }

    public static LinkedHashMap<Integer, Movie> listWithRange(Pair p, LinkedHashMap<Integer, Movie> movieList) {
        LinkedHashMap<Integer, Movie> res = new LinkedHashMap<>();
        
        Set<Integer> keys =  movieList.keySet();
        Iterator<Integer> it = keys.iterator();
        while(it.hasNext()) {
            Integer key = it.next();

            if(key > p.max) break;
            
            if(key >= p.min) res.put(key, movieList.get(key));
        }

        return res;
    }

    public static List<Movie> sortByField(List<Movie> movies, String f, String order) {
        List<Movie> sortedList = new LinkedList<>();

        Field[] fields = Movie.class.getDeclaredFields();

        for(int i = 1; i <= 9; i++) {
            if(fields[i].getName().equals(f)) {
                if(order.equals("ASC")) {
                    if(fields[i].getType().toString().contains("String")) {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (String) field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return f;
                        }
                        )).collect(Collectors.toList());
                    } else if(fields[i].getType().toString().contains("Double")) {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (Double)field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return 0.0;
                        }
                        )).collect(Collectors.toList());
                    } else if(fields[i].getType().toString().contains("Integer")) {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (Integer)field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return 0;
                        }
                        )).collect(Collectors.toList());
                    } else {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (LocalDate)field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return null;
                        }
                        )).collect(Collectors.toList());

                    }
                } else if (order.equals("DESC")) {
                    if(fields[i].getType().toString().contains("String")) {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (String)field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return f;
                        }
                        ).reversed()).collect(Collectors.toList());
                    } else if(fields[i].getType().toString().contains("Double")) {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (double)field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return 0.0;
                        }
                        ).reversed()).collect(Collectors.toList());
                    } else if(fields[i].getType().toString().contains("Integer")) {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (Integer)field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return 0;
                        }
                        ).reversed()).collect(Collectors.toList());
                    } else {
                        sortedList = movies.stream().sorted(Comparator.comparing(fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (LocalDate)field.get(fl);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return null;
                        }
                        ).reversed()).collect(Collectors.toList());

                    }
                }
            }
        }
        return sortedList;
    }

    public static List<Movie> searchByField(List<Movie> movies, String f, Object value) throws ParseException, NoSuchFieldException, SecurityException {
        List<Movie> searchedList = new LinkedList<>();
        Field[] fields = Movie.class.getDeclaredFields();

        for(int i = 1; i <= 9; i++) {
            if(fields[i].getName().equals(f)) {
                if(fields[i].getType().toString().contains("String")) {
                    searchedList = movies.stream().filter((fl -> {
                        try {
                            Field field = Movie.class.getDeclaredField(f);
                            field.setAccessible(true);
                            return ((String)field.get(fl)).contains((String)value);
                        }
                        catch (Exception ex) {
                            System.out.println("An error occured: " + ex.getMessage());
                        } 
                        return false;
                    })).collect(Collectors.toList());
                } else if(fields[i].getType().toString().contains("Double")) {
                    searchedList = movies.stream().filter((fl -> {
                        try {
                            Field field = Movie.class.getDeclaredField(f);
                            field.setAccessible(true);
                            return ((Double)field.get(fl)) == Double.parseDouble((String) value);
                        }
                        catch (Exception ex) {
                            System.out.println("An error occured: " + ex.getMessage());
                        } 
                        return false;
                    })).collect(Collectors.toList());
                } else if(fields[i].getType().toString().contains("Integer")) {
                    searchedList = movies.stream().filter((fl -> {
                        try {
                            Field field = Movie.class.getDeclaredField(f);
                            field.setAccessible(true);
                            return ((Double)field.get(fl)) == Integer.parseInt((String) value);
                        }
                        catch (Exception ex) {
                            System.out.println("An error occured: " + ex.getMessage());
                        } 
                        return false;
                    })).collect(Collectors.toList());
                } else { 
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    searchedList = movies.stream().filter((fl -> {
                        try {
                            Field field = Movie.class.getDeclaredField(f);
                            field.setAccessible(true);
                            return ((LocalDate)field.get(fl)).equals(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        }
                        catch (Exception ex) {
                            System.out.println("An error occured: " + ex.getMessage());
                        } 
                        return false;
                    })).collect(Collectors.toList());
                }
            }
        }

         return searchedList;
    }

    public static List<Movie> filterFields(List<Movie> movies, String f, String rule, Object value) {
        List<Movie> filteredList = new LinkedList<>(movies);
        Field[] fields = Movie.class.getDeclaredFields();
        int check1 = 0, check2 = 0;

        for(int i = 1; i <= 9; i++) {
            if(fields[i].getName().equals(f)) {
                check1++;
                if(fields[i].getType().toString().contains("String")) {
                    if(rule.equals("in any given genre(s)")) {
                        check2++;
                        String[] genres = ((String) value).split(" ");
                        List<Movie> something;
                        filteredList = movies;

                        for(int j = 0; j < genres.length; j++) {
                            System.out.println(genres[j]);
                            something = filterFields(filteredList, "Genre", "contains", genres[j]);
                            filteredList = something;
                        }
                    } else if(rule.equals("startsWith")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                
                                return ((String)field.get(fl)).startsWith((String)value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if (rule.equals("endsWith")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((String)field.get(fl)).endsWith((String)value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if (rule.equals("contains")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((String)field.get(fl)).contains((String)value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if (rule.equals("null")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return !(((String)field.get(fl)).equals(value));
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    }
                } else if(fields[i].getType().toString().contains("Double")) {
                    if(rule.equals("equal")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Double)field.get(fl)) == Double.parseDouble((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("greater than")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Double)field.get(fl)) > Double.parseDouble((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("less than")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Double)field.get(fl)) < Double.parseDouble((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("greater and equal to")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Double)field.get(fl)) >= Double.parseDouble((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("less and equal to")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Double)field.get(fl)) <= Double.parseDouble((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("between")) {
                        check2++;
                        String[] limits = ((String) value).split(" ");
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((Double)field.get(fl)) >= Double.parseDouble((String) limits[0]) &&
                                        ((Double)field.get(fl)) <= Double.parseDouble((String) limits[1]);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("null")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (Double)field.get(fl) != Double.parseDouble((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    }
                } else if(fields[i].getType().toString().contains("Integer")) {
                    if(rule.equals("equal")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Integer)field.get(fl)) == Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());;
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("greater than")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Integer)field.get(fl)) > Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("less than")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Integer)field.get(fl)) < Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("greater and equal to")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Integer)field.get(fl)) >= Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("less and equal to")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((Integer)field.get(fl)) <= Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("between")) {
                        check2++;
                        String[] limits = ((String) value).split(" ");
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((Integer)field.get(fl)) >= Integer.parseInt((String) limits[0]) &&
                                        ((Integer)field.get(fl)) <= Integer.parseInt((String) limits[1]);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("null")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return (Integer)field.get(fl) != Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    }
                } else { 
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    if(rule.equals("equal")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((LocalDate)field.get(fl)).equals(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("greater than")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return ((LocalDate)field.get(fl)).isAfter(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("less than")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return((LocalDate)field.get(fl)).isBefore(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("greater and equal to")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((LocalDate)field.get(fl)).isAfter(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) ||
                                        ((LocalDate)field.get(fl)).equals(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("less and equal to")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((LocalDate)field.get(fl)).isBefore(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) ||
                                        ((LocalDate)field.get(fl)).equals(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("between")) {
                        check2++;
                        String[] limits = ((String) value).split(" ");
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((LocalDate)field.get(fl)).isAfter(df.parse(limits[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) &&
                                        ((LocalDate)field.get(fl)).isBefore(df.parse(limits[1]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("null")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  !(((LocalDate)field.get(fl)).isEqual(df.parse((String) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("in a specific year")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((LocalDate)field.get(fl)).getYear() == Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("in a specific month")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((LocalDate)field.get(fl)).getMonthValue() == Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    } else if(rule.equals("in a specific day")) {
                        check2++;
                        filteredList = movies.stream().filter((fl -> {
                            try {
                                Field field = Movie.class.getDeclaredField(f);
                                field.setAccessible(true);
                                return  ((LocalDate)field.get(fl)).getDayOfMonth() == Integer.parseInt((String) value);
                            }
                            catch (Exception ex) {
                                System.out.println("An error occured: " + ex.getMessage());
                            } 
                            return false;
                        })).collect(Collectors.toList());
                    }
                }
            } 
        }

        if(check1 == 0 || check2 == 0) filteredList.clear();

        return filteredList;
    }
    
    public static void writeToCSV(List<Movie> movies, String dest) {
        File file = new File(dest);

        try {
            file.createNewFile();
        } catch (IOException e1) {
            System.out.println("An error occured: " + e1.getMessage());
        }

        try (FileWriter writer = new FileWriter(file)) {
            Iterator<Movie> itr = movies.iterator();

            writer.write("Release_Date,Title,Overview,Popularity,Vote_Count," +
                        "Vote_Average,Original_Language,Genre,Poster_Url");

            while(itr.hasNext()) {
                Movie m = itr.next();

                writer.write("\n" + m.getRelease_Date() + "," + m.getTitle() + "," + m.getOverview() + "," + m.getPopularity() +
                             "," + m.getVote_Count() + "," + m.getVote_Average() + "," + m.getOriginal_Language() + "," + m.getGenre() + "," + m.getPoster_Url());
            }
        } catch (IOException e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }

}

class Pair {
    int min;
    int max;

    public Pair(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
}