// Author: Luke Piper
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

// Clients struct
struct clients {
    char *first_name;
    char *last_name;
    char *phone;
    char *email_address;
};

static int count;

// Swap clients in client_database struct
void swap_clients(struct clients **client_database, int i, int j) {
    struct clients *temp = client_database[i];
    client_database[i] = client_database[j];
    client_database[j] = temp;
}

// Sort all clients by first name
void sort_by_first_name(struct clients **client_database, int size) {
    // Loop through all clients
    for (int i = 0; i < size - 1; i++) {
        // Loop through all clients again
        for (int j = 0; j < size - i - 1; j++) {
            // If client is greater than next client
            if (strcmp(client_database[j]->first_name, client_database[j + 1]->first_name) > 0) {
                // Swap clients
                swap_clients(client_database, j, j + 1);
            }
        }
    }
}

// Sort all clients by last name
void sort_by_last_name(struct clients **client_database, int size) {
    // Loop through all clients
    for (int i = 0; i < size - 1; i++) {
        // Loop through all clients again
        for (int j = 0; j < size - i - 1; j++) {
            // If client is greater than next client
            if (strcmp(client_database[j]->last_name, client_database[j + 1]->last_name) > 0) {
                // Swap clients
                swap_clients(client_database, j, j + 1);
            }
        }
    }
}

// Sort all clients by phone number
void sort_by_phone(struct clients **client_database, int size) {
    // Loop through all clients
    for (int i = 0; i < size - 1; i++) {
        // Loop through all clients again
        for (int j = 0; j < size - i - 1; j++) {
            // If client is greater than next client
            if (strcmp(client_database[j]->phone, client_database[j + 1]->phone) > 0) {
                // Swap clients
                swap_clients(client_database, j, j + 1);
            }
        }
    }
}

// Sort all clients by email address
void sort_by_email(struct clients **client_database, int size) {
    // Loop through all clients
    for (int i = 0; i < size - 1; i++) {
        // Loop through all clients again
        for (int j = 0; j < size - i - 1; j++) {
            // If client is greater than next client
            if (strcmp(client_database[j]->first_name, client_database[j + 1]->first_name) > 0) {
                // Swap clients
                swap_clients(client_database, j, j + 1);
            }
        }
    }
}
// Function to find first name
int *find_first_name(struct clients **client_database, char *search_term, int *matches) {
    // What row is the client found
    int *indexes = malloc(count * sizeof(int));
    // Number of times name is found
    int match_count = 0;
    // Loop through all the names
    for (int i = 0; i < count; i++) {
        // If name is found
        if(strcmp(client_database[i]->first_name, search_term) == 0) {
            // Add the index to array
            indexes[match_count] = i;
            // Add to match count
            match_count++;
        }
    }
    // Return indicies
    *matches = match_count;
    return indexes;
}

// Function to find last name
int *find_last_name(struct clients **client_database, char *search_term, int *matches) {
    // What row is the client found
    int *indexes = malloc(count * sizeof(int));
    // Number of times name is found
    int match_count = 0;
    // Loop through all the names
    for (int i = 0; i < count; i++) {
        // If name is found
        if(strcmp(client_database[i]->last_name, search_term) == 0) {
            // Add the index to array
            indexes[match_count] = i;
            // Add to match count
            match_count++;
        }
    }
    // Return indicies
    *matches = match_count;
    return indexes;
}

// Function to find phone number
int find_phone(struct clients **client_database, char* search_term){
    // Loop through all emails
    for (int i = 0; i < count; i++) {
        // If email is found, return row number
        if(strcmp(client_database[i]->phone, search_term) == 0) {
            return i;
        }
    }
    return 0;
}

// Function to find email address
int find_email(struct clients **client_database, char* search_term){
    // Loop through all emails
    for (int i = 0; i < count; i++) {
        // If email is found, return row number
        if(strcmp(client_database[i]->email_address, search_term) == 0) {
            return i;
        }
    }
    return 0;
}

void print_client_info(int row, struct clients **client_database){
    // Print client information
    printf("Record: %d\n\tName: %s %s\n\tPhone: %s\n\tEmail: %s\n", row + 1, client_database[row]->first_name, client_database[row]->last_name, client_database[row]->phone, client_database[row]->email_address);
}

