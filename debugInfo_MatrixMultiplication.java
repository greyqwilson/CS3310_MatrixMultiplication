//This is a verbose debug logged version of the program

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
	
	public static int[][] regularMM(int n, int[][] A, int[][] B) {
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
		//Print matrix
		printMatrix(A);
		System.out.println("x");
		printMatrix(B);
		System.out.println("=");
		printMatrix(AB);
		return AB;
	}
	
	public static int[][] dacMM(int n, int[][] A, int[][] B) {
		int[][] AB = new int[n][n];
		//3x3 or oddxodd matrix all you do is add an additional row and column
		//ONLY IF ABOVE 2x2!
		if (n%2 != 0 && n!= 1) {
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
			AB[0][0] = (A[0][0] * B[0][0]);
			return AB;
		}
		//Split matrix into 4 submatrices (Divide)
		else {
			
			int[][] AUL = new int[n/2][n/2]; //A Upper left
			int[][] AUR = new int[n/2][n/2]; //A Upper right
			int[][] ABL = new int[n/2][n/2]; //A Bottom left
			int[][] ABR = new int[n/2][n/2]; //A Bottom right
			int[][] BUL = new int[n/2][n/2]; //B Upper left
			int[][] BUR = new int[n/2][n/2]; //B Upper right
			int[][] BBL = new int[n/2][n/2]; //B Bottom left
			int[][] BBR = new int[n/2][n/2]; //B Bottom right
			System.out.println("Splitting " + n + "x" + n + " matrix into " + n/2 + "x" + n/2);
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
			
			//Delegate work to recursive calls
			System.out.println("Delegating work for " + n + "x" + n + " matrix...");
			
			int[][] CUL = AddSquareMatrix(dacMM(n/2, AUL, BUL), dacMM(n/2, AUR, BBL));
			int[][] CUR = AddSquareMatrix(dacMM(n/2, AUL, BUR), dacMM(n/2, AUR, BBR));
			int[][] CBL = AddSquareMatrix(dacMM(n/2, ABL, BUL), dacMM(n/2, ABR, BBL));
			int[][] CBR = AddSquareMatrix(dacMM(n/2, ABL, BUR), dacMM(n/2, ABR, BBR));
			
			//Combine all of their work
			System.out.println("Combining returned arrays from recursive calls");
			int[][] C = new int[n][n];
			
			//Fill in C's quadrants
			int c_row = 0;
			int c_col = 0;
			//Copy into Upper Left of C
			//c_col flips between 1 and 0 for CUL, CBL
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CUL[row][col];					
					c_col++;
				}
				c_col = 0;
				c_row++;
			}

			//Copy into Bottom Left of C
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CBL[row][col];
					c_col++;
				}
				c_col = 0;
				c_row++;
			}

			//Copy into Upper Right of C
			//c_col flips between 3 and 2 for CUR, CBR
			c_row = 0;
			c_col = n/2; //This is a very awful way to loop through 
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CUR[row][col];
					c_col++;
				}
				c_col = n/2;
				c_row++;
			}
			//Copy into Bottom Right of C
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CBR[row][col];
					c_col++;
				}
				c_col = n/2;
				c_row++;
			}
			System.out.println("Returning!");
			return C;
		}
	}
	
	public static int[][] strassMM(int n, int[][] A, int[][] B) {
		
		
		//Base case that matrix is 2x2 or 1x1
		if (A.length <= 2 && B.length <= 2) {
			System.out.println("Base case reached");
			return regularMM(n, A, B); //If base case is 2x2
		}
		
		else {
			
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
				System.out.println("Resizing matrix from " + n + "x" + n + " to " + (n+1) + "x" + (n+1) );
				n += 1;
				//printMatrix(A); //---------------DEBUG
				//printMatrix(B);
			}
			
			int[][] C = new int[n][n];
			
			//Divide matrices		
			//Make 4 submatrices for A and B
			System.out.println("Still too big to solve easily. Splitting A and B into submatrices");
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
			//System.out.println("Creating m1 thru m7");
			int[][] M1 = regularMM(n/2, AddSquareMatrix(AUL, ABR), AddSquareMatrix(BUL, BBR)); //(a11+a22)(b11+b22)
			System.out.println("M1");
			printMatrix(M1);
			int[][] M2 = regularMM(n/2, AddSquareMatrix(ABL, ABR), BUL);					   //(a21+a22)b11
			System.out.println("M2");
			printMatrix(M2);
			int[][] M3 = regularMM(n/2, AUL, SubSquareMatrix(BUR, BBR));					   //a11(b12-b22)
			System.out.println("M3");
			printMatrix(M3);
			int[][] M4 = regularMM(n/2, ABR, SubSquareMatrix(BBL, BUL));					   //a22(b21-b11)
			System.out.println("M4");
			printMatrix(M4);
			int[][] M5 = regularMM(n/2, AddSquareMatrix(AUL, AUR), BBR);                       //(a11+a12)b22
			System.out.println("M5");
			printMatrix(M5);
			int[][] M6 = regularMM(n/2,	SubSquareMatrix(ABL, AUL), AddSquareMatrix(BUL, BUR)); //(a21-a11)(b11+b12)
			System.out.println("M6");
			printMatrix(M6);
			int[][] M7 = regularMM(n/2, SubSquareMatrix(AUR, ABR), AddSquareMatrix(BBL, BBR)); //(a12-a22)(b21+b22)
			System.out.println("M7");
			printMatrix(M7);
			//Throw computed intermediate values into C to be 
			//[C11,C12] Where C could be some 2x2, 4x4, or any other even length square matrix
			//[C21,C22]
			System.out.println("Making " + n + "x" + n + " C matrix");
			int[][] CUL = AddSquareMatrix(SubSquareMatrix(M1, M5), AddSquareMatrix(M4, M7));
			System.out.println("CUL");
			printMatrix(CUL);
			int[][] CUR = AddSquareMatrix(M3, M5);
			System.out.println("CUR");
			printMatrix(CUR);
			int[][] CBL = AddSquareMatrix(M2, M4);
			System.out.println("CBL");
			printMatrix(CBL);
			int[][] CBR = AddSquareMatrix(SubSquareMatrix(M1, M2), AddSquareMatrix(M3, M6));
			System.out.println("CBR");
			printMatrix(CBR);
			//Copy C submatrices into C
			int c_row = 0;
			int c_col = 0;
			//Copy into Upper Left of C
			//c_col flips between 1 and 0 for CUL, CBL
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CUL[row][col];					
					c_col++;
				}
				c_col = 0;
				c_row++;
			}
			//Copy into Bottom Left of C
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CBL[row][col];
					c_col++;
				}
				c_col = 0;
				c_row++;
			}
			//Copy into Upper Right of C
			//c_col flips between 3 and 2 for CUR, CBR
			c_row = 0;
			c_col = n/2; //This is a very awful way to loop through 
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CUR[row][col];
					c_col++;
				}
				c_col = n/2;
				c_row++;
			}
			//Copy into Bottom Right of C
			for (int row=0; row<n/2; row++) {
				for (int col=0; col<n/2; col++) {
					C[c_row][c_col] = CBR[row][col];
					c_col++;
				}
				c_col = n/2;
				c_row++;
			}
			
			System.out.println("Returning C");
			return C;
		}
	}
	
	public static int[][] AddSquareMatrix(int[][] A, int[][] B){
		//Only supports square matrices of same size
		if (A.length != B.length)
			return null;
		if (A[0].length != B[0].length)
			return null;
		int n = A.length;
		
		int[][] C = new int[n][n];
		for (int row=0; row < n; row++) {
			for (int col=0; col < n; col++) {
				C[row][col] = A[row][col] + B[row][col];
			}
		}
		printMatrix(A);
		System.out.println(" + ");
		printMatrix(B);
		System.out.println(" = ");
		printMatrix(C);
		return C;
	}
	
	public static int[][] SubSquareMatrix(int[][] A, int[][] B){
		//Only supports square matrices of same size
				if (A.length != B.length)
					return null;
				if (A[0].length != B[0].length)
					return null;
				int n = A.length;
				
				int[][] C = new int[n][n];
				for (int row=0; row < n; row++) {
					for (int col=0; col < n; col++) {
						C[row][col] = A[row][col] - B[row][col];
					}
				}
				printMatrix(A);
				System.out.println(" - ");
				printMatrix(B);
				System.out.println(" = ");
				printMatrix(C);
				return C;
	}
	
	public static void main(String[] args) {
		int[][] matA = { {3, 1,}, {1, 2} };
		int[][] matB = { {1, 2}, {2, 4} };
		
		int[][] fbfA = {{1, 1, 2, 2}, {2, 2, 1, 1}, {3, 3, 4, 4}, {4, 4, 3, 3} };
		int[][] fbfB = {{4, 4, 3, 3}, {3, 3, 4, 4}, {2, 2, 1, 1}, {1, 1, 2, 2} };
		
		int[][] tbtA = {{1, 2, 2}, {2, 1, 1}, {3, 3, 3} };
		int[][] tbtB = {{3, 3, 3}, {1, 1, 2}, {2, 2, 1} };
		
		System.out.println("Performing regular matrix multiplication on 3x3");
		printMatrix(regularMM(3, tbtA, tbtB));
		System.out.println("Performing regular matrix multiplication on 4x4");
		printMatrix(regularMM(4, fbfA, fbfB));
		
		System.out.println("Performing Divide and Conquer matrix multiplication on 3x3");
		printMatrix(dacMM(3, tbtA, tbtB));
		System.out.println("Performing Divide and Conquer matrix multiplication on 4x4");
		printMatrix(dacMM(4, fbfA, fbfB));
		
		System.out.println("Performing Strassen's matrix multiplication algorithm on 3x3");
		printMatrix(strassMM(3, tbtA, tbtB));
		System.out.println("Performing Strassen's matrix multiplication algorithm on 4x4");
		printMatrix(strassMM(4, fbfA, fbfB));
		
	}

}
