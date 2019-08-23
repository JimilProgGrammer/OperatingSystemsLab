#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
/**
 * C program to demonstrate the zombie process.
 */

void main() {
	int p;
	int random;
	/**
	 * fork() spuns child processes --> all the processes are in main memory.
	 * Used for non-blocking calls.
	 *
	 * p = 0 : Child/leaf process --> Reached the end of the inheritance tree
	 * p = -1: Unsuccessfull fork()
	 * p > 0 : PID of child returned to parent process
	 */
	p = fork();
	if(p == 0 && p != -1) {
		printf("I'm child and my PID is: %d\n",getpid());
		printf("I'm child and my PPID is: %d\n",getppid());
		printf("Enter some random integer: \n");
		scanf("%d",&random);
		printf("\nYou entered: %d\n",random);
		exit(0);
	} else if(p > 0 && p != -1) {
		sleep(20);
		printf("I'm parent and my PID is: %d\n",getpid());
		printf("I'm parent and my PPID is: %d\n",getppid());		
	}
}
