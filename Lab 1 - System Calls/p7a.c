#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
/**
 * A program that spuns n child processes recursively.
 * Program for task 7A in PDF.
 */
 
void main(int argc, char *argv[]) {
	int p = 0;
	int i = 0;
	printf("Hello to task 7A in PDF.\n");
	int parentId = getpid();
	printf("Process : PID = %d; PPID = %d --> created\n",getpid(),getppid());
	if(argc == 1) {
		printf("Value of n is not passed.\n");
		exit(1);
	} else {
		int n = atoi(argv[1]);
		for(i = 1; i <= n; i++) {
			p = fork();
			wait(0);
			if(p == 0 && p != -1) {
				printf("Process : PID = %d; PPID = %d --> created\n",getpid(),getppid());
				//p = fork();
				//wait(0);
			} else {
				printf("Process : PID = %d; PPID = %d --> exited\n",getpid(),getppid());
				exit(0);
			}
		}
		printf("Process : PID = %d; PPID = %d --> exited\n",getpid(),getppid());
	}
}
