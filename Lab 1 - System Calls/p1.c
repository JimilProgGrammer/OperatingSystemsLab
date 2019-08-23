#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
/**
 * Program 1: Demonstrating fork(), wait(), getpid(), getppid(), sleep() system calls 
 * Program for Task 1 from PDF.
 */

void main() {
	int p;
	printf("Hello, World!\n");
	/**
	 * fork() spuns child processes --> all the processes are in main memory.
	 * Used for non-blocking calls.
	 *
	 * p = 0 : Child/leaf process --> Reached the end of the inheritance tree
	 * p = -1: Unsuccessfull fork()
	 * p > 0 : PID of child returned to parent process
	 */
	p = fork();
	/**
	 * forces parent process to wait; lets child process to be executed first
	 */
	//wait();
	printf("Welcome to Batch E, %d\n", p);
	/**
	 * getpid(): returns PID of current process
	 * getppid(): returns the PID of the parent process, that forked this child, if the parent process stil
	 *            exists at the time of the call, otherwise returns 1 --> PID of init process.
	 */
	if(p == 0 && p != -1) {
		printf("I'm child and my PID is: %d\n",getpid());
		printf("I'm child and my PPID is: %d\n",getppid());
		//printf("I'm child and I'm going to halt my execution for 15 seconds.\n");
		/**
		 * sleep(unsigned int seconds) 
		 */
		//sleep(15);
	} else if(p > 0 && p != -1) {
		printf("I'm parent and my PID is: %d\n",getpid());
		printf("I'm parent and my PPID is: %d\n",getppid());
	}
}
