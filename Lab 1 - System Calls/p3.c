#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
/**
 * Program that calls mycat.c executable and executes it in p3's child process.
 * Program for Task 3 in PDF.
 */
void main() {
	int p;
	printf("Welcome to task 3 program in PDF\n");
	p = fork();
	wait();
	if(p == 0 && p != -1) {
		printf("I am child process and my PID is : %d\n",getpid());
		char *args[] = {"./MYCAT","<","sample.txt",NULL};
		execvp(args[0], args);		
	} else {
		printf("I am parent process and my PID is : %d\n",getpid());
		printf("My parent PID is: %d\n",getppid());
	}
}
