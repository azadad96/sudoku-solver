#include <stdio.h>

int solve(int grid[9][9]);
int checkField(int grid[9][9], int i, int j, int num);
unsigned char nextField(int grid[9][9]);
void printSudoku(int grid[9][9]);

int main() {
	int grid[9][9] = {0};
	printf("Path to file with sudoku: ");
	char path[1024] = {0};
	scanf("%1023s", path);
	FILE *f = fopen(path, "r");
	if (f == NULL) {
		printf("Couldn't open file\n");
		return -1;
	}
	
	for (int i = 0; i < 9; i++) {
		char line[9];
		fscanf(f, "%9s", line);
		for (int j = 0; j < 9; j++) {
			grid[i][j] = line[j] - '0';
		}
	}
	fclose(f);
	
	if (!solve(grid))
		printf("Couldn't find a solution to this sudoku\n");

	return 0;
}

int solve(int grid[9][9]) {
	unsigned char next = nextField(grid);
	if (next == 0xFF) {
		printSudoku(grid);
		printf("Do you want to save this in a file? (y/n): ");
		char save;
		scanf("%1s", &save);
		if (save == 'y') {
			printf("Enter the path to the file: ");
			char path[1024] = {0};
			scanf("%1023s", path);
			FILE *f = fopen(path, "w");
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++)
					fprintf(f, "%i", grid[i][j]);
				fprintf(f, "\n");
			}
			fclose(f);
		}
		return 1;
	}
	int i = (next & 0xF0) >> 4;
	int j = next & 0x0F;
	
	for (int num = 1; num < 10; num++)
		if (checkField(grid, i, j, num)) {
			grid[i][j] = num;
			if (solve(grid))
				return 1;
			grid[i][j] = 0;
		}

	return 0;
}

int checkField(int grid[9][9], int i, int j, int num) {
	for (int xy = 0; xy < 9; xy++)
		if (num == grid[i][xy] || num == grid[xy][j])
			return 0;
	
	int r = i - i % 3;
	int c = j - j % 3;
	for (int y = r; y < r + 3; y++)
		for (int x = c; x < c + 3; x++)
			if (num == grid[y][x])
				return 0;

	return 1;
}

unsigned char nextField(int grid[9][9]) {
	for (int i = 0; i < 9; i++)
		for (int j = 0; j < 9; j++)
			if (grid[i][j] == 0) {
				// I store i in the first four bits and j in the last for bits, this way I can return 2 numbers
				char next = i;
				next = next << 4;
				next += j;
				return next;
			}
	return 0xFF;
}

void printSudoku(int grid[9][9]) {
	printf("-------------------\n");
	for (int i = 0; i < 9; i++) {
		printf("|");
		for (int j = 0; j < 9; j++)
			printf("%i%c", grid[i][j], (j % 3 == 2 ? '|' : ' '));
		printf(i % 3 == 2 ? "\n-------------------\n" : "\n");
	}
}
