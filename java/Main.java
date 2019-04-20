import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		int grid[][] = new int[9][9];
		System.out.print("Path to file with sudoku: ");
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		
		try {
			File f = new File(path);
			sc = new Scanner(f);
			int i = 0;
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				for (int j = 0; j < 9; j++)
					grid[i][j] = Character.getNumericValue(line.charAt(j));
				i++;
			}
		} catch(Exception e) {
			System.err.println("Couldn't open the file");
		}
		
		if (!solve(grid))
			System.err.println("Couldn't find a solution to this sudoku");
		
		sc.close();
	}

	public static boolean solve(int grid[][]) {
		int next[] = nextField(grid);
		int i = next[0];
		int j = next[1];
		if  (i == -1) {
			printSudoku(grid);
			System.out.print("Do you want to save this in a file? (y/n): ");
			char save;
			Scanner sc = new Scanner(System.in);
			save = sc.next().charAt(0);
			if (save == 'y') {
				System.out.print("Enter the path to the file: ");
				sc = new Scanner(System.in);
				String path = sc.nextLine();
				
				try {
					FileOutputStream fo = new FileOutputStream(path);
					for (int y = 0; y < 9; y++) {
						for (int x = 0; x < 9; x++)
							fo.write(Integer.toString(grid[y][x]).getBytes());
						fo.write("\n".getBytes());
					}
					fo.close();
				} catch(Exception e) {
					System.err.println("Couldn't open the file");
				}
			}
			sc.close();

			return true;
		}

		for (int num = 1; num < 10; num++)
			if (checkField(grid, i, j, num)) {
				grid[i][j] = num;
				if (solve(grid))
					return true;
				grid[i][j] = 0;
			}

		return false;
	}

	public static boolean checkField(int grid[][], int i, int j, int num) {
		for (int xy = 0; xy < 9; xy++)
			if (num == grid[i][xy] || num == grid[xy][j])
				return false;
		int r = i - i % 3;
		int c = j - j % 3;
		for (int y = r; y < r + 3; y++)
			for (int x = c; x < c + 3; x++)
				if (grid[y][x] == num)
					return false;

		return true;
	}

	public static int[] nextField(int grid[][]) {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (grid[i][j] == 0)
					return new int[] {i, j};

		return new int[] {-1, -1};
	}

	public static void printSudoku(int grid[][]) {
		System.out.println("-------------------");
		for (int i = 0; i < 9; i++) {
			System.out.print("|");
			for (int j = 0; j < 9; j++) {
				System.out.print(grid[i][j]);
				System.out.print(j % 3 == 2 ? "|" : " ");
			}
			System.out.println(i % 3 == 2 ? "\n-------------------" : "");
		}
	}
}
