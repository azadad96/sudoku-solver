def solve(grid):
    i, j = nextField(grid)
    if i == -1:
        printSudoku(grid)
        save = input("Do you want to save this in a file? (y/n): ")
        if save == "y":
            path = input("Enter the path to the file: ")
            f = open(path, "w")
            for i in range(9):
                for j in range(9):
                    f.write(str(grid[i][j]))
                f.write("\n")
            f.close()
        return True
    
    for num in range(1, 10):
        if checkField(grid, i, j, num):
            grid[i][j] = num
            if solve(grid):
                return True
            grid[i][j] = 0
    
    return False

def checkField(grid, i, j, num):
    row = all(num != grid[i][x] for x in range(9))
    col = all(num != grid[y][j] for y in range(9))
    r = i - i % 3
    c = j - j % 3
    for y in range(r, r + 3):
        for x in range(c, c + 3):
            if grid[y][x] == num:
                return False
    return row and col

def nextField(grid):
    for i in range(9):
        for j in range(9):
            if grid[i][j] == 0:
                return (i, j)
    return (-1, -1)

def printSudoku(grid):
    print("-" * 19)
    for i in range(9):
        print("|", end="")
        for j in range(9):
            print(grid[i][j], end="")
            print("|" if j % 3 == 2 else " ", end="")
        print(("\n" + 19 * "-") if i % 3 == 2 else "")

grid = []
path = input("Path to file with sudoku: ")
f = open(path, "r")
for line in f:
    line = line[:-1]
    row = []
    for i in line:
        row.append(int(i))
    grid.append(row)
f.close()

if not solve(grid):
    print("Coudn't find a solution to this sudoku")
