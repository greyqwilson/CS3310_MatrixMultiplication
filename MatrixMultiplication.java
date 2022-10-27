public class MatrixMultiplication {
	
	public static void printMatrix(int[][] M) {
		int n = M.length;
		for (int row=0; row<n; row++) {
			for (int col=0; col<n; col++) {
				System.out.print(M[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println("--------");
	}
	
	public static void regularMM(int n, int[][] A, int[][] B) {
		int[][] AB = new int[n][n];
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					System.out.println("Multiplying " + A[i][k] + "*" + B[k][j]);
					int sum = A[i][k] * B[k][j];
					System.out.println("Adding " + sum + " to " + AB[i][j]);
					AB[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		for (int row=0; row<n; row++) {
			for (int col=0; col<n; col++) {
				System.out.print(AB[row][col] + " ");
			}
			System.out.println();
		}
	}
	
	public static int dacMM(int n, int[][] A, int[][] B) {
		int[][] AB = new int[n][n];
		//3x3 or oddxodd matrix all you do is add an additional row and column
		if (n%2 != 0) {
			int[][] resizedA = new int[n+1][n+1];
			int[][] resizedB = new int[n+1][n+1];
			
			int i=0;
			for (int[] row : A) {
				java.lang.System.arraycopy(row, 0, resizedA[i], 0, n);
				i++;
			}
			i=0;
			for (int[] row : B) {
				java.lang.System.arraycopy(row, 0, resizedB[i], 0, n);
				i++;
			}
			A = resizedA;
			B = resizedB;
			n += 1;
		}
		
		//Base case that matrix is 1x1
		if (A.length == 1 && B.length == 1) {
			//regularMM(n, A, B);
			return (A[0][0] * B[0][0]);
		}
		//Split matrix into 4 submatrices
		int[][] AUL = new int[n/2][n/2]; //A Upper left
		int[][] AUR = new int[n/2][n/2]; //A Upper right
		int[][] ABL = new int[n/2][n/2]; //A Bottom left
		int[][] ABR = new int[n/2][n/2]; //A Bottom right
		int[][] BUL = new int[n/2][n/2]; //B Upper left
		int[][] BUR = new int[n/2][n/2]; //B Upper right
		int[][] BBL = new int[n/2][n/2]; //B Bottom left
		int[][] BBR = new int[n/2][n/2]; //B Bottom right
		
		//Assign values to each submatrix
		//Might be able to combine for loops with matching starting/ending rows or cols
		//Upper left
		for (int row=0; row<n/2; row++) {
			for(int col=0; col<n/2; col++) {
				AUL[row][col] = A[row][col];
				BUL[row][col] = B[row][col];
			}
		}
		//Upper right
		int i=0;
		int j=0;
		for (int row=0; row<n/2; row++) {
			for (int col=n/2; col<n; col++) {
				//A and B need their rows different
				AUR[i][j] = A[row][col];
				BUR[i][j] = B[row][col];
				j++;
			}
			j=0;
			i++;
		}
		//Bottom left
		i=0;
		j=0;
		for (int row=n/2; row<n; row++) {
			for (int col=0; col<n/2; col++) {
				ABL[i][j] = A[row][col];
				BBL[i][j] = B[row][col];
				j++;
			}
			j=0;
			i++;
		}
		//Bottom right
		i=0;
		j=0;
		for (int row=n/2; row<n; row++) {
			for (int col=n/2; col<n; col++) {
				ABR[i][j] = A[row][col];
				BBR[i][j] = B[row][col];
				j++;
			}
			j=0;
			i++;
		}
		
		//Then call each split array
		System.out.println("AUL");
		printMatrix(AUL);
		System.out.println("BUL");
		printMatrix(BUL);
		System.out.println("AUR");
		printMatrix(AUR);
		System.out.println("BUR");
		printMatrix(BUR);
		System.out.println("ABL");
		printMatrix(ABL);
		System.out.println("BBL");
		printMatrix(BBL);
		System.out.println("ABR");
		printMatrix(ABR);
		System.out.println("BBR");
		printMatrix(BBR);
		dacMM(n/2, AUL, BUL);
		dacMM(n/2, AUR, BUR);
		dacMM(n/2, ABL, BBL);
		dacMM(n/2, ABR, BBR);
	}
	
	public static int strassMM(int n, int[][] A, int[][] B) {
		int[][] AB = new int[n][n];
		//Check for odd-dimension square matrix (like 3x3). all you do is add an additional row and column
		if (n%2 != 0) {
			int[][] resizedA = new int[n+1][n+1];
			int[][] resizedB = new int[n+1][n+1];
			
			int i=0;
			for (int[] row : A) {
				java.lang.System.arraycopy(row, 0, resizedA[i], 0, n);
				i++;
			}
			i=0;
			for (int[] row : B) {
				java.lang.System.arraycopy(row, 0, resizedB[i], 0, n);
				i++;
			}
			A = resizedA;
			B = resizedB;
			n += 1;
		}
		
		//Base case that matrix is 1x1
		if (A.length == 1 && B.length == 1) {
			//regularMM(n, A, B); //If base case is 2x2
			return (A[0][0] * B[0][0]);
		}
		
		
		//Split matrix into 4 submatrices
		int[][] AUL = new int[n/2][n/2]; //A Upper left
		int[][] AUR = new int[n/2][n/2]; //A Upper right
		int[][] ABL = new int[n/2][n/2]; //A Bottom left
		int[][] ABR = new int[n/2][n/2]; //A Bottom right
		int[][] BUL = new int[n/2][n/2]; //B Upper left
		int[][] BUR = new int[n/2][n/2]; //B Upper right
		int[][] BBL = new int[n/2][n/2]; //B Bottom left
		int[][] BBR = new int[n/2][n/2]; //B Bottom right
		
		//Assign values to each submatrix
		//Might be able to combine for loops with matching starting/ending rows or cols
		//Upper left
		for (int row=0; row<n/2; row++) {
			for(int col=0; col<n/2; col++) {
				AUL[row][col] = A[row][col];
				BUL[row][col] = B[row][col];
			}
		}
		//Upper right
		int i=0;
		int j=0;
		for (int row=0; row<n/2; row++) {
			for (int col=n/2; col<n; col++) {
				//A and B need their rows different
				AUR[i][j] = A[row][col];
				BUR[i][j] = B[row][col];
				j++;
			}
			j=0;
			i++;
		}
		//Bottom left
		i=0;
		j=0;
		for (int row=n/2; row<n; row++) {
			for (int col=0; col<n/2; col++) {
				ABL[i][j] = A[row][col];
				BBL[i][j] = B[row][col];
				j++;
			}
			j=0;
			i++;
		}
		//Bottom right
		i=0;
		j=0;
		for (int row=n/2; row<n; row++) {
			for (int col=n/2; col<n; col++) {
				ABR[i][j] = A[row][col];
				BBR[i][j] = B[row][col];
				j++;
			}
			j=0;
			i++;
		}
		
		//Get m1, m2, m3, m4, m5, m6, m7
		int[][] M1 = AUL;
		int[][] M2;
		int[][] M3;
		int[][] M4;
		int[][] M5;
		int[][] M6;
		int[][] M7;		
				
				
		//Then call each split array
		System.out.println("AUL");
		printMatrix(AUL);
		System.out.println("BUL");
		printMatrix(BUL);
		System.out.println("AUR");
		printMatrix(AUR);
		System.out.println("BUR");
		printMatrix(BUR);
		System.out.println("ABL");
		printMatrix(ABL);
		System.out.println("BBL");
		printMatrix(BBL);
		System.out.println("ABR");
		printMatrix(ABR);
		System.out.println("BBR");
		printMatrix(BBR);
		dacMM(n/2, AUL, BUL);
		dacMM(n/2, AUR, BUR);
		dacMM(n/2, ABL, BBL);
		dacMM(n/2, ABR, BBR);
	}
	
	public static void main(String[] args) {
		int[][] matA = { {3, 1,}, {1, 2} };
		int[][] matB = { {1, 2}, {2, 4} };
		
		int[][] fbfA = {{1, 1, 2, 2}, {2, 2, 1, 1}, {3, 3, 4, 4}, {4, 4, 3, 3} };
		int[][] fbfB = {{4, 4, 3, 3}, {3, 3, 4, 4}, {2, 2, 1, 1}, {1, 1, 2, 2} };
		
		int[][] tbtA = {{1, 2, 2}, {2, 1, 1}, {3, 3, 3} };
		int[][] tbtB = {{3, 3, 3}, {1, 1, 2}, {2, 2, 1} };
		
		//regularMM(2, matA, matB);
		//dacMM(4, fbfA, fbfB);
		dacMM(3, tbtA, tbtB);
		
	}

}
