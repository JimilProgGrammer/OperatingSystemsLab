#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
int pid, j;
int main(int argc, char *argv[]) {
	int i,n,current_pid;
	n = atoi(argv[1]);
	for(i=0, j=n; i<n; i++,j--) {
		pid = fork();
		if(pid == -1) {
			printf("Fork failed.\n");
		} else if(pid == 0) {
			if (i==0) {
				printf("Parent is: %d\n", getppid());
				printf("Number of children: %d\n", n);
			}
			printf("Child %d is created. Parent (%d). Sleep %d\n", getpid(),getppid(), j);
			sleep(j);
			return getpid();
		}
	}
	// Need to wait for all
	for(i=0; i<n; i++) {
		int a = wait(NULL);
		printf("Child %d exited\n", a) ;
	}
	printf("Parent exited.\n");
	return 0;
}
