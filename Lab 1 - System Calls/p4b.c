#include<stdio.h>
#include<fcntl.h>
#include<errno.h>
#include<string.h>
#include<unistd.h>
#include<sys/types.h>
/**
 * Program for parent process to open a file and fork a child and child prints contents of file and executes mycat program
 * Program for task 4b in PDF.
 */
int fd, p;
char content[2000];
void main(int argc, char *argv[]) {	
	printf("Hello from task 4b in PDF\n");
	if(argc == 1) {
		printf("Please pass file name as command line argument.\n");
		exit(1);
	} else {
		fd = open(argv[1], O_RDONLY | O_CREAT | O_TRUNC);
		if(fd == -1) {
			printf("Error! In opening file\n");
			exit(0);
		}
		dup2(fd, 0); // duplicate in stdin
		close(fd);
		p = fork();
		if (p > 0) {
			printf("Parent: File opened. fd = %d\n", 0);
		} else {
			printf("Child: Execute mycat to stdout file descriptor stored.\n");
			char *args[]={"./MYCAT", NULL};
			execlp(args[0],args);
		}
	}
}
