#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
/**
 * Program for parent process to open a file and fork a child and parent and child can both write to file.
 * Program for task 4a in PDF.
 */
void main(int argc, char *argv[]) {
	int p;
	FILE *fptr;
	printf("Welcome to task 4a in PDF.\n");
	if(argc == 1) {
		printf("File name is not passed in the command line arguments\n");
	} else {
		fptr = fopen(argv[1], "w");
		printf("IN parent: File opened.\n");
		if(fptr == NULL) {
			printf("Error! File cannot be opened.\n");
			exit(1);
		}
		p = fork();
		wait();
		if(p == 0 && p != -1) {
			printf("IN child: PID = %d\n",getpid());
			printf("IN child: Writing to file.\n");
			fprintf(fptr,"%s","Hello World! I am child!");
			printf("IN child: Write complete\n");
		} else {
			printf("IN parent: PID = %d\n",getpid());
			printf("IN parent: Writing to file.\n");
			fprintf(fptr,"%s","\nHello World! I am parent!");
			printf("IN parent: Write complete\n");
			printf("IN parent: Closing file\n");
			fclose(fptr);
		}
	}
}
