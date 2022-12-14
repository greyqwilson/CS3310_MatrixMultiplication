//Author: Greyson Wilson
//For CS3310 Analysis and Design of Algorithms (Fall 2022)
//Professor Young
//Project 1
//The purpose of this program is to experimentally find out the time complexity of
//three different matrix multiplication methods:
//Regular matrix multiplication (GEMM), Divide-and-conquer, and Strassen's algorithm
//Gets average of sample performance of matrices of size nxn where n is a power of 2
//for x number of randomly generated matrices. Algorithm performance is timed and
//saved to file for analysis.

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixMultiplication {
	
	public static void printMatrix(int[][] M) {
		int n = M.length;
		for (int row=0; row<n; row++) {
			for (int col=0; col<n; col++) {
				System.out.printf("% 3d", M[row][col]);
			}
			System.out.println("");
		}
		System.out.println("--------");
	}
	
	public static int[][] regularMM(int n, int[][] A, int[][] B) {

		
		int[][] AB = new int[n][n];	
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					//System.out.println("Multiplying " + A[i][k] + "*" + B[k][j]);
					int sum = A[i][k] * B[k][j];
					//System.out.println("Adding " + sum + " to " + AB[i][j]);
					AB[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		
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
			
			
			//Delegate work to recursive calls
			
			int[][] CUL = AddSquareMatrix(dacMM(n/2, AUL, BUL), dacMM(n/2, AUR, BBL));
			int[][] CUR = AddSquareMatrix(dacMM(n/2, AUL, BUR), dacMM(n/2, AUR, BBR));
			int[][] CBL = AddSquareMatrix(dacMM(n/2, ABL, BUL), dacMM(n/2, ABR, BBL));
			int[][] CBR = AddSquareMatrix(dacMM(n/2, ABL, BUR), dacMM(n/2, ABR, BBR));
			
			//Combine all of their work
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
			//System.out.println("Finished filling in CBL " + c_row);
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
			
			return C;
		}
	}
	
	public static int[][] strassMM(int n, int[][] A, int[][] B) {

		
		//Base case that matrix is 1x1
		if (A.length == 1 && B.length == 1) {
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
				n += 1;

			}
			
			int[][] C = new int[n][n];
			
			//Divide matrices		
			//Make 4 submatrices for A and B
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
			int[][] M1 = strassMM(n/2, AddSquareMatrix(AUL, ABR), AddSquareMatrix(BUL, BBR)); //(a11+a22)(b11+b22)
			int[][] M2 = strassMM(n/2, AddSquareMatrix(ABL, ABR), BUL);					   //(a21+a22)b11
			int[][] M3 = strassMM(n/2, AUL, SubSquareMatrix(BUR, BBR));					   //a11(b12-b22)
			int[][] M4 = strassMM(n/2, ABR, SubSquareMatrix(BBL, BUL));					   //a22(b21-b11)
			int[][] M5 = strassMM(n/2, AddSquareMatrix(AUL, AUR), BBR);                       //(a11+a12)b22
			int[][] M6 = strassMM(n/2,	SubSquareMatrix(ABL, AUL), AddSquareMatrix(BUL, BUR)); //(a21-a11)(b11+b12)
			int[][] M7 = strassMM(n/2, SubSquareMatrix(AUR, ABR), AddSquareMatrix(BBL, BBR)); //(a12-a22)(b21+b22)

			//Throw computed intermediate values into C to be 
			//[C11,C12] Where C could be some 2x2, 4x4, or any other even length square matrix
			//[C21,C22]
			int[][] CUL = AddSquareMatrix(SubSquareMatrix(M1, M5), AddSquareMatrix(M4, M7));
			int[][] CUR = AddSquareMatrix(M3, M5);
			int[][] CBL = AddSquareMatrix(M2, M4);
			int[][] CBR = AddSquareMatrix(SubSquareMatrix(M1, M2), AddSquareMatrix(M3, M6));

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

				return C;
	}
	
	static int[][] generateRandomMatrix(int n) {
		int[][] M = new int[n][n];
		for (int row=0; row<n; row++) {
			for (int col=0; col<n; col++) {
				M[row][col] = ThreadLocalRandom.current().nextInt(1, 100);
			}
		}
		return M;
	}
	
	static long[][] performTests(int n, int avgOf, int numTests) {
		//Returns a (numTests)x3 matrix of each test time {{avgOfReg0, avgOfDAC0, avgOfStrass0}, {...}, ...} 
		long[][] testTimes = new long[numTests][3];
		for (int testNum=0; testNum < numTests; testNum++) {
			
			//Generate nxn matrix
			int[][] matA = generateRandomMatrix(n);
			int[][] matB = generateRandomMatrix(n);
			
			//Get average of regular matrix multiplication
			long avgOfReg = 0;
			for (int regTestNum=0; regTestNum < avgOf; regTestNum++) {
				//TIMER START
				long startTime = System.nanoTime();
				
				regularMM(n, matA, matB);
				
				//TIMER END
				long execTime = System.nanoTime() - startTime;
				avgOfReg += execTime;
			}
			avgOfReg = avgOfReg/avgOf;

			//Get average of divide and conquer matrix multiplication
			long avgOfDAC = 0;
			for (int dacTestNum=0; dacTestNum < avgOf; dacTestNum++) {
				//TIMER START
				long startTime = System.nanoTime();
				
				dacMM(n, matA, matB);
				
				//TIMER END
				long execTime = System.nanoTime() - startTime;
				avgOfDAC += execTime;
			}
			avgOfDAC = avgOfDAC/avgOf;

			//Get average of strassen matrix multiplication
			long avgOfStrass = 0;
			for (int strassTestNum=0; strassTestNum < numTests; strassTestNum++) {
				//TIMER START
				long startTime = System.nanoTime();
				
				strassMM(n, matA, matB);
				
				//TIMER END
				long execTime = System.nanoTime() - startTime;
				avgOfStrass += execTime;
			}
			avgOfStrass = avgOfStrass/avgOf;

			testTimes[testNum][0] = avgOfReg;
			testTimes[testNum][1] = avgOfDAC;
			testTimes[testNum][2] = avgOfStrass;
		}
		return testTimes;
	}
	
	static void SaveResultsFile(long[][] testTimes, int n, int iterations) {
		//testTimes is the (iterations)x3 array used to save time results. n is the size of the matrix to label the file
		String fileName = new String(n + "x" + n + "_" + iterations + "its_results.csv");
		java.io.File file = new java.io.File(fileName);
		try {
			if (file.createNewFile()) {
				System.out.println(fileName + " successfully made!");
				FileWriter fileWrite = new FileWriter(file);
				
				for (long[] row : testTimes) {
					//File out here
					fileWrite.write(row[0] + "," + row[1] + "," + row[2] + "\n");
				}
				fileWrite.close();
				System.out.println(fileName + " finished writing.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong creating " + fileName);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		int n = 2;
		int powerOfN = 2;
		int avgOf = 20;
		int iterations = 1000;
		while (powerOfN <= 256) {
			long[][] testTimes = performTests(powerOfN, avgOf, iterations);
			SaveResultsFile(testTimes, powerOfN, iterations);
			//Multiply by self each time to get powers of n 
			powerOfN = powerOfN * n;
		}
		
	}

}
