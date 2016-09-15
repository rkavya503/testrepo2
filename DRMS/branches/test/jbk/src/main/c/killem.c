#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
  int result;
  char die[BUFSIZ];

  int x;
  

  for ( x=1; x<argc; x++) {
	sprintf(die, "kill -9 %s\n", argv[x]);
	printf("%s\n", die);
	setuid(0);
	printf("uid %d\n", getuid());
	system(die);
  }
  return 0;

}

