import java.util.ArrayList;
import java.util.List;

/**
 * Print out all valid dates a user can enter
 * Author - Luke Piper
 */
public class ValidTests {
    /**
     * Print out all valid dates
     * @param separator
     * @throws Exception
     */
    public static void generateDates(String separator) throws Exception {
        // List of amount of days in month
        ArrayList<Integer> daysInMonths = new ArrayList<Integer>(List.of(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31));
        // List of first 3 letters of months
        ArrayList<String> MONTHS = new ArrayList<String>(List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));

        final int MINYEAR = 1753;
        final int MAXYEAR = 3000;
        // Loop through years
        for(int year = MINYEAR; year <= MAXYEAR; year++) {
            // Change daysInMonths list if leap year found
            if(isLeapYear(year)) {
                daysInMonths.set(1, 29);
            } else {
                daysInMonths.set(1, 28);
            }
            // Loop through months
            for (int month = 1; month <= MONTHS.size(); month++) {
                int daysInMonth = daysInMonths.get(month-1);
                // Loop through all days from 1 to daysInMonth
                for (int day = 1; day <= daysInMonth; day++) {
                    // Change print formatting for days below 10
                    if(day < 10) {
                        System.out.println("0"+day+separator+month+separator+year);
                        System.out.println("0"+day+separator+MONTHS.get(month-1)+separator+year);
                        System.out.println("0"+day+separator+MONTHS.get(month-1).toUpperCase()+separator+year);
                        System.out.println("0"+day+separator+MONTHS.get(month-1).toLowerCase()+separator+year);
                        
                    }
                    // Change print formatting for months below 10
                    if(month < 10) {
                        // Change print formatting for days below 10
                        if(day < 10) {
                            System.out.println("0"+day+separator+"0"+month+separator+year);
                        }
                        System.out.println(day+separator+"0"+month+separator+year);
                    }
                    // Print all valid formats of this date
                    System.out.println(day+separator+month+separator+year);
                    System.out.println(day+separator+MONTHS.get(month-1)+separator+year);
                    System.out.println(day+separator+MONTHS.get(month-1)+separator+year);
                    System.out.println(day+separator+MONTHS.get(month-1).toUpperCase()+separator+year);
                    System.out.println(day+separator+MONTHS.get(month-1).toLowerCase()+separator+year);
                }
            }
        }
        