// Main function
int main(int argc, char *argv[]) {
    // Client information
    char first_name[50];
    char last_name[50];
    char phone[50];
    char email_address[50];
    
    // Struct of clients
    struct clients** client_database = (struct clients**) malloc(100*sizeof(client_database));
    struct clients* client;

    // Open file
    char *input_file = argv[1];
    FILE *f = fopen(input_file, "r");
   
    // If file is there
    if(f == NULL){
        fprintf(stderr, "File not found - please enter a valid file into the arguments when running main\n");
        exit(1);
    }
    // Scan contents of each line
    while(fscanf(f, "%s %s %s %s", first_name, last_name, phone, email_address) != EOF) {
        // Allocate memory
        client = malloc(sizeof(*client));
        // Memory allocation and copying
        client->first_name = strdup(first_name);
        client->last_name = strdup(last_name);
        client->phone = strdup(phone);
        client->email_address = strdup(email_address);
        // Set struct in array
        client_database[count] = client; 
        count ++;
    }
    // Close file
    fclose(f);
    free(client);

    int command = 0;
    int repeat = 1;
    // Introduction to program
    printf("Welcome to the client database program.\n");
    // Loop repeating program until user quits
    while (repeat == 1) {
        // Print menu
        printf("---------------------------------------------\n");
        printf("Search by:\n\t1: First Name\n\t2: Last Name\n\t3: Phone Number\n\t4: Email Address\nSort By:\n\t5: First Name\n\t6: Last Name\n\t7: Phone Number\n\t8: Email Address\n9: Print all clients\n10: Quit Program\n");
        printf("---------------------------------------------\n");
        printf("Please enter the number of the action you would like performed: ");
        // Get user input
        if(scanf("%d", &command) != 1){
            printf("Invalid input - requiring integer\n");
            // Clear input buffer
            while (getchar() != '\n') {
                continue;
            }
            // Skip current cycle of loop
            continue;
        }
        // If input is valid
        while(command != 0) {
            // Get value to search for of client
            char* val = malloc(100*sizeof(val[0]));
            // Set rows and matches (count) of clients found with identifier in struct
            int *indexes;
            int matches;
            // Switch case for command
            switch(command){
                // Case 1: First Name
                case 1:
                    // Get name to search for
                    printf("Please enter what first name you are searching for: ");
                    fscanf(stdin, "%s", val); 
                    printf("\nLooking for first name %s\n", val);
                    // Find all clients with that name
                    indexes = find_first_name(client_database, val, &matches);
                    // If a client is found
                    if(matches > 0){
                        printf("\nFound in client records:\n");
                        // Print all clients with that name
                        for (int i = 0; i < matches; i++) {
                            // Print each client with name
                            print_client_info(indexes[i], client_database);
                        }
                    } else {
                        // If no name found in records print error messages
                        printf("Does not exist in client records\n");
                    }
                    // Free memory
                    free(indexes);
                    free(val);
                    command = 0;
                    break;
                // Case 2: Last Name
                case 2:
                    // Get name to search for
                    printf("Please enter what last name you are searching for: ");
                    fscanf(stdin, "%s", val);
                    printf("\nLooking for last name %s\n", val);
                    // Find all clients with that name
                    indexes = find_last_name(client_database, val, &matches);
                    // If a client is found
                    if(matches > 0){
                        printf("\nFound in client record:\n");
                        // Print all clients with that name
                        for (int i = 0; i < matches; i++) {
                            // Print each client with name
                            print_client_info(indexes[i], client_database);
                        }
                    } else {
                        // If no name found in records print error messages
                        printf("Does not exist in client records\n");
                    }
                    // Free memory
                    free(indexes);
                    free(val);
                    command = 0;
                    break;
                // Case 3: Phone Number
                case 3:
                    // Get phone number to search for
                    printf("Please enter what phone number you are searching for: ");
                    fscanf(stdin, "%s", val);
                    printf("\nLooking for phone number %s\n", val);
                    // Find client with that phone number
                    if(find_phone(client_database, val) >= 0){
                        // Print client information
                        printf("\nFound in client record %d\n", find_phone(client_database, val)+1);
                        print_client_info(find_phone(client_database, val), client_database);
                    } else {
                        // If no phone number found in records print error messages
                        printf("Does not exist in client records\n");
                    }
                    free(val);
                    command = 0;
                    break;
                // Case 4: Email Address
                case 4:
                    // Get email address to search for
                    printf("Please enter what email address you are searching for: ");
                    fscanf(stdin, "%s", val);
                    printf("\nLooking for email address %s\n", val);
                    // Find client with that email address
                    if(find_email(client_database, val) >= 0){
                        // Print client information
                        printf("\nFound in client record %d\n", find_email(client_database, val)+1);
                        print_client_info(find_email(client_database, val), client_database);                    
                    } else {
                        // If no email address found in records print error messages
                        printf("Does not exist in client records\n");
                    }
                    free(val);
                    command = 0;
                    break;
                case 5:
                    // Sort by first name
                    sort_by_first_name(client_database, count);
                    printf("Sorting by first name...\n");
                    printf("Clients now sorted by first name\n");
                    command = 0;
                    break;
                case 6:
                    // Sort by first name
                    sort_by_last_name(client_database, count);
                    printf("Sorting by last name...\n");
                    printf("Clients now sorted by last name\n");
                    command = 0;
                    break;
                case 7:
                    // Sort by phone
                    sort_by_phone(client_database, count);
                    printf("Sorting by phone number...\n");
                    printf("Clients now sorted by phone number\n");
                    command = 0;
                    break;
                case 8:
                    // Sort by email
                    sort_by_email(client_database, count);
                    printf("Sorting by email address...\n");
                    printf("Clients now sorted by email address\n");
                    command = 0;
                    break;
                case 9:
                    // Print all clients
                    for (int i = 0; i < count; i++) {
                        // Print each client with name
                        print_client_info(i, client_database);
                    }
                    command = 0;
                    break;
                case 10:
                    // Quit program
                    printf("Exiting program\n");
                    // Free memory before quitting program
                    free(val);
                    free(client_database);
                    exit(0);
                default:
                    // If input is invalid
                    fprintf(stderr, "Invalid input - Please enter a valid search command between 1 and 10\n");
                    command = 0;
                    break;
            }
        }
    }
}