import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Date class to read user input and determine whether or not it is a valid date between 1753 and 3000
 * Author - Luke Piper
 */
public class Date {
    /**
     * Determine whether or not a user inputted date is a validate date between the years 1753 and 3000
     * @param date
     * @return void
     */
    public static void isValidDate(String date) {
        // Store first 3 letters of every month for further testing
        final ArrayList<String> MONTHS = new ArrayList<String>(List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        // List of amount of days in month - Not a constant because Feb days change on leap years
        ArrayList<Integer> daysInMonths = new ArrayList<Integer>(List.of(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31));

        // Store separator
        String separator = getSeparator(date);
        if(separator == null) return;

        // Make sure only 2 separators are present input
        if(checkFor2Separators(separator, date) == false) return;

        // Split input string by separator
        String[] format = date.split(separator);
        if(checkFormatLength(format, date) == false) return;

        // Store month
        int month = getMonth(format, date, MONTHS);
        if(month == -1) return;

        // Store the day as int
        int day = getDay(format, date);
        if(day == -1) return;
        
        // Store year
        int year = getYear(format, date);
        if(year == -1) return;

        // Check format of yearString
        if(getYearString(format, date) == false) return;

        // Store dayString
        String dayString = getDayString(format, date, day);
        if(dayString == null) return;

        // Set date to yyyy format
        year = setYear(year);
        // Check ranges of day, month and year
        if(checkMonthRanges(month, date, MONTHS.size()) == false) return;
        if(checkYearRanges(year, date) == false) return;
        if(checkDayRanges(day, month, year, date, daysInMonths, MONTHS) == false) return;

        // Print date
        System.out.println(dayString + " " + MONTHS.get(month-1) + " " + year);
    }

    /**
     * Make sure only two instances of the separator is present
     * @param separator
     * @param date
     * @return true/false
     */
    public static boolean checkFor2Separators(String separator, String date) {
        // Make sure user input has only 2 separators present
        int firstSeparator = date.indexOf(separator);
        int secondSeparator = date.indexOf(separator, firstSeparator + 1);
        int thirdSeparator = date.indexOf(separator, secondSeparator + 1);

        // If more than two occurences of separator, return error
        if (thirdSeparator != -1) {
            System.out.println(date + " - INVALID");
            System.err.println("Only two separators allowed");
            return false;
        }
        return true;
    }

    /**
     * Validate whether there is a day, month, and year inputted
     * @param format
     * @param date
     */
    public static boolean checkFormatLength(String[] format, String date) {
        // Check for date having day, month, and year
        final int VALID_FORMART_LENGTH = 3;
        if (format.length != VALID_FORMART_LENGTH) {
            System.out.println(date + " - INVALID");
            System.err.println("Formatted date incorrectly");
            return false;
        }
        return true;
    }

    /**
     * Validate monthString is valid Jan, jan, or JAN format
     * @param monthString
     * @param date
     */
    public static boolean getMonthSubString(String monthString, String date) {
        // Check if monthString has a first letter uppercase and rest lowercase
        if(!monthString.substring(0).equals(monthString.substring(0).toUpperCase())) {
            if(!monthString.substring(1).equals(monthString.substring(1).toLowerCase())
            || !monthString.substring(2).equals(monthString.substring(2).toLowerCase())) {
                System.out.println(date + " - INVALID");
                System.err.println("Formatted month incorrectly");
                return false;
            }
        }
        return true;
    }

    /**
     * Validate dayString
     * @param format
     * @param date
     * @param day
     * @return dayString
     */
    public static String getDayString(String[] format, String date, int day) {
        // Make dayString when outputting date back to user
        String dayString = format[0];
        final int MAXDAY_LENGTH = 2;
        // Error is day is ddd or longer
        if(dayString.length() > MAXDAY_LENGTH) {
            System.out.println(date + " - INVALID");
            System.err.println("Formatted day incorrectly");
            return null;
        }
        // Set dayString
        if (day < 10) {
            dayString = "0" + day;
        }
        return dayString;
    }

    /**
     * Validate yearString is valid
     * @param format
     * @param date
     * @return true / false
     */
    public static boolean getYearString(String[] format, String date) {
        // If year is not in yy or yyyy format
        String yearString = format[2];
        if(yearString.length() != 2 && yearString.length() != 4) {
            System.out.println(date + " - INVALID");
            System.err.println("Formatted year incorrectly");
            return false;
        }
        // If year start with 00 and not in yy format then error
        if(yearString.startsWith("00") && yearString.length() != 2) {
            System.out.println(date + " - INVALID");
            System.err.println("Formatted year incorrectly");
            return false;
        }
        return true;
    }

    /**
     * Store year as int, error otherwise
     * @param format
     * @param date
     * @return int year
     */
    public static int getYear(String[] format, String date) {
        // Store year as int
        int year;
        try {
            year = Integer.parseInt(format[2]);
        } catch (NumberFormatException e) {
            // Otherwise print error
            System.out.println(date + " - INVALID");
            System.err.println("Year has to be an integer");
            return -1;
        }
        return year;
    }

    /**
     * Try store month as int, otherwise find if string inputted is valid month of first 3 letters
     * @param format
     * @param date
     * @param MONTHS
     * @return int month
     */
    public static int getMonth(String[] format, String date, ArrayList<String> MONTHS) {
        // Convert month to integer
        int monthCount = 0;
        int month = 0;
        final int MAX_INT_MONTH_LENGTH = 2;
        try {
            // Try convert month to integer
            month = Integer.parseInt(format[1]);
            monthCount++;
            String monthString = format[1];
            // Validate length of month to keep inside mm, m, or 0m range
            if(monthString.length() > MAX_INT_MONTH_LENGTH) {
                System.out.println(date + " - INVALID");
                System.err.println("Formatted month incorrectly");
                return -1;
            }
        } catch (NumberFormatException e) {
            // Else check if user month is a valid first three letters of a month
            String monthString = format[1];
            // Loop through our list of months names
            for(int i = 0; i < MONTHS.size(); i++) {
                // Find if user input for month matches a month in list
                if(monthString.toLowerCase().equals(MONTHS.get(i).toLowerCase())) {
                    // If so, set integer month to corresponding month
                    month = i + 1;
                    monthCount++;
                }
            }
            // Check month is in jan, Jan, JAN formats only
            if(getMonthSubString(monthString, date) == false) return -1;
        }

        // If month not an int and not found in month list then error
        if (monthCount == 0) {
            System.out.println(date + " - INVALID");
            System.err.println("Formatted month incorrectly");
            return -1;
        }
        return month;
    }

    /**
     * Try stores day as int, error otherwise
     * @param format
     * @param date
     * @return day
     */
    public static int getDay(String[] format, String date) {
        int day;
        try {
            day = Integer.parseInt(format[0]);
        } catch (NumberFormatException e) {
            // Otherwise print error
            System.out.println(date + " - INVALID");
            System.err.println("Day has to be an integer");
            return -1;
        }
        return day;
    }

    /**
     * Gets separator used by user in input
     * @param date
     * @return separator string, "" if none found
     */
    public static String getSeparator(String date) {
        // Split date by "-", "<space>", or "/"
        String separator = "";
        if(date.contains("-")) {
            separator = "-";
        } else if(date.contains(" ")) {
            separator = " ";
        } else if(date.contains("/")) {
            separator = "/";
        } else {
            // If no separator present
            System.out.println(date + " - INVALID");
            System.err.println("Use of invalid separator");
            return null;
        }
        return separator;
    }

    /**
     * Checks whether year in range for MINYEAR(1753) and MAXYEAR(3000) inclusive
     * @param year
     * @param date
     * @return true if year in range / false otherwise
     */
    public static boolean checkYearRanges(int year, String date) {
        final int MINYEAR = 1753;
        final int MAXYEAR = 3000;
        // Year range validating
        if(year < MINYEAR || year > MAXYEAR) {
            System.out.println(date + " - INVALID");
            System.err.println("Year is out of range");
            return false;
        }
        return true;
    }

    /**
     * Checks whether month is in range of 1-12
     * @param month
     * @param date
     * @param length
     * @return true if month is in range / false otherwise
     */
    public static boolean checkMonthRanges(int month, String date, int length) {
        // Month range validating
        if(month < 1 || month > length) {
            System.out.println(date + " - INVALID");
            System.err.println("Month is out of range");
            return false;
        }
        return true;
    }

    /**
     * Checks whether the day inputted in is range for the given month
     * @param day
     * @param month
     * @param year
     * @param date
     * @param daysInMonths
     * @param MONTHS
     * @return true/false whether day is in range for given month
     */
    public static boolean checkDayRanges(int day, int month, int year, String date, ArrayList<Integer> daysInMonths, ArrayList<String> MONTHS) {
        // Validate day range for Feb
        if (month == MONTHS.indexOf("Feb")+1) {
            // Validate day range for leap years Feb
            if(isLeapYear(year)) {
                daysInMonths.set(1, 29);
            }
        }
        // Check day range for whatever month user has entered
        if(day < 1 || day > daysInMonths.get(month-1)) {
            System.out.println(date + " - INVALID");
            System.err.println("Day is out of range");
            return false;
        }
        return true;
    }

    /**
     * If user inputted date in yy formated, convert year to yyyy format
     * @param year
     * @return year in correct yyyy format for output
     */
    public static int setYear(int year) {
        // If year in yy format
        if(year >= 00 && year < 100) {
            // Convert to yyyy format
            if (year < 50) {
                year += 2000;
            } else {
                year += 1900;
            }
        }
        return year;
    }

    /**
     * Check is year is a leap year
     * @param year
     * @return true/false
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    /**
     * Read user input and go to validate the date they inputted
     * @param args
     */
    public static void main(String[] args) {
        // Create scanner to read user input
        Scanner scan = new Scanner(System.in);
        // While input is still there
        while(scan.hasNextLine()) {
            // Store user input
            String date = scan.nextLine();
            // Validate date
            isValidDate(date);
        }
        // Close scanner
        scan.close();
    }
}