        // Repeat process for yy years
        for(int year = 0; year < 100; year++) {
            // Change daysInMonths list if leap year found
            if(isLeapYear(year)) {
                daysInMonths.set(1, 29);
            } else {
                daysInMonths.set(1, 28);
            }
            // Loop through months
            for (int month = 1; month <= 12; month++) {
                int daysInMonth = daysInMonths.get(month-1);
                // Loop through all days from 1 to daysInMonth
                for (int day = 1; day <= daysInMonth; day++) {
                    // Change print formatting for days below 10
                    if(day < 10) {
                        if(year < 10) {
                            System.out.println("0"+day+separator+month+separator+"0"+year);
                            System.out.println("0"+day+separator+MONTHS.get(month-1)+separator+"0"+year);
                            System.out.println("0"+day+separator+MONTHS.get(month-1).toUpperCase()+separator+"0"+year);
                            System.out.println("0"+day+separator+MONTHS.get(month-1).toLowerCase()+separator+"0"+year);
                        } else {
                            System.out.println("0"+day+separator+month+separator+year);
                            System.out.println("0"+day+separator+MONTHS.get(month-1)+separator+year);
                            System.out.println("0"+day+separator+MONTHS.get(month-1).toUpperCase()+separator+year);
                            System.out.println("0"+day+separator+MONTHS.get(month-1).toLowerCase()+separator+year);
                        }
                        
                    }
                    // Change print formatting for months below 10
                    if(month < 10) {
                        // Change print formatting for days below 10
                        if(day < 10) {
                            if(year < 10) {
                                System.out.println("0"+day+separator+"0"+month+separator+"0"+year);
                            } else {
                                System.out.println("0"+day+separator+"0"+month+separator+year);
                            }
                        }
                        if(year < 10) {
                            System.out.println(day+separator+"0"+month+separator+"0"+year);
                        } else {
                            System.out.println(day+separator+"0"+month+separator+year);
                        }
                    }

                    if(year < 10) {
                        System.out.println(day+separator+month+separator+"0"+year);
                        System.out.println(day+separator+MONTHS.get(month-1)+separator+"0"+year);
                        System.out.println(day+separator+MONTHS.get(month-1)+separator+"0"+year);
                        System.out.println(day+separator+MONTHS.get(month-1).toUpperCase()+separator+"0"+year);
                        System.out.println(day+separator+MONTHS.get(month-1).toLowerCase()+separator+"0"+year);
                    } else {
                        // Print all valid formats of this date
                        System.out.println(day+separator+month+separator+year);
                        System.out.println(day+separator+MONTHS.get(month-1)+separator+year);
                        System.out.println(day+separator+MONTHS.get(month-1)+separator+year);
                        System.out.println(day+separator+MONTHS.get(month-1).toUpperCase()+separator+year);
                        System.out.println(day+separator+MONTHS.get(month-1).toLowerCase()+separator+year);
                    }    
                }
            }
        }
    }

    /**
     * Return true if year is a valid leap year, false if not
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        //return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
        ArrayList<Integer> leapYears = new ArrayList<Integer>(List.of(1756, 1760, 1764, 1768, 1772, 1776, 1780, 1784, 1788, 1792, 1796, 1804, 1808, 1812, 1816, 1820, 1824, 1828, 1832, 1836, 1840,
        1844, 1848, 1852, 1856, 1860, 1864, 1868, 1872, 1876, 1880, 1884, 1888, 1892, 1896, 1904, 1908, 1912, 1916, 1920, 1924, 1928, 1932, 1936, 1940, 1944, 1948, 1952, 1956, 1960, 1964, 1968, 1972, 1976,
        1980, 1984, 1988, 1992, 1996, 2000, 2004, 2008, 2012, 2016, 2020, 2024, 2028, 2032, 2036, 2040, 2044, 2048, 2052, 2056, 2060, 2064, 2068, 2072, 2076, 2080, 2084, 2088, 2092, 2096, 2104, 2108, 2112,
        2116, 2120, 2124, 2128, 2132, 2136, 2140, 2144, 2148, 2152, 2156, 2160, 2164, 2168, 2172, 2176, 2180, 2184, 2188, 2192, 2196, 2204, 2208, 2212, 2216, 2220, 2224, 2228, 2232, 2236, 2240, 2244, 2248,
        2252, 2256, 2260, 2264, 2268, 2272, 2276, 2280, 2284, 2288, 2292, 2296, 2304, 2308, 2312, 2316, 2320, 2324, 2328, 2332, 2336, 2340, 2344, 2348, 2352, 2356, 2360, 2364, 2368, 2372, 2376, 2380, 2384,
        2388, 2392, 2396, 2400, 2404, 2408, 2412, 2416, 2420, 2424, 2428, 2432, 2436, 2440, 2444, 2448, 2452, 2456, 2460, 2464, 2468, 2472, 2476, 2480, 2484, 2488, 2492, 2496, 2504, 2508, 2512, 2516, 2520,
        2524, 2528, 2532, 2536, 2540, 2544, 2548, 2552, 2556, 2560, 2564, 2568, 2572, 2576, 2580, 2584, 2588, 2592, 2596, 2604, 2608, 2612, 2616, 2620, 2624, 2628, 2632, 2636, 2640, 2644, 2648, 2652, 2656,
        2660, 2664, 2668, 2672, 2676, 2680, 2684, 2688, 2692, 2696, 2704, 2708, 2712, 2716, 2720, 2724, 2728, 2732, 2736, 2740, 2744, 2748, 2752, 2756, 2760, 2764, 2768, 2772, 2776, 2780, 2784, 2788, 2792,
        2796, 2800, 2804, 2808, 2812, 2816, 2820, 2824, 2828, 2832, 2836, 2840, 2844, 2848, 2852, 2856, 2860, 2864, 2868, 2872, 2876, 2880, 2884, 2888, 2892, 2896, 2904, 2908, 2912, 2916, 2920, 2924, 2928,
        2932, 2936, 2940, 2944, 2948, 2952, 2956, 2960, 2964, 2968, 2972, 2976, 2980, 2984, 2988, 2992, 2996, 52, 56, 60, 64, 68, 72, 76, 80, 84, 88, 92, 96, 00, 04, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48));
        return leapYears.contains(year);
    }

    /**
     * Call generateDates method with all 3 valid separators
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        generateDates(" ");
        generateDates("-");
        generateDates("/");
    }
}
