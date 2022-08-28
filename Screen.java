class ConsoleColors {
    public static final String RESET = "\033[0m";  
    public static final String PURPLE = "\u001B[35m";     
    public static final String WHITE = "\033[0;37m";   
    public static final String WHITE_BOLD = "\033[1;37m";
}

public class Screen {
    public static void StartScreen() {
        System.out.print("\033[H\033[2J");
        System.out.println(ConsoleColors.PURPLE + "\t\t\t" + " _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.println("\t\t\t" + "* ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** *" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE + "\t\t\t" + "#----------------------------------------------->Movie<-------------------------------------------------------#\n");
	    System.out.println("\t\t\t" + "#---------------------------------------------->DATASET<------------------------------------------------------#\n");
	    System.out.println("\t\t\t" + "#--------------------------------------------->MANAGEMENT<----------------------------------------------------#\n" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "\t\t\t" + "*_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_*" + ConsoleColors.RESET);

        System.out.println("\n\n\t\tWELCOME TO THE MOVIE DATASET MANAGEMENT. WITH THIS INTERFACE YOU WILL HAVE A CHANCE TO ACCESS MORE THAN 9000 MOVIES! ENJOY IT!");

        System.out.println(ConsoleColors.WHITE_BOLD + "\n\n\t\t\t\t\t\t\t\tPress Enter to continue..." + ConsoleColors.RESET);

        try { 
            System.in.read(); 
        } catch(Exception e) {
            System.out.println("An error occured: " + e.getMessage());
        }  
        
    }

    public static void MainMenu() {
        System.out.print("\033[H\033[2J");

        System.out.println(ConsoleColors.WHITE + "\t#-----------------------------------------------------------------------------------------------#\n");
        System.out.println("\t#					   >MAIN MENU<					        #\n");
        System.out.println("\t#-----------------------------------------------------------------------------------------------#\n" + ConsoleColors.RESET);
    }

    public static void ListMenu(){
        System.out.print("\033[H\033[2J");

        System.out.println(ConsoleColors.WHITE + "\t#-----------------------------------------------------------------------------------------------#\n");
        System.out.println("\t#					   >LIST MENU<					        #\n");
        System.out.println("\t#-----------------------------------------------------------------------------------------------#\n" + ConsoleColors.RESET);
    }

    public static void SortMenu(){
        System.out.print("\033[H\033[2J");

        System.out.println(ConsoleColors.WHITE + "\t#-----------------------------------------------------------------------------------------------#\n");
        System.out.println("\t#					   >SORT MENU<					        #\n");
        System.out.println("\t#-----------------------------------------------------------------------------------------------#\n" + ConsoleColors.RESET);
    }

    public static void SearchMenu(){
        System.out.print("\033[H\033[2J");

        System.out.println(ConsoleColors.WHITE + "\t#-----------------------------------------------------------------------------------------------#\n");
        System.out.println("\t#					>SEARCH MENU<	        			        #\n");
        System.out.println("\t#-----------------------------------------------------------------------------------------------#\n" + ConsoleColors.RESET);
    }

    public static void ColumnName(){
        System.out.print("\033[H\033[2J");

        System.out.println(ConsoleColors.WHITE + "\t#-----------------------------------------------------------------------------------------------#\n");
        System.out.println("\t#		                        >COLUMN NAME LIST<		                        #\n");
        System.out.println("\t#-----------------------------------------------------------------------------------------------#\n" + ConsoleColors.RESET);
    }

    public static void FilterMenu(){
        System.out.print("\033[H\033[2J");

        System.out.println(ConsoleColors.WHITE + "\t#-----------------------------------------------------------------------------------------------#\n");
        System.out.println("\t#			           >FILTER MENU<					        #\n");
        System.out.println("\t#-----------------------------------------------------------------------------------------------#\n" + ConsoleColors.RESET);
    }

    public static void EndScreen() {
        System.out.print("\033[H\033[2J");
        System.out.println(ConsoleColors.PURPLE + "\t\t\t" + " _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        System.out.println("\t\t\t" + "* ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** *" + ConsoleColors.RESET);
	    System.out.println("\t\t\t" + "#---------------------------------------------->Finished successfully!<--------------------------------------#\n");
	    System.out.println("\t\t\t" + "#----------------------------------------------------->GOOD BYE<---------------------------------------------#\n" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "\t\t\t" + "*_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_**_*_*" + ConsoleColors.RESET);
    }
}
