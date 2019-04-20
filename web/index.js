window.onload = function() {
	var table = document.getElementById("sudoku");
	for (var i = 0; i < 9; i++) {
		table.innerHTML += "<tr id=\"row" + i + "\"></tr>";
		for (var j = 0; j < 9; j++) {
			var row = document.getElementById("row" + i);
			var input = '<input id="cell' + i + j + '" autocomplete="off" type="number" step="1" min="1" max="9"/>';
			row.innerHTML += "<td>" + input + "</td>";
		}
	}
}

function start() {
	var table = document.getElementById("sudoku");
	var grid = [];
	for (var i = 0; i < 9; i++) {
		var row = [];
		for (var j = 0; j < 9; j++) {
			row[j] = document.getElementById("cell" + i + j).value;
		}
		grid[i] = row;
	}

	if (!solve(grid))
		alert("Couldn't find a solution to this sudoku");
}

function solve(grid) {
	var next = nextField(grid);
	var i = next.i;
	var j = next.j;
	if (i == -1) {
		showSudoku(grid);
		return true;
	}

	for (var num = 1; num < 10; num++)
		if (checkField(grid, i, j, num)) {
			grid[i][j] = num;
			if (solve(grid))
				return true;
			grid[i][j] = 0;
		}

	return false;
}

function checkField(grid, i, j, num) {
	for (var xy = 0; xy < 9; xy++)
		if (num == grid[i][xy] || num == grid[xy][j])
			return false;
	var r = i - i % 3;
	var c = j - j % 3;
	for (var y = r; y < r + 3; y++)
		for (var x = c; x < c + 3; x++)
			if (num == grid[y][x])
				return false;

	return true;
}

function nextField(grid) {
	for (var i = 0; i < 9; i++)
		for (var j = 0; j < 9; j++)
			if (grid[i][j] == 0)
				return {i: i, j: j};

	return {i: -1, j: -1};
}

function showSudoku(grid) {
	for (var i = 0; i < 9; i++)
		for (var j = 0; j < 9; j++)
			document.getElementById("cell" + i + j).value = grid[i][j];
}